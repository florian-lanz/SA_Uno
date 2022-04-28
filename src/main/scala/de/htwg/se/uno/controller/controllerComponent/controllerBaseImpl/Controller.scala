package de.htwg.se.uno.controller.controllerComponent.controllerBaseImpl

import com.google.inject.{Guice, Inject, Injector, Key}
import com.google.inject.name.Names
import de.htwg.se.uno.UnoModule
import de.htwg.se.uno.controller.controllerComponent.*
import de.htwg.se.uno.util.UndoManager
import play.api.libs.json.{JsValue, Json}
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpMethods, HttpRequest}
import akka.http.scaladsl.unmarshalling.Unmarshaller

import scala.concurrent.ExecutionContextExecutor
import scala.swing.{Color, Publisher}
import scala.util.{Failure, Success, Try}

class Controller(var gameJson: JsValue = Json.obj()) extends ControllerInterface with Publisher:
  private var undoManager = new UndoManager
  val injector: Injector = Guice.createInjector(new UnoModule)
  private var controllerEventString = "Du bist dran. Mögliche Befehle: q, n, t, s [Karte], g, u, r"
  private var savedSpecialCard = ""
  var undoList: List[String] = List()
  var redoList: List[String] = List()
  implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "my-system")
  implicit val executionContext: ExecutionContextExecutor = system.executionContext

  def createGame(size: Int): Unit =
    Http().singleRequest(
      HttpRequest(
        method = HttpMethods.POST,
        uri = "http://localhost:8082/create-game",
        entity = HttpEntity(ContentTypes.`application/json`,
          Json.obj(
            "gameSize" -> size
          ).toString
        )
      )
    ).onComplete {
      case Success(value) =>
        Unmarshaller.stringUnmarshaller(value.entity).onComplete {
          case Success(value) =>
            gameJson = Json.parse(value)
            initialize()
          case Failure(_) => controllerEvent("modelRequestError")
        }
      case Failure(_) => controllerEvent("modelRequestError")
    }

  def initialize(): Unit =
    savedSpecialCard = ""
    undoManager = new UndoManager
    undoList = List()
    redoList = List()
    controllerEvent("yourTurn")
    publish(new GameSizeChanged)

  def set(string: String, color: Int = 0): Unit =
    if string.charAt(0) != 'S' || color != 0 then
      if nextTurn() then
        val s = (gameJson \ "game" \ "playerCards").as[List[String]]
        undoList = gameJson.toString :: undoList
        changeActivePlayer()

        def afterPushCommand(): Unit =
          if !s.equals((gameJson \ "game" \ "playerCards").as[List[String]]) then
            controllerEvent("enemyTurn")
            changeGameJson(alreadyPulledNew = false)
            publish(new GameChanged)
            won()
          else
            resetActivePlayer()
            controllerEvent("pushCardNotAllowed")
            publish(new GameNotChanged)

        undoManager.doStep(new PushCommand(string, color, this, afterPushCommand))
      else
        controllerEvent("enemyTurn")
        publish(new GameNotChanged)
    else
      savedSpecialCard = string
      controllerEvent("chooseColor")
      publish(new ChooseColor)

  def get(): Unit =
    if nextTurn() then
      val b = (gameJson \ "game" \ "anotherPull").get.toString.toBoolean
      undoList = gameJson.toString :: undoList
      changeActivePlayer()
      val activePlayer = (gameJson \ "game" \ "activePlayer").get.toString.toInt

      def afterPullCommand(): Unit =
        val activePlayer2 = (gameJson \ "game" \ "activePlayer").get.toString.toInt
        controllerEvent("enemyTurn")
        if b then
          changeGameJson(alreadyPulledNew = false)
        if (gameJson \ "game" \ "anotherPull").get.toString.toBoolean then
          resetActivePlayer()
          controllerEvent("yourTurn")
        if activePlayer != activePlayer2 then
          resetActivePlayer()
          controllerEvent("pullCardNotAllowed")
        publish(new GameChanged)
        shuffle()

      undoManager.doStep(new PullCommand(this, afterPullCommand))

    else
      controllerEvent("enemyTurn")
      publish(new GameNotChanged)


  def enemy(): Unit =
    val enemyIndex = nextEnemy() - 1
    undoList = gameJson.toString :: undoList
    changeActivePlayer()

    def afterEnemyCommand(): Unit =
      if nextTurn() then
        controllerEvent("yourTurn")
      else
        controllerEvent("enemyTurn")
      if (gameJson \ "game" \ "anotherPull").get.toString.toBoolean then
        resetActivePlayer()
        controllerEvent("enemyTurn")
      publish(new GameChanged)
      shuffle()
      won()

    undoManager.doStep(new EnemyCommand(this, enemyIndex, afterEnemyCommand))

  def undo(): Unit =
    var result = undoManager.undoStep()
    result match
      case Success(value) =>
        controllerEvent("undo")
        while !nextTurn() do
          result = undoManager.undoStep()
          result match
            case Success(_) => controllerEvent("undo")
            case Failure(_) => controllerEvent("couldNotUndo")
      case Failure(_) => controllerEvent("couldNotUndo")
    publish(new GameChanged)
    won()

  def redo(): Unit =
    val result = undoManager.redoStep()
    result match
      case Success(_) => controllerEvent("redo")
      case Failure(_) => controllerEvent("couldNotRedo")
    publish(new GameChanged)
    shuffle()
    won()

  def save(): Unit =
    Http().singleRequest(
      HttpRequest(
        method = HttpMethods.POST,
        uri = "http://localhost:8081/save",
        entity = HttpEntity(ContentTypes.`application/json`, gameJson.toString)
      )
    ).onComplete {
      case Success(value) =>
        Unmarshaller.stringUnmarshaller(value.entity).onComplete {
          case Success(value) =>
            if value.equals("Success") then
              controllerEvent("save")
              publish(new GameChanged)
            else
              controllerEvent("couldNotSave")
              publish(new GameChanged)
          case Failure(_) =>
            controllerEvent("couldNotSave")
            publish(new GameChanged)
        }
      case Failure(_) =>
        controllerEvent("couldNotSave")
        publish(new GameChanged)
    }

  def load(): Unit =
    Http().singleRequest(
      HttpRequest(
        method = HttpMethods.GET,
        uri = "http://localhost:8081/load"
      )
    ).onComplete {
      case Success(value) =>
        Unmarshaller.stringUnmarshaller(value.entity).onComplete {
          case Success(value) =>
            if value.equals("Failure") then
              controllerEvent("couldNotLoad")
              publish(new GameChanged)
            else
              gameJson = Json.parse(value)
              savedSpecialCard = ""
              undoManager = new UndoManager
              controllerEvent("load")
              publish(new GameChanged)
          case Failure(_) =>
            controllerEvent("couldNotLoad")
            publish(new GameChanged)
        }
      case Failure(_) =>
        controllerEvent("couldNotLoad")
        publish(new GameChanged)
    }

  def won(): Unit =
    if (gameJson \ "game" \ "playerCards").as[List[String]].isEmpty then
      controllerEvent("won")
      publish(new GameEnded)
    else if (gameJson \ "game" \ "enemy1Cards").as[List[String]].isEmpty then
      controllerEvent("lost")
      publish(new GameEnded)
    else if (gameJson \ "game" \ "numOfPlayers").get.toString.toInt >= 3 && (gameJson \ "game" \ "enemy2Cards").as[List[String]].isEmpty then
      controllerEvent("lost")
      publish(new GameEnded)
    else if (gameJson \ "game" \ "numOfPlayers").get.toString.toInt >= 4 && (gameJson \ "game" \ "enemy3Cards").as[List[String]].isEmpty then
      controllerEvent("lost")
      publish(new GameEnded)
    else
      controllerEvent("idle")

  def shuffle(): Unit =
    if (gameJson \ "game" \ "coveredCardStack").as[List[String]].length <= 16 then
      undoList = gameJson.toString :: undoList

      def afterShuffleCommand(): Unit =
        controllerEvent("shuffled")
        publish(new GameChanged)

      undoManager.doStep(new ShuffleCommand(this, afterShuffleCommand))

    else
      controllerEvent("idle")

  def getLength(list: Int): Int =
    list match
      case 0 => (gameJson \ "game" \ "enemy1Cards").as[List[String]].length
      case 1 => (gameJson \ "game" \ "enemy2Cards").as[List[String]].length
      case 2 => (gameJson \ "game" \ "enemy3Cards").as[List[String]].length
      case 3 => (gameJson \ "game" \ "openCardStack").as[List[String]].length
      case 4 => (gameJson \ "game" \ "playerCards").as[List[String]].length
      case _ => (gameJson \ "game" \ "coveredCardStack").as[List[String]].length

  def getCardText(list: Int, index: Int): String =
    if list == 3 && index == 1 then (gameJson \ "game" \ "openCardStack").as[List[String]].head
    else if list == 3 && index == 2 then "Do Step"
    else if list == 4 then
      val playerCards: List[String] = (gameJson \ "game" \ "playerCards").as[List[String]]
      playerCards(index)
    else "Uno"

  def getGuiCardText(list: Int, index: Int): String =
    if list == 3 && index == 1 then 
      val firstChar = (gameJson \ "game" \ "openCardStack").as[List[String]].head(1).toString
      val secondChar = if firstChar == " " then (gameJson \ "game" \ "openCardStack").as[List[String]].head(2).toString else " "
      val thirdChar = if firstChar == " " then " " else (gameJson \ "game" \ "openCardStack").as[List[String]].head(2).toString
      firstChar + secondChar + thirdChar 
    else if list == 3 && index == 2 then "Do Step"
    else if list == 4 then 
      val playerCards = (gameJson \ "game" \ "playerCards").as[List[String]]
      val firstChar = playerCards(index)(1).toString
      val secondChar = if firstChar == " " then playerCards(index)(2).toString else " "
      val thirdChar = if firstChar == " " then " " else playerCards(index)(2).toString
      firstChar + secondChar + thirdChar 
    else "Uno"

  def resetActivePlayer(): Unit =
    changeGameJson(directionNew = !(gameJson \ "game" \ "direction").get.toString.toBoolean)
    changeActivePlayer()
    changeGameJson(directionNew = !(gameJson \ "game" \ "direction").get.toString.toBoolean)

  def getNumOfPlayers: Int = (gameJson \ "game" \ "numOfPlayers").get.toString.toInt
  
  def nextTurn(): Boolean =
    val activePlayer = (gameJson \ "game" \ "activePlayer").get.toString.toInt
    val direction = (gameJson \ "game" \ "direction").get.toString.toBoolean
    val numOfPlayers = (gameJson \ "game" \ "numOfPlayers").get.toString.toInt
    (activePlayer == 1 && (!direction || numOfPlayers == 2)) ||
      (activePlayer == 2 && direction && numOfPlayers == 3) ||
      (activePlayer == 3 && direction && numOfPlayers == 4)

  def getHs2: String = savedSpecialCard

  def nextEnemy(): Int =
    val activePlayer = (gameJson \ "game" \ "activePlayer").get.toString.toInt
    val direction = (gameJson \ "game" \ "direction").get.toString.toBoolean
    val numOfPlayers = (gameJson \ "game" \ "numOfPlayers").get.toString.toInt
    if numOfPlayers == 2 || (activePlayer == 0 && direction) || (activePlayer == 2 && !direction) then
      1
    else if (numOfPlayers >= 3 && activePlayer == 1 && direction) || (numOfPlayers == 3 && activePlayer == 0 && !direction) || (activePlayer == 3 && !direction) then
      2
    else
      3

  def changeActivePlayer(): Unit = if nextTurn() then changeGameJson(activePlayerNew = 0) else changeGameJson(activePlayerNew = nextEnemy())

  def gameToJson(): String = Json.prettyPrint(gameJson)

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
      case "couldNotLoad" => "Konnte keinen Spielstand laden!"
      case "couldNotSave" => "Spielstand konnte nicht gespeichert werden"
      case "couldNotUndo" => "Es konnte Kein Spielzug rückgängig gemacht werden"
      case "couldNotRedo" => "Es konnte kein Spielzug wiederhergestellt werden"
      case "modelRequestError" => "Fehler bei der Kommunikation mit dem Model"
      case "chooseColor" => "Wähle eine Farbe"
      case "shuffled" => "Verdeckter Kartenstapel wurde neu gemischt"
      case "idle" => controllerEventString
    controllerEventString

  def changeGameJson(alreadyPulledNew: Boolean = (gameJson \ "game" \ "anotherPull").get.toString.toBoolean,
                     directionNew: Boolean = (gameJson \ "game" \ "direction").get.toString.toBoolean,
                     activePlayerNew: Int = (gameJson \ "game" \ "activePlayer").get.toString.toInt): Unit =
    gameJson = Json.obj(
      "game" -> Json.obj(
        "numOfPlayers" -> (gameJson \ "game" \ "numOfPlayers").get,
        "activePlayer" -> activePlayerNew,
        "direction" -> directionNew,
        "anotherPull" -> alreadyPulledNew,
        "specialCard" -> (gameJson \ "game" \ "specialCard").get,
        "enemy1Cards" -> (gameJson \ "game" \ "enemy1Cards").get,
        "enemy2Cards" -> (gameJson \ "game" \ "enemy2Cards").get,
        "enemy3Cards" -> (gameJson \ "game" \ "enemy3Cards").get,
        "openCardStack" -> (gameJson \ "game" \ "openCardStack").get,
        "playerCards" -> (gameJson \ "game" \ "playerCards").get,
        "coveredCardStack" -> (gameJson \ "game" \ "coveredCardStack").get
      )
    )