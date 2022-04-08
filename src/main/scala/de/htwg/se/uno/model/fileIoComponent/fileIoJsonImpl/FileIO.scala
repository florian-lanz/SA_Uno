package de.htwg.se.uno.model.fileIoComponent.fileIoJsonImpl

import com.google.inject.{Guice, Key}
import com.google.inject.name.Names
import de.htwg.se.uno.UnoModule
import de.htwg.se.uno.model.gameComponent.gameBaseImpl
import de.htwg.se.uno.model.fileIoComponent.FileIOInterface
import de.htwg.se.uno.model.gameComponent.gameBaseImpl._
import de.htwg.se.uno.model.gameComponent.GameInterface
import play.api.libs.json.*

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer
import scala.io.Source

class FileIO extends FileIOInterface:
  override def load(source: String = Source.fromFile("game.json").getLines().mkString): GameInterface =
    val json: JsValue = Json.parse(source)
    val injector = Guice.createInjector(new UnoModule)

    val numOfPlayers = (json \ "game" \ "numOfPlayers").get.toString.toInt
    var game = injector.getInstance(Key.get(classOf[GameInterface], Names.named(numOfPlayers + " Players")))

    val activePlayer = (json \ "game" \ "activePlayer").get.toString.toInt
    while activePlayer != game.activePlayer do game = game.changeActivePlayer()
    val direction = (json \ "game" \ "direction").get.toString.toBoolean
    game = game.copyGame(direction = direction)

    val anotherPull = (json \ "game" \ "anotherPull").get.toString.toBoolean
    game = game.copyGame(alreadyPulled = anotherPull)
    val cards = CardStack().createCoveredCardStack(1, 1).cardStack
    
    def createCardList(listName: String): List[Card] =
      (json \ "game" \ listName).as[List[String]].flatMap(cardString => cards.filter(card => card.toString.equals(cardString)))

    game = createCardLists("enemy1Cards", 0)
    game = createCardLists("enemy2Cards", 1)
    game = createCardLists("enemy3Cards", 2)
    game = createCardLists("openCardStack", 3)
    game = createCardLists("playerCards", 4)
    game = createCardLists("coveredCardStack", 5)

    game

  def gameToJson(game: GameInterface): JsValue =
    Json.obj(
      "game" -> Json.obj(
        "numOfPlayers" -> JsNumber(game.numOfPlayers),
        "activePlayer" -> JsNumber(game.activePlayer),
        "direction" -> JsBoolean(game.direction),
        "anotherPull" -> JsBoolean(game.alreadyPulled),
        "specialCard" -> JsNumber(game.revealedCardEffect),
        "enemy1Cards" -> JsArray(for cardNumber <- game.enemies.head.enemyCards.indices yield JsString(game.enemies.head.enemyCards(cardNumber).toString)),
        "enemy2Cards" -> JsArray(for cardNumber <- game.enemies(1).enemyCards.indices yield JsString(game.enemies(1).enemyCards(cardNumber).toString)),
        "enemy3Cards" -> JsArray(for cardNumber <- game.enemies(2).enemyCards.indices yield JsString(game.enemies(2).enemyCards(cardNumber).toString)),
        "openCardStack" -> JsArray(for cardNumber <- game.revealedCards.indices yield JsString(game.revealedCards(cardNumber).toString)),
        "playerCards" -> JsArray(for cardNumber <- game.player.handCards.indices yield JsString(game.player.handCards(cardNumber).toString)),
        "coveredCardStack" -> JsArray(for cardNumber <- game.coveredCards.indices yield JsString(game.coveredCards(cardNumber).toString))
      )
    )

  def gameToString(game: GameInterface): String = gameToJson(game).toString

  override def save(grid: GameInterface): Unit =
    import java.io._
    val pw = new PrintWriter(new File("game.json"))
    pw.write(Json.prettyPrint(gameToJson(grid)))
    pw.close()