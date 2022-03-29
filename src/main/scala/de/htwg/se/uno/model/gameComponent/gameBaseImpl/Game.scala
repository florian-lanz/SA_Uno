package de.htwg.se.uno.model.gameComponent.gameBaseImpl

import com.fasterxml.jackson.annotation.JsonValue
import com.google.inject.Inject
import com.google.inject.name.Named
import de.htwg.se.uno.model.gameComponent.GameInterface

import scala.collection.mutable
import scala.collection.mutable.{ListBuffer, Stack}

case class Game @Inject() (@Named("DefaultPlayers") numOfPlayers: 2 | 3 | 4) extends GameInterface{
  var init = InitializeGameStrategy()

  init.initializeGame(numOfPlayers)

  var activePlayer = numOfPlayers - 1
  private var direction = true
  var alreadyPulled = false
  var revealedCardEffect = 0

  def createGame() : Game = {
    init = InitializeGameStrategy()
    init = init.initializeGame(numOfPlayers)
    activePlayer = numOfPlayers - 1
    direction = true
    alreadyPulled = false
    revealedCardEffect = 0
    this
  }
  def createTestGame() : Game = {
    init = InitializeGameStrategy(1)
    init = init.initializeGame(numOfPlayers)
    activePlayer = numOfPlayers - 1
    direction = true
    alreadyPulled = false
    revealedCardEffect = 0
    this
  }

  def pushMove(string : String, color : Int) : Game = {
    if (revealedCardEffect != - 1) {
      init.player = init.player.pushMove(string, color, this)
    } else if (revealedCardEffect == -1) {
      revealedCardEffect = 0
      setActivePlayer()
    }
    this
  }
  def pullMove() : Game = {
    if(revealedCardEffect != - 1) {
      init.player = init.player.pullMove(this)
    } else {
      revealedCardEffect = 0
      setActivePlayer()
    }
    this
  }
  def enemy() : Game = {
    if (revealedCardEffect != - 1) {
      init.enemy = init.enemy.enemy(this)
    } else if (revealedCardEffect == -1) {
      revealedCardEffect = 0
    }
    this
  }
  def enemy2() : Game = {
    if (revealedCardEffect != - 1) {
      init.enemy2 = init.enemy2.enemy(this)
    } else if (revealedCardEffect == -1) {
      revealedCardEffect = 0
    }
    this
  }
  def enemy3() : Game = {
    if (revealedCardEffect != - 1) {
      init.enemy3 = init.enemy3.enemy(this)
    } else if (revealedCardEffect == -1) {
      revealedCardEffect = 0
    }
    this
  }

  def getLength(list:Integer) : Int = {
    if list == 0 then
      init.enemy.enemyCards.length
    else if list == 1 then
      init.enemy2.enemyCards.length
    else if list == 2 then
      init.enemy3.enemyCards.length
    else if list == 3 then
      init.cardsRevealed.length
    else if list == 4 then
      init.player.handCards.length
    else
      init.cardsCovered.length
  }
  def getCardText(list : Int, index : Int) : String = {
    if (list == 3 && index == 1) {
      init.cardsRevealed.head.toString
    } else if (list == 3 && index == 2) {
      "Do Step"
    } else if (list == 4) {
      init.player.handCards(index).toString
    } else {
      "Uno"
    }
  }
  def getGuiCardText(list : Int, index : Int) : String = {
    if (list == 3 && index == 1) {
      init.cardsRevealed.head.toGuiString
    } else if (list == 3 && index == 2) {
      "Do Step"
    } else if (list == 4) {
      init.player.handCards(index).toGuiString
    } else {
      "Uno"
    }
  }
  def getNumOfPlayers : 2 | 3 | 4 = {
    numOfPlayers
  }

  def nextEnemy() : Int = {
    if (numOfPlayers == 2) { // ||(activePlayer == 0 && direction) ||(numOfPlayers == 3 && activePlayer == 2)||(numOfPlayers == 4 && activePlayer == 2 && direction)
      1
    } else if ((numOfPlayers == 3)) {
      if (activePlayer == 0) {
        if (direction) {
          1
        } else {
          2
        }
      } else if (activePlayer == 1) {
        2
      } else {
        1
      }
    } else {
      if (activePlayer == 0) {
        if (direction) {
          1
        } else {
          3
        }
      } else if (activePlayer == 1) {
        2
      } else if (activePlayer == 2) {
        if (direction) {
          3
        } else {
          1
        }
      } else {
        2
      }
    }
  }
  def nextTurn() : Boolean = {
    if ((activePlayer == 1 && (!direction || numOfPlayers == 2)) ||
      (activePlayer == 2 && direction && numOfPlayers == 3) || (activePlayer == 3 && direction && numOfPlayers== 4)) {
      true
    } else {
      false
    }
  }
  def getNextEnemy() : Enemy = {
    val i = nextEnemy()
    if (i == 1) {
      init.enemy
    } else if (i == 2) {
      init.enemy2
    } else {
      init.enemy3
    }
  }

  def setActivePlayer() : Game = {
    if (nextTurn()) {
      activePlayer = 0
    } else {
      activePlayer = nextEnemy()
    }
    this
  }
  def setDirection() : Game = {
    direction = !direction
    this
  }
  def setAnotherPull(b : Boolean = false) : Game = {
    alreadyPulled = b
    this
  }

  def getActivePlayer: Int = activePlayer
  def getDirection: Boolean = direction
  def getAnotherPull : Boolean = alreadyPulled


  def getAllCards(list: Int, index: Int) : String = {
    if (list == 0)
      init.enemy.enemyCards(index).toString
    else if (list == 1)
      init.enemy2.enemyCards(index).toString
    else if (list == 2)
      init.enemy3.enemyCards(index).toString
    else if (list == 3)
      init.cardsRevealed(index).toString
    else if (list == 4)
      init.player.handCards(index).toString
    else
      init.cardsCovered(index).toString
  }

  def setAllCards(list: Int, card: Card) : Game = {
    if (list == 0)
      init.enemy = init.enemy.pushCard(card)
    else if (list == 1)
      init.enemy2 = init.enemy2.pushCard(card)
    else if (list == 2)
      init.enemy3 = init.enemy3.pushCard(card)
    else if (list == 3)
      init.cardsRevealed = init.cardsRevealed :+ card
    else if (list == 4)
    init.player = init.player.pushCard(card)
    else
      init.cardsCovered = init.cardsCovered :+ card
    this
  }

  def clearAllLists() : Game = {
    init.enemy = Enemy(List[Card]())
    init.enemy2 = Enemy(List[Card]())
    init.enemy3 = Enemy(List[Card]())
    init.player = Player(List[Card]())
    init.cardsCovered = new ListBuffer[Card]()
    init.cardsRevealed = new ListBuffer[Card]()
    revealedCardEffect = 0
    this
  }

  def getSpecialTop : Int = {
    revealedCardEffect
  }

  def setRevealedCardEffect(io : Int) : Game = {
    revealedCardEffect = io
    this
  }

  def shuffle() : Game = {
    var cards = init.cardsCovered ++ init.cardsRevealed.drop(1)
    var n = cards.length
    for (_ <- cards.indices) {
      val r = new scala.util.Random
      val p = 1 + r.nextInt(n)
      init.cardsCovered = cards(p - 1) +: init.cardsCovered
      cards = cards.take(p - 1) ++ cards.drop(p)
      n -= 1
    }
    init.cardsRevealed = init.cardsRevealed.take(1)
    this
  }

  override def toString: String = {
    val a = "┌-------┐  "
    val b = "|       |  "
    val c = "|  Uno  |  "
    val d = "└-------┘  "
    var e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x = ""

    for (_ <- 1 to init.enemy.enemyCards.length) {
      e = e.concat(a)
      f = f.concat(b)
      g = g.concat(c)
      h = h.concat(d)
    }
    for (i <- 1 to init.player.handCards.length) {
      m = m.concat(a)
      n = n.concat(b)
      o = o.concat("|  " + init.player.handCards(i-1).toString + "  |  ")
      p = p.concat(d)
    }
    i = i.concat(a).concat("           ┌-------┐") + "\n"
    j = j.concat(b).concat("           |       |") + "\n"
    k = k.concat(c).concat("           |  " + init.cardsRevealed.head.toString + "  |") + "\n"
    l = l.concat(d).concat("           └-------┘") + "\n\n"


    if (numOfPlayers >= 3) {
      for (_ <- 1 to init.enemy2.enemyCards.length) {
        q = q.concat(a)
        r = r.concat(b)
        s = s.concat(c)
        t = t.concat(d)
      }
      if (numOfPlayers == 4) {
        for (_ <- 1 to init.enemy3.enemyCards.length) {
          u = u.concat(a)
          v = v.concat(b)
          w = w.concat(c)
          x = x.concat(d)
        }
      }
    }

    val playingField = e + "\t\t\t\t\t" + q + "\n" +
                        f + "\t\t\t\t\t" + r + "\n" +
                        g + "\t\t\t\t\t" + s + "\n" +
                        f + "\t\t\t\t\t" + r + "\n" +
                        h + "\t\t\t\t\t" + t + "\n\n" +
                        i + j + k + j + l +
                        m + "\t\t\t\t\t" + u + "\n" +
                        n + "\t\t\t\t\t" + v + "\n" +
                        o + "\t\t\t\t\t" + w + "\n" +
                        n + "\t\t\t\t\t" + v + "\n" +
                        p + "\t\t\t\t\t" + x
    playingField
  }
}
