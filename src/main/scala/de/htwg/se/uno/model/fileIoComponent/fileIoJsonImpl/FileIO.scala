package de.htwg.se.uno.model.fileIoComponent.fileIoJsonImpl

import com.google.inject.{Guice, Key}
import com.google.inject.name.Names
import de.htwg.se.uno.UnoModule
import de.htwg.se.uno.model.gameComponent.gameBaseImpl
import de.htwg.se.uno.model.fileIoComponent.FileIOInterface
import de.htwg.se.uno.model.gameComponent.gameBaseImpl.{Card, Color, Value}
import de.htwg.se.uno.model.gameComponent.GameInterface
import de.htwg.se.uno.model.gameComponent.gameBaseImpl.Color
import play.api.libs.json.*

import scala.collection.mutable.ListBuffer
import scala.io.Source

class FileIO extends  FileIOInterface{
  override def load: GameInterface = {
    var game: GameInterface = null
    val source: String = Source.fromFile("game.json").getLines().mkString
    val json: JsValue = Json.parse(source)
    val injector = Guice.createInjector(new UnoModule)

    val numOfPlayers = (json \ "game" \ "numOfPlayers").get.toString.toInt
    numOfPlayers match {
      case 2 => game = injector.getInstance(Key.get(classOf[GameInterface], Names.named("2 Players")))
      case 3 => game = injector.getInstance(Key.get(classOf[GameInterface], Names.named("3 Players")))
      case 4 => game = injector.getInstance(Key.get(classOf[GameInterface], Names.named("4 Players")))
    }

    val activePlayer = (json \ "game" \ "activePlayer").get.toString.toInt
    while (activePlayer != game.getActivePlayer()) {
      game = game.setActivePlayer()
    }

    val direction = (json \ "game" \ "direction").get.toString.toBoolean
    if (direction != game.getDirection()) {
      game = game.setDirection()
    }

    val anotherPull = (json \ "game" \ "anotherPull").get.toString.toBoolean
    game = game.setAnotherPull(anotherPull)

    var cards = new ListBuffer[Card]()
    for (color <- Color.values) {
      for (value <- Value.values) {
        cards += gameBaseImpl.Card(color, value)
      }
    }

    game = game.clearAllLists()

    val specialTop = (json \ "game" \ "specialTop").get.toString.toInt
    game = game.setSpecialTop(specialTop)

    var i = 0
    for (listNumber <- 0 to 5) {
      val listLength = (json \\ "length")(listNumber).as[Int]
      for (_ <- 0 until listLength) {
        val card = (json \\ "card")(i).as[String]
        for (i <- cards.indices) {
          if (cards(i).toString.equals(card)) {
            game = game.setAllCards(listNumber, cards(i))
          }
        }
        i += 1
      }
    }

    game
  }

  def gameToJson(game: GameInterface): JsValue = {
    Json.obj(
      "game" -> Json.obj(
        "numOfPlayers" -> JsNumber(game.getNumOfPlayers()),
        "activePlayer" -> JsNumber(game.getActivePlayer()),
        "direction" -> JsBoolean(game.getDirection()),
        "anotherPull" -> JsBoolean(game.getAnotherPull()),
        "specialTop" -> JsNumber(game.getSpecialTop()),
        "cardLists" -> Json.toJson(
          for {
            listNumber <- 0 to 5
            cardNumber <- 0 until game.getLength(listNumber)
          } yield {
            Json.obj(
              "card" -> JsString(game.getAllCards(listNumber, cardNumber))
            )
          }
        ),
        "listLengths" -> Json.toJson(
          for {
            i <- 0 to 5
          } yield {
            Json.obj(
            "length" -> JsNumber(game.getLength(i))
            )
          }
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
