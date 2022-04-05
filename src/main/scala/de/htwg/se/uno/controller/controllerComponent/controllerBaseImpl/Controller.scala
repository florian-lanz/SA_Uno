package de.htwg.se.uno.controller.controllerComponent.controllerBaseImpl

import com.google.inject.{Guice, Inject, Injector, Key}
import com.google.inject.name.Names
import de.htwg.se.uno.UnoModule
import de.htwg.se.uno.controller.controllerComponent.*
import de.htwg.se.uno.model.fileIoComponent.FileIOInterface
import de.htwg.se.uno.model.gameComponent.GameInterface
import de.htwg.se.uno.util.UndoManager

import scala.swing.{Color, Publisher}

class Controller @Inject() (var game: GameInterface) extends ControllerInterface with Publisher:
  private var undoManager = new UndoManager
  val injector: Injector = Guice.createInjector(new UnoModule)
  val fileIo: FileIOInterface = injector.getInstance(classOf[FileIOInterface])
  private var controllerEventString = "Du bist dran. Mögliche Befehle: q, n, t, s [Karte], g, u, r"
  private var savedSpecialCard = ""
  var undoList: List[String] = List()
  var redoList: List[String] = List()

  def createGame(size: Int): Unit =
    game = injector.getInstance(Key.get(classOf[GameInterface], Names.named(size + " Players")))
    game = game.createGame()
    initialize()

  def createTestGame(): Unit =
    game = injector.getInstance(Key.get(classOf[GameInterface], Names.named("4 Players")))
    game = game.createTestGame()
    initialize()

  def initialize(): Unit =
    savedSpecialCard = ""
    undoManager = new UndoManager
    undoList = List()
    redoList = List()
    controllerEvent("yourTurn")
    publish(new GameSizeChanged)

  def set(string: String, color: Int = 0): Unit =
    if string.charAt(0) != 'S' || color != 0 then
      if game.nextTurn() then
        val s = gameToString
        undoList = fileIo.gameToString(game).toString :: undoList
        game = game.changeActivePlayer()
        undoManager.doStep(new PushCommand(string, color, this))
        if !s.equals(gameToString) then
          controllerEvent("enemyTurn")
          game = game.copyGame(alreadyPulled = false)
          publish(new GameChanged)
          won()
        else
          resetActivePlayer()
          controllerEvent("pushCardNotAllowed")
          publish(new GameNotChanged)
      else
        controllerEvent("enemyTurn")
        publish(new GameNotChanged)
    else
      savedSpecialCard = string
      controllerEvent("chooseColor")
      publish(new ChooseColor)

  def get(): Unit =
    if game.nextTurn() then
      val b = game.alreadyPulled
      undoList = fileIo.gameToString(game).toString :: undoList
      game = game.changeActivePlayer()
      val activePlayer = game.activePlayer
      undoManager.doStep(new PullCommand(this))
      val activePlayer2 = game.activePlayer
      controllerEvent("enemyTurn")
      if b then
        game = game.copyGame(alreadyPulled = false)
      if game.alreadyPulled then
        resetActivePlayer()
        controllerEvent("yourTurn")
      if activePlayer != activePlayer2 then
        resetActivePlayer()
        controllerEvent("pullCardNotAllowed")
      publish(new GameChanged)
      shuffle()
    else
      controllerEvent("enemyTurn")
      publish(new GameNotChanged)

  def enemy(): Unit =
    val enemyIndex = game.nextEnemy() - 1
    undoList = fileIo.gameToString(game).toString :: undoList
    game = game.changeActivePlayer()
    undoManager.doStep(EnemyCommand(this, enemyIndex))
    if game.nextTurn() then controllerEvent("yourTurn")
    else controllerEvent("enemyTurn")
    if game.alreadyPulled then
      controllerEvent("enemyTurn")
    publish(new GameChanged)
    shuffle()
    won()

  def undo(): Unit =
    undoManager.undoStep()
    while !game.nextTurn() do undoManager.undoStep()
    controllerEvent("undo")
    publish(new GameChanged)
    won()

  def redo(): Unit =
    undoManager.redoStep()
    controllerEvent("redo")
    publish(new GameChanged)
    shuffle()
    won()

  def save(): Unit =
    fileIo.save(game)
    controllerEvent("save")
    publish(new GameChanged)

  def load(): Unit =
    game = fileIo.load()
    savedSpecialCard = ""
    undoManager = new UndoManager
    controllerEvent("load")
    publish(new GameChanged)

  def won(): Unit =
    if game.player.handCards.isEmpty then
      controllerEvent("won")
      publish(new GameEnded)
    else if game.enemies.head.enemyCards.isEmpty then
      controllerEvent("lost")
      publish(new GameEnded)
    else if game.numOfPlayers >= 3 && game.enemies(1).enemyCards.isEmpty then
      controllerEvent("lost")
      publish(new GameEnded)
    else if game.numOfPlayers >= 4 && game.enemies(2).enemyCards.isEmpty then
      controllerEvent("lost")
      publish(new GameEnded)
    else
      controllerEvent("idle")

  def shuffle(): Unit =
    if game.coveredCards.length <= 16 then
      undoList = fileIo.gameToString(game) :: undoList
      undoManager.doStep(new ShuffleCommand(this))
      controllerEvent("shuffled")
      publish(new GameChanged)
    else
      controllerEvent("idle")

  def getLength(list: Int): Int =
    list match
      case 0 | 1 | 2 => game.enemies(list).enemyCards.length
      case 3         => game.revealedCards.length
      case 4         => game.player.handCards.length
      case _         => game.coveredCards.length

  def getCardText(list: Int, index: Int): String =
    if list == 3 && index == 1 then game.revealedCards.head.toString
    else if list == 3 && index == 2 then "Do Step"
    else if list == 4 then game.player.handCards(index).toString
    else "Uno"

  def getGuiCardText(list: Int, index: Int): String =
    if list == 3 && index == 1 then game.revealedCards.head.toGuiString
    else if list == 3 && index == 2 then "Do Step"
    else if list == 4 then game.player.handCards(index).toGuiString
    else "Uno"

  def resetActivePlayer(): Unit =
    game = game.copyGame(direction = !game.direction)
    game = game.changeActivePlayer()
    game = game.copyGame(direction = !game.direction)

  def gameToString: String = game.toString
  def getNumOfPlayers: 2 | 3 | 4 = game.numOfPlayers
  def nextTurn(): Boolean = game.nextTurn()
  def getHs2: String = savedSpecialCard
  def nextEnemy(): Int = game.nextEnemy()

  def controllerEvent(string: String): String =
    controllerEventString = string match
      case "pushCardNotAllowed" => "Du kannst diese Karte nicht legen"
      case "enemyTurn" => "Gegner ist an der Reihe"
      case "pullCardNotAllowed" => "Du kannst keine Karte ziehen"
      case "unknownCommand" => "Befehl nicht bekannt"
      case "yourTurn" => "Du bist dran. Mögliche Befehle: q, n [2 | 3 | 4], t, s Karte [Farbe], g, u, r, d, sv, ld"
      case "won" => "Glückwunsch, du hast gewonnen!"
      case "lost" => "Du hast leider verloren"
      case "undo" => "Zug rückgängig gemacht"
      case "redo" => "Zug wiederhergestellt"
      case "save" => "Spiel gespeichert"
      case "load" => "Spiel geladen"
      case "chooseColor" => "Wähle eine Farbe"
      case "shuffled" => "Verdeckter Kartenstapel wurde neu gemischt"
      case "idle" => controllerEventString
    controllerEventString