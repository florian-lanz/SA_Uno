package de.htwg.se.uno.model.fileIoComponent.fileIoXmlImpl

import com.google.inject.{Guice, Key}
import com.google.inject.name.Names
import de.htwg.se.uno.UnoModule
import de.htwg.se.uno.model.gameComponent.gameBaseImpl
import de.htwg.se.uno.model.fileIoComponent.FileIOInterface
import de.htwg.se.uno.model.gameComponent.gameBaseImpl._
import de.htwg.se.uno.model.gameComponent.GameInterface
import play.api.libs.json.{JsValue, Json}

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer
import scala.xml.{Node, PrettyPrinter}

class FileIO extends FileIOInterface :
  override def load(source: String = "game.xml"): GameInterface =
    val file = if source.equals("game.xml") then scala.xml.XML.loadFile(source) else scala.xml.XML.loadString(source)
    val injector = Guice.createInjector(new UnoModule)

    val numOfPlayers = (file \\ "game" \\ "@numOfPlayers").text.toInt
    var game = injector.getInstance(Key.get(classOf[GameInterface], Names.named(numOfPlayers + " Players")))

    val activePlayer = (file \\ "game" \ "@activePlayer").text.toInt
    while activePlayer != game.activePlayer do game = game.changeActivePlayer()

    val direction = (file \\ "game" \ "@direction").text.toBoolean
    if direction != game.direction then game = game.copyGame(direction = !game.direction)

    val anotherPull = (file \\ "game" \ "@anotherPull").text.toBoolean
    game = game.copyGame(alreadyPulled = anotherPull)

    val specialTop = (file \\ "game" \ "@specialTop").text.toInt
    game = game.copyGame(revealedCardEffect = specialTop)

    game = game.copyGame(enemies = List(Enemy(), Enemy(), Enemy()), player = Player(), revealedCards = List(), coveredCards = List())

    val cards = CardStack().createCoveredCardStack(1, 1).cardStack

    def createCardLists(listName: String, listIndex: Int): GameInterface = {
      val list = (file \\ listName \ "card").toList
      println(list)
      @tailrec
      def recursionList(i: Int, game: GameInterface): GameInterface = {
        if i < list.length then
          val newGame = recursionCards(0, (list(i) \ "@card").text, game)
          recursionList(i + 1, newGame)
        else game
      }

      @tailrec
      def recursionCards(j: Int, card: String, game: GameInterface): GameInterface = {
        if j < cards.length && cards(j).toString.equals(card) then
          game.addCardToList(listIndex, cards(j))
        else if j < cards.length then recursionCards(j + 1, card, game)
        else game
      }

      recursionList(0, game).reverseList(listIndex)
    }

    game = createCardLists("enemy1Cards", 0)
    game = createCardLists("enemy2Cards", 1)
    game = createCardLists("enemy3Cards", 2)
    game = createCardLists("revealedCards", 3)
    game = createCardLists("playerCards", 4)
    game = createCardLists("coveredCards", 5)

    game

  override def save(game: GameInterface): Unit = saveString(game)

  def saveString(game: GameInterface): Unit =
    import java.io._
    val pw = new PrintWriter(new File("game.xml"))
    val prettyPrinter = new PrettyPrinter(120, 4)
    val xml = prettyPrinter.format(gameToXml(game))
    pw.write(xml)
    pw.close()

  def gameToXml(game: GameInterface): Node =
    <game
    numOfPlayers={game.numOfPlayers.toString}
    activePlayer={game.activePlayer.toString}
    direction={game.direction.toString}
    anotherPull={game.alreadyPulled.toString}
    specialTop={game.revealedCardEffect.toString}>
      <enemy1Cards>
        {for cardNumber <- game.enemies.head.enemyCards.indices
        yield <card card={game.enemies.head.enemyCards(cardNumber).toString}/>}
      </enemy1Cards>
      <enemy2Cards>
        {for cardNumber <- game.enemies(1).enemyCards.indices
        yield <card card={game.enemies(1).enemyCards(cardNumber).toString}/>}
      </enemy2Cards>
      <enemy3Cards>
        {for cardNumber <- game.enemies(2).enemyCards.indices
        yield <card card={game.enemies(2).enemyCards(cardNumber).toString}/>}
      </enemy3Cards>
      <revealedCards>
        {for cardNumber <- game.revealedCards.indices
        yield <card card={game.revealedCards(cardNumber).toString}/>}
      </revealedCards>
      <playerCards>
        {for cardNumber <- game.player.handCards.indices
        yield <card card={game.player.handCards(cardNumber).toString}/>}
      </playerCards>
      <coveredCards>
        {for cardNumber <- game.coveredCards.indices
        yield <card card={game.coveredCards(cardNumber).toString}/>}
      </coveredCards>
    </game>

  def gameToString(game: GameInterface): String = gameToXml(game).mkString