package de.htwg.se.uno.model.fileIoComponent.fileIoXmlImpl

import com.google.inject.{Guice, Key}
import com.google.inject.name.Names
import de.htwg.se.uno.UnoModule
import de.htwg.se.uno.model.fileIoComponent.FileIOInterface
import de.htwg.se.uno.model.gameComponent.gameBaseImpl.*
import de.htwg.se.uno.model.gameComponent.GameInterface
import play.api.libs.json.{JsValue, Json}

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer
import scala.util.Try
import scala.xml.{Node, PrettyPrinter}

class FileIO extends FileIOInterface :
  override def load(source: String = "game.xml"): Try[GameInterface] = Try {
    val file = if source.equals("game.xml") then scala.xml.XML.loadFile(source) else scala.xml.XML.loadString(source)
    val injector = Guice.createInjector(new UnoModule)

    val numOfPlayers = (file \\ "game" \\ "@numOfPlayers").text.toInt
    val game = injector.getInstance(Key.get(classOf[GameInterface], Names.named(numOfPlayers + " Players")))
    val activePlayer = (file \\ "game" \ "@activePlayer").text.toInt
    val direction = (file \\ "game" \ "@direction").text.toBoolean
    val alreadyPulled = (file \\ "game" \ "@anotherPull").text.toBoolean
    val revealedCardEffect = (file \\ "game" \ "@specialTop").text.toInt
    val cards = CardStack().createCoveredCardStack(1, 1).cardStack

    def createCardList(listName: String): List[Card] =
      (file \\ listName \ "card").toList.flatMap(cardString => cards.filter(card => card.toString.equals((cardString \ "@card").text)))

    game.copyGame(
      activePlayer = activePlayer,
      direction = direction,
      alreadyPulled = alreadyPulled,
      revealedCardEffect = revealedCardEffect,
      enemies = List(
        Enemy(createCardList("enemy1Cards")),
        Enemy(createCardList("enemy2Cards")),
        Enemy(createCardList("enemy3Cards"))
      ),
      player = Player(createCardList("playerCards")),
      revealedCards = createCardList("revealedCards"),
      coveredCards = createCardList("coveredCards")
    )
  }

  override def save(game: GameInterface): Try[Unit] = Try { saveString(game) }

  def saveString(game: GameInterface): Unit =
    import java.io._
    val pw = new PrintWriter(new File("game.xml"))
    val prettyPrinter = new PrettyPrinter(120, 4)
    val xml = prettyPrinter.format(gameToXml(game))
    pw.write(xml)
    pw.close()

  def gameToString(game: GameInterface): String = gameToXml(game).mkString

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