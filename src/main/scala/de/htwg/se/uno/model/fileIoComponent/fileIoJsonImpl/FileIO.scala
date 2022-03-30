package de.htwg.se.uno.model.fileIoComponent.fileIoJsonImpl

import com.google.inject.{Guice, Key}
import com.google.inject.name.Names
import de.htwg.se.uno.UnoModule
import de.htwg.se.uno.model.gameComponent.gameBaseImpl
import de.htwg.se.uno.model.fileIoComponent.FileIOInterface
import de.htwg.se.uno.model.gameComponent.gameBaseImpl.{
  Card,
  CardStack,
  Color,
  Value
}
import de.htwg.se.uno.model.gameComponent.GameInterface
import play.api.libs.json.*

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer
import scala.io.Source

class FileIO extends FileIOInterface {
  override def load(
      source: String = Source.fromFile("game.json").getLines().mkString
  ): GameInterface = {
    val json: JsValue = Json.parse(source)
    val injector = Guice.createInjector(new UnoModule)

    val numOfPlayers = (json \ "game" \ "numOfPlayers").get.toString.toInt
    var game: GameInterface = numOfPlayers match {
      case 2 =>
        injector.getInstance(
          Key.get(classOf[GameInterface], Names.named("2 Players"))
        )
      case 3 =>
        injector.getInstance(
          Key.get(classOf[GameInterface], Names.named("3 Players"))
        )
      case 4 =>
        injector.getInstance(
          Key.get(classOf[GameInterface], Names.named("4 Players"))
        )
    }

    val activePlayer = (json \ "game" \ "activePlayer").get.toString.toInt
    while (activePlayer != game.getActivePlayer) {
      game = game.setActivePlayer()
    }

    val direction = (json \ "game" \ "direction").get.toString.toBoolean
    if (direction != game.getDirection) {
      game = game.setDirection()
    }

    val anotherPull = (json \ "game" \ "anotherPull").get.toString.toBoolean
    game = game.setAnotherPull(anotherPull)

    val cards = CardStack().createCoveredCardStack(1, 1).cardStack

    game = game.clearAllLists()

    // val specialTop = (json \ "game" \ "specialCard").get.toString.toInt
    // game = game.setSpecialTop(specialTop)

    def createCardLists(listName: String, listIndex: Int): GameInterface = {
      val list = (json \ "game" \ listName).as[List[String]]

      @tailrec
      def recursionList(i: Int, game: GameInterface): GameInterface = {
        if i < list.length then
          val newGame = recursionCards(0, list(i), game)
          recursionList(i + 1, newGame)
        else game
      }

      @tailrec
      def recursionCards(
          j: Int,
          card: String,
          game: GameInterface
      ): GameInterface = {
        if j < cards.length && cards(j).toString.equals(card) then
          game.setAllCards(listIndex, cards(j))
        else if j < cards.length then recursionCards(j + 1, card, game)
        else game
      }

      recursionList(0, game)
    }

    game = createCardLists("enemy1Cards", 0)
    game = createCardLists("enemy2Cards", 1)
    game = createCardLists("enemy3Cards", 2)
    game = createCardLists("openCardStack", 3)
    game = createCardLists("playerCards", 4)
    game = createCardLists("coveredCardStack", 5)

    game
  }

  def gameToJson(game: GameInterface): JsValue = {
    Json.obj(
      "game" -> Json.obj(
        "numOfPlayers" -> JsNumber(game.getNumOfPlayers),
        "activePlayer" -> JsNumber(game.getActivePlayer),
        "direction" -> JsBoolean(game.getDirection),
        "anotherPull" -> JsBoolean(game.getAnotherPull),
        "specialCard" -> JsNumber(game.getSpecialTop),
        "enemy1Cards" -> JsArray(
          for cardNumber <- 0 until game.getLength(0)
          yield JsString(game.getAllCards(0, cardNumber))
        ),
        "enemy2Cards" -> JsArray(
          for cardNumber <- 0 until game.getLength(1)
          yield JsString(game.getAllCards(1, cardNumber))
        ),
        "enemy3Cards" -> JsArray(
          for cardNumber <- 0 until game.getLength(2)
          yield JsString(game.getAllCards(2, cardNumber))
        ),
        "openCardStack" -> JsArray(
          for cardNumber <- 0 until game.getLength(3)
          yield JsString(game.getAllCards(3, cardNumber))
        ),
        "playerCards" -> JsArray(
          for cardNumber <- 0 until game.getLength(4)
          yield JsString(game.getAllCards(4, cardNumber))
        ),
        "coveredCardStack" -> JsArray(
          for cardNumber <- 0 until game.getLength(5)
          yield JsString(game.getAllCards(5, cardNumber))
        )
      )
    )
  }

  override def save(grid: GameInterface): Unit = {
    import java.io._
    val pw = new PrintWriter(new File("game.json"))
    pw.write(Json.prettyPrint(gameToJson(grid)))
    pw.close()
  }
}
