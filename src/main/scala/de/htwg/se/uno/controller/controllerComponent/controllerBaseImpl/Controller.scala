package de.htwg.se.uno.controller.controllerComponent.controllerBaseImpl

import com.google.inject.{Guice, Inject, Injector, Key}
import com.google.inject.name.Names
import de.htwg.se.uno.UnoModule
import de.htwg.se.uno.controller.controllerComponent.*
import de.htwg.se.uno.model.fileIoComponent.FileIOInterface
import de.htwg.se.uno.model.gameComponent.GameInterface
import de.htwg.se.uno.util.UndoManager

import scala.swing.{Color, Publisher}

class Controller @Inject() (var game: GameInterface)
    extends ControllerInterface
    with Publisher {
  private var undoManager = new UndoManager
  val injector: Injector = Guice.createInjector(new UnoModule)
  val fileIo: FileIOInterface = injector.getInstance(classOf[FileIOInterface])
  private var controllerEventString =
    "Du bist dran. Mögliche Befehle: q, n, t, s [Karte], g, u, r"
  private var savedSpecialCard = ""
  var undoList: List[String] = List()
  var redoList: List[String] = List()

  def createGame(size: Int): Unit = {
    size match {
      case 2 =>
        game = injector.getInstance(
          Key.get(classOf[GameInterface], Names.named("2 Players"))
        )
      case 3 =>
        game = injector.getInstance(
          Key.get(classOf[GameInterface], Names.named("3 Players"))
        )
      case 4 =>
        game = injector.getInstance(
          Key.get(classOf[GameInterface], Names.named("4 Players"))
        )
      case _ =>
    }
    game = game.createGame()
    savedSpecialCard = ""
    undoManager = new UndoManager
    controllerEvent("yourTurn")
    publish(new GameSizeChanged)
  }

  def createTestGame(): Unit = {
    game = injector.getInstance(
      Key.get(classOf[GameInterface], Names.named("4 Players"))
    )
    game = game.createTestGame()
    savedSpecialCard = ""
    undoManager = new UndoManager
    controllerEvent("yourTurn")
    publish(new GameSizeChanged)
  }

  def set(string: String, color: Int = 0): Unit = {
    if (string.charAt(0) != 'S' || color != 0) {
      if (game.nextTurn()) {
        val s = gameToString
        game = game.setActivePlayer()
        undoManager.doStep(new PushCommand(string, color, this))
        if (!s.equals(gameToString)) {
          controllerEvent("enemyTurn")
          game = game.setAnotherPull()
          publish(new GameChanged)
          won()
        } else {
          game = game.setDirection()
          game = game.setActivePlayer()
          game = game.setDirection()
          controllerEvent("pushCardNotAllowed")
          publish(new GameNotChanged)
        }
      } else {
        controllerEvent("enemyTurn")
        publish(new GameNotChanged)
      }
    } else {
      savedSpecialCard = string
      controllerEvent("chooseColor")
      publish(new ChooseColor)
    }
  }
  def get(): Unit = {
    if (game.nextTurn()) {
      val b = game.getAnotherPull
      game = game.setActivePlayer()
      val activePlayer = game.getActivePlayer
      undoManager.doStep(new PullCommand(this))
      val activePlayer2 = game.getActivePlayer
      controllerEvent("enemyTurn")
      if (b) {
        game = game.setAnotherPull()
      }
      if (game.getAnotherPull) {
        game = game.setDirection()
        game = game.setActivePlayer()
        game = game.setDirection()
        controllerEvent("yourTurn")
      }
      if (activePlayer != activePlayer2) {
        game = game.setDirection()
        game = game.setActivePlayer()
        game = game.setDirection()
        controllerEvent("pullCardNotAllowed")
      }
      publish(new GameChanged)
      shuffle()
    } else {
      controllerEvent("enemyTurn")
      publish(new GameNotChanged)
    }
  }
  def enemy(): Unit = {
    game = game.setActivePlayer()
    undoManager.doStep(EnemyCommand(this, game.nextEnemy() - 1))

    if game.nextTurn() then controllerEvent("yourTurn")
    else controllerEvent("enemyTurn")

    if game.getAnotherPull then
      game.setDirection()
      game.setActivePlayer()
      game.setDirection()
      controllerEvent("enemyTurn")

    publish(new GameChanged)
    shuffle()
    won()
  }

  def undo(): Unit = {
    var undo = true
    undoManager.undoStep()
    while !game.nextTurn() && undo do undo = undoManager.undoStep()
    while !game.nextTurn() do
      game.setDirection()
      game.setActivePlayer()
      game.setDirection()
    controllerEvent("undo")
    publish(new GameChanged)
    won()
  }
  def redo(): Unit = {
    undoManager.redoStep
    controllerEvent("redo")
    publish(new GameChanged)
    shuffle()
    won()
  }

  def save(): Unit = {
    fileIo.save(game)
    controllerEvent("save")
    publish(new GameChanged)
  }

  def load(): Unit = {
    game = fileIo.load()
    savedSpecialCard = ""
    undoManager = new UndoManager
    controllerEvent("load")
    publish(new GameChanged)
  }

  def won(): Unit = {
    if (game.getLength(4) == 0) {
      controllerEvent("won")
      publish(new GameEnded)
    } else if (game.getLength(0) == 0) {
      controllerEvent("lost")
      publish(new GameEnded)
    } else if (game.getNumOfPlayers >= 3 && game.getLength(1) == 0) {
      controllerEvent("lost")
      publish(new GameEnded)
    } else if (game.getNumOfPlayers >= 4 && game.getLength(2) == 0) {
      controllerEvent("lost")
      publish(new GameEnded)
    } else {
      controllerEvent("idle")
    }
  }

  def shuffle(): Unit = {
    if (game.getLength(5) <= 16) {
      undoManager.doStep(new ShuffleCommand(this))
      controllerEvent("shuffled")
      publish(new GameChanged)
    } else {
      controllerEvent("idle")
    }
  }

  def gameToString: String = game.toString
  def getCardText(list: Int, index: Int): String = game.getCardText(list, index)
  def getGuiCardText(list: Int, index: Int): String =
    game.getGuiCardText(list, index)
  def getLength(list: Int): Int = game.getLength(list)
  def getNumOfPlayers: 2 | 3 | 4 = game.getNumOfPlayers
  def nextTurn(): Boolean = game.nextTurn()
  def getHs2: String = savedSpecialCard
  def nextEnemy(): Int = game.nextEnemy()

  def controllerEvent(string: String): String = {
    string match {
      case "pushCardNotAllowed" => {
        controllerEventString = "Du kannst diese Karte nicht legen"
        controllerEventString
      }
      case "enemyTurn" => {
        controllerEventString = "Gegner ist an der Reihe"
        controllerEventString
      }
      case "pullCardNotAllowed" => {
        controllerEventString = "Du kannst keine Karte ziehen"
        controllerEventString
      }
      case "unknownCommand" => {
        controllerEventString = "Befehl nicht bekannt"
        controllerEventString
      }
      case "yourTurn" => {
        controllerEventString =
          "Du bist dran. Mögliche Befehle: q, n [2 | 3 | 4], t, s Karte [Farbe], g, u, r, d, sv, ld"
        controllerEventString
      }
      case "won" => {
        controllerEventString = "Glückwunsch, du hast gewonnen!"
        controllerEventString
      }
      case "lost" => {
        controllerEventString = "Du hast leider verloren"
        controllerEventString
      }
      case "undo" => {
        controllerEventString = "Zug rückgängig gemacht"
        controllerEventString
      }
      case "redo" => {
        controllerEventString = "Zug wiederhergestellt"
        controllerEventString
      }
      case "save" => {
        controllerEventString = "Spiel gespeichert"
        controllerEventString
      }
      case "load" => {
        controllerEventString = "Spiel geladen"
        controllerEventString
      }
      case "chooseColor" => {
        controllerEventString = "Wähle eine Farbe"
        controllerEventString
      }
      case "shuffled" => {
        controllerEventString = "Verdeckter Kartenstapel wurde neu gemischt"
        controllerEventString
      }
      case "idle" => controllerEventString
    }
  }
}
