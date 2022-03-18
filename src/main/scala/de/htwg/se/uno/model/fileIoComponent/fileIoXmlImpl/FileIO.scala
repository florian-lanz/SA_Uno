package de.htwg.se.uno.model.fileIoComponent.fileIoXmlImpl

import com.google.inject.{Guice, Key}
import com.google.inject.name.Names
import de.htwg.se.uno.UnoModule
import de.htwg.se.uno.model.gameComponent.gameBaseImpl
import de.htwg.se.uno.model.fileIoComponent.FileIOInterface
import de.htwg.se.uno.model.gameComponent.gameBaseImpl.{Card, Color, Value}
import de.htwg.se.uno.model.gameComponent.GameInterface
import de.htwg.se.uno.model.gameComponent.gameBaseImpl.Color

import scala.collection.mutable.ListBuffer
import scala.xml.{Node, PrettyPrinter}

class FileIO extends FileIOInterface{
  override def load: GameInterface = {
    var game: GameInterface = null
    val file = scala.xml.XML.loadFile("game.xml")
    val injector = Guice.createInjector(new UnoModule)

    val numOfPlayers = (file \\ "game" \\ "@numOfPlayers").text.toInt
    numOfPlayers match {
      case 2 => game = injector.getInstance(Key.get(classOf[GameInterface], Names.named("2 Players")))
      case 3 => game = injector.getInstance(Key.get(classOf[GameInterface], Names.named("3 Players")))
      case 4 => game = injector.getInstance(Key.get(classOf[GameInterface], Names.named("4 Players")))
    }

    val activePlayer = (file \\ "game" \ "@activePlayer").text.toInt
    while (activePlayer != game.getActivePlayer()) {
      game = game.setActivePlayer()
    }

    val direction = (file \\ "game" \ "@direction").text.toBoolean
    if (direction != game.getDirection()) {
      game = game.setDirection()
    }

    val anotherPull = (file \\ "game" \ "@anotherPull").text.toBoolean
    game = game.setAnotherPull(anotherPull)

    var cards = new ListBuffer[Card]()
    for (color <- Color.values) {
      for (value <- Value.values) {
        cards += gameBaseImpl.Card(color, value)
      }
    }

    game = game.clearAllLists()

    val specialTop = (file \\ "game" \ "@specialTop").text.toInt
    game = game.setSpecialTop(specialTop)

    val listLength = (file \\ "length")
    var lengths = new ListBuffer[Int]()
    for (length <- listLength) {
      val l = (length \ "@length").text.toInt
      lengths = lengths :+ l
    }

    val cardNodes = (file \\ "card")
    var i = 0
    var j = 0
    for (card <- cardNodes) {
      if (i == lengths(j)) {
        j += 1
        while (lengths(j) == 0) {
          j += 1
        }
        i = 0
      }
      val c = (card \ "@card").text
      for (x <- cards.indices) {
        if (cards(x).toString.equals(c)) {
          game = game.setAllCards(j, cards(x))
        }
      }
      i += 1
    }

    game
  }

  override def save(game: GameInterface): Unit = saveString(game)

  def saveString(game: GameInterface): Unit = {
    import java.io._
    val pw = new PrintWriter(new File("game.xml"))
    val prettyPrinter = new PrettyPrinter(120, 4)
    val xml = prettyPrinter.format(gameToXml(game))
    pw.write(xml)
    pw.close()
  }

  def gameToXml(game: GameInterface): Node = {
    <game
      numOfPlayers={ game.getNumOfPlayers().toString }
      activePlayer={ game.getActivePlayer().toString }
      direction={ game.getDirection().toString }
      anotherPull={ game.getAnotherPull().toString }
      specialTop={ game.getSpecialTop().toString }
    >
      <cardLists>
        {
          for {
            listNumber <- 0 to 5
            cardNumber <- 0 until game.getLength(listNumber)
          } yield {
            <card card={ game.getAllCards(listNumber, cardNumber) }></card>
          }
        }
      </cardLists>
      <listLengths>
        {
          for {
            i <- 0 to 5
          } yield {
            <length length={ game.getLength(i).toString }></length>
          }
        }
      </listLengths>
    </game>
  }
}
