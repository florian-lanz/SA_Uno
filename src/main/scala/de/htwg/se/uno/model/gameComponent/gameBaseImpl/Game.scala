package de.htwg.se.uno.model.gameComponent.gameBaseImpl

import com.fasterxml.jackson.annotation.JsonValue
import com.google.inject.Inject
import com.google.inject.name.Named
import de.htwg.se.uno.model.gameComponent.GameInterface

import scala.collection.mutable
import scala.collection.mutable.{ListBuffer, Stack}

case class Game @Inject() (
    @Named("DefaultPlayers") numOfPlayers: 2 | 3 | 4,
    coveredCards: List[Card] = List(),
    revealedCards: List[Card] = List(),
    player: Player = Player(List()),
    enemies: List[Enemy] = List(),
    revealedCardEffect: Int = 0,
    activePlayer: Int = 0,
    direction: Boolean = true,
    alreadyPulled: Boolean = true
) extends GameInterface {

  def createGame(): Game = {
    val cards =
      CardStack()
        .createCoveredCardStack()
        .shuffle()
        .cardStack
    val revealedCards = List(cards.head)

    def initializePlayer(i: Int, handCards: List[Card]): List[Card] = {
      if i < 7 then initializePlayer(i + 1, cards(i + 1) :: handCards)
      else handCards
    }

    val player = Player(initializePlayer(0, List[Card]()))

    def initializeEnemies(
        enemyCounter: Int,
        i: Int,
        enemies: List[Card]
    ): List[Card] = {
      if enemyCounter < numOfPlayers - 1 then
        if i < 7 then
          initializeEnemies(
            enemyCounter,
            i + 1,
            cards((enemyCounter + 1) * 7 + 1 + i) :: enemies
          )
        else initializeEnemies(enemyCounter + 1, 0, enemies)
      else enemies
    }

    val enemiesCards = initializeEnemies(0, 0, List[Card]())
    val enemyOne = enemiesCards.take(7)
    val enemyTwo =
      if numOfPlayers < 3 then List()
      else if numOfPlayers == 3 then enemiesCards.drop(7)
      else enemiesCards.slice(7, 14)
    val enemyThree = if numOfPlayers < 4 then List() else enemiesCards.drop(14)

    val enemies = List(Enemy(enemyOne), Enemy(enemyTwo), Enemy(enemyThree))

    val coveredCards = cards.drop(numOfPlayers * 7 + 1)

    copy(
      numOfPlayers,
      coveredCards,
      revealedCards,
      player,
      enemies,
      0,
      numOfPlayers - 1,
      true,
      false
    )
  }

  def createTestGame(): Game = {
    val cards = CardStack().createCoveredCardStack().cardStack

    val revealedCards = List(cards.head)

    val player = Player(
      List(
        cards(100),
        cards(104),
        cards(1),
        cards(5),
        cards(32),
        cards(61),
        cards(19),
        cards(21),
        cards(23)
      )
    )

    val enemies = List(
      Enemy(
        List(
          cards(101),
          cards(105),
          cards(2),
          cards(6),
          cards(33),
          cards(62),
          cards(20),
          cards(22),
          cards(24)
        )
      ),
      Enemy(
        List(
          cards(102),
          cards(106),
          cards(3),
          cards(7),
          cards(34),
          cards(63),
          cards(44),
          cards(46),
          cards(48)
        )
      ),
      Enemy(
        List(
          cards(103),
          cards(107),
          cards(4),
          cards(8),
          cards(35),
          cards(64),
          cards(45),
          cards(47),
          cards(49)
        )
      )
    )

    val coveredCards =
      ((((cards filterNot revealedCards.contains) filterNot player.handCards.contains) filterNot enemies(
        0
      ).enemyCards.contains) filterNot enemies(
        1
      ).enemyCards.contains) filterNot enemies(2).enemyCards.contains

    copy(
      numOfPlayers,
      coveredCards,
      revealedCards,
      player,
      enemies,
      0,
      numOfPlayers - 1,
      true,
      false
    )
  }

  def pushMove(string: String, color: Int): Game = {
    val cardOption = player.getCard(string)
    if cardOption.isDefined then
      val card = cardOption.get
      if player.canPush(card, revealedCards.head, revealedCardEffect) then
        val playerNew = player.pushCard(card)
        val coloredCard = color match
          case 1 => Card(Color.Blue, card.value)
          case 2 => Card(Color.Green, card.value)
          case 3 => Card(Color.Yellow, card.value)
          case 4 => Card(Color.Red, card.value)
          case _ => card
        val revealedCardsNew = coloredCard :: revealedCards
        val alreadyPulledNew = false
        val directionNew =
          if card.value == Value.DirectionChange then !direction else direction
        val revealedCardEffectNew = card.value match
          case Value.Suspend  => -1
          case Value.PlusTwo  => revealedCardEffect + 2
          case Value.PlusFour => revealedCardEffect + 4
          case _              => 0
        copy(
          numOfPlayers,
          coveredCards,
          revealedCardsNew,
          playerNew,
          enemies,
          revealedCardEffectNew,
          activePlayer,
          directionNew,
          alreadyPulledNew
        )
      else copy()
    else copy()

    //    for (i <- 2 to handCards.length) {
//      if (handCards(i - 2).color == card.color && handCards(i - 2).value == card.value && !alreadyPushed) {
////        game.init.cardsRevealed = myCard +: game.init.cardsRevealed
//        handCards = handCards.take(i - 2) ++ handCards.drop(i-1)
//        alreadyPushed = true
//      }
//    }
//    if (!alreadyPushed) {
////      game.init.cardsRevealed = myCard +: game.init.cardsRevealed
//      handCards = handCards.take(handCards.length - 1)
//    }

//    if (card.value == Value.DirectionChange) {
//      game.setDirection()
//      game.special.push(0)
//    } else if (card.value == Value.PlusTwo) {
//      game.special.push(game.special.top + 2)
//    } else if (card.value == Value.PlusFour) {
//      game.special.push(game.special.top + 4)
//    } else if (card.value == Value.Suspend) {
//      game.special.push(-1)
//    } else {
//      game.special.push(0)
//    }
//    game.alreadyPulled = false

    // if (revealedCardEffect != -1) {
    //   init.player = init.player.pushMove(string, color, this)
    // } else if (revealedCardEffect == -1) {
    //   revealedCardEffect = 0
    //   setActivePlayer()
    // }
    this
  }
  def pullMove(): Game = {
    // if (revealedCardEffect != -1) {
    //   init.player = init.player.pullMove(this)
    // } else {
    //   revealedCardEffect = 0
    //   setActivePlayer()
    // }
    this
  }
  def enemy(): Game = {
    // if (revealedCardEffect != -1) {
    //   init.enemy = init.enemy.enemy(this)
    // } else if (revealedCardEffect == -1) {
    //   revealedCardEffect = 0
    // }
    this
  }
  def enemy2(): Game = {
    // if (revealedCardEffect != -1) {
    //   init.enemy2 = init.enemy2.enemy(this)
    // } else if (revealedCardEffect == -1) {
    //   revealedCardEffect = 0
    // }
    this
  }
  def enemy3(): Game = {
    // if (revealedCardEffect != -1) {
    //   init.enemy3 = init.enemy3.enemy(this)
    // } else if (revealedCardEffect == -1) {
    //   revealedCardEffect = 0
    // }
    this
  }

  def getLength(list: Integer): Int = {
    // if list == 0 then init.enemy.enemyCards.length
    // else if list == 1 then init.enemy2.enemyCards.length
    // else if list == 2 then init.enemy3.enemyCards.length
    // else if list == 3 then init.cardsRevealed.length
    // else if list == 4 then init.player.handCards.length
    // else init.cardsCovered.length
    0
  }
  def getCardText(list: Int, index: Int): String = {
    // if (list == 3 && index == 1) {
    //   init.cardsRevealed.head.toString
    // } else if (list == 3 && index == 2) {
    //   "Do Step"
    // } else if (list == 4) {
    //   init.player.handCards(index).toString
    // } else {
    //   "Uno"
    // }
    "Test"
  }
  def getGuiCardText(list: Int, index: Int): String = {
    // if (list == 3 && index == 1) {
    //   init.cardsRevealed.head.toGuiString
    // } else if (list == 3 && index == 2) {
    //   "Do Step"
    // } else if (list == 4) {
    //   init.player.handCards(index).toGuiString
    // } else {
    //   "Uno"
    // }
    "Test"
  }
  def getNumOfPlayers: 2 | 3 | 4 = {
    numOfPlayers
  }

  def nextEnemy(): Int = {
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
  def nextTurn(): Boolean = {
    if (
      (activePlayer == 1 && (!direction || numOfPlayers == 2)) ||
      (activePlayer == 2 && direction && numOfPlayers == 3) || (activePlayer == 3 && direction && numOfPlayers == 4)
    ) {
      true
    } else {
      false
    }
  }
  def getNextEnemy(): Enemy = {
    // val i = nextEnemy()
    // if (i == 1) {
    //   init.enemy
    // } else if (i == 2) {
    //   init.enemy2
    // } else {
    //   init.enemy3
    // }
    Enemy(List())
  }

  def setActivePlayer(): Game = {
    // if (nextTurn()) {
    //   activePlayer = 0
    // } else {
    //   activePlayer = nextEnemy()
    // }
    this
  }
  def setDirection(): Game = {
    // direction = !direction
    this
  }
  def setAnotherPull(b: Boolean = false): Game = {
    // alreadyPulled = b
    this
  }

  def getActivePlayer: Int = activePlayer
  def getDirection: Boolean = direction
  def getAnotherPull: Boolean = alreadyPulled

  def getAllCards(list: Int, index: Int): String = {
    // if (list == 0)
    //   init.enemy.enemyCards(index).toString
    // else if (list == 1)
    //   init.enemy2.enemyCards(index).toString
    // else if (list == 2)
    //   init.enemy3.enemyCards(index).toString
    // else if (list == 3)
    //   init.cardsRevealed(index).toString
    // else if (list == 4)
    //   init.player.handCards(index).toString
    // else
    //   init.cardsCovered(index).toString
    "Test"
  }

  def setAllCards(list: Int, card: Card): Game = {
    // if (list == 0)
    //   init.enemy = init.enemy.pushCard(card)
    // else if (list == 1)
    //   init.enemy2 = init.enemy2.pushCard(card)
    // else if (list == 2)
    //   init.enemy3 = init.enemy3.pushCard(card)
    // else if (list == 3)
    //   init.cardsRevealed = init.cardsRevealed :+ card
    // else if (list == 4)
    //   init.player = init.player.pushCard(card)
    // else
    //   init.cardsCovered = init.cardsCovered :+ card
    this
  }

  def clearAllLists(): Game = {
    // init.enemy = Enemy(List[Card]())
    // init.enemy2 = Enemy(List[Card]())
    // init.enemy3 = Enemy(List[Card]())
    // init.player = Player(List[Card]())
    // init.cardsCovered = new ListBuffer[Card]()
    // init.cardsRevealed = new ListBuffer[Card]()
    // revealedCardEffect = 0
    this
  }

  def getSpecialTop: Int = {
    revealedCardEffect
  }

  def setRevealedCardEffect(io: Int): Game = {
    // revealedCardEffect = io
    this
  }

  def shuffle(): Game = {
    // var cards = init.cardsCovered ++ init.cardsRevealed.drop(1)
    // var n = cards.length
    // for (_ <- cards.indices) {
    //   val r = new scala.util.Random
    //   val p = 1 + r.nextInt(n)
    //   init.cardsCovered = cards(p - 1) +: init.cardsCovered
    //   cards = cards.take(p - 1) ++ cards.drop(p)
    //   n -= 1
    // }
    // init.cardsRevealed = init.cardsRevealed.take(1)
    this
  }

  override def toString: String = {
    //   val a = "┌-------┐  "
    //   val b = "|       |  "
    //   val c = "|  Uno  |  "
    //   val d = "└-------┘  "
    //   var e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x = ""

    //   for (_ <- 1 to init.enemy.enemyCards.length) {
    //     e = e.concat(a)
    //     f = f.concat(b)
    //     g = g.concat(c)
    //     h = h.concat(d)
    //   }
    //   for (i <- 1 to init.player.handCards.length) {
    //     m = m.concat(a)
    //     n = n.concat(b)
    //     o = o.concat("|  " + init.player.handCards(i - 1).toString + "  |  ")
    //     p = p.concat(d)
    //   }
    //   i = i.concat(a).concat("           ┌-------┐") + "\n"
    //   j = j.concat(b).concat("           |       |") + "\n"
    //   k = k
    //     .concat(c)
    //     .concat(
    //       "           |  " + init.cardsRevealed.head.toString + "  |"
    //     ) + "\n"
    //   l = l.concat(d).concat("           └-------┘") + "\n\n"

    //   if (numOfPlayers >= 3) {
    //     for (_ <- 1 to init.enemy2.enemyCards.length) {
    //       q = q.concat(a)
    //       r = r.concat(b)
    //       s = s.concat(c)
    //       t = t.concat(d)
    //     }
    //     if (numOfPlayers == 4) {
    //       for (_ <- 1 to init.enemy3.enemyCards.length) {
    //         u = u.concat(a)
    //         v = v.concat(b)
    //         w = w.concat(c)
    //         x = x.concat(d)
    //       }
    //     }
    //   }

    //   val playingField = e + "\t\t\t\t\t" + q + "\n" +
    //     f + "\t\t\t\t\t" + r + "\n" +
    //     g + "\t\t\t\t\t" + s + "\n" +
    //     f + "\t\t\t\t\t" + r + "\n" +
    //     h + "\t\t\t\t\t" + t + "\n\n" +
    //     i + j + k + j + l +
    //     m + "\t\t\t\t\t" + u + "\n" +
    //     n + "\t\t\t\t\t" + v + "\n" +
    //     o + "\t\t\t\t\t" + w + "\n" +
    //     n + "\t\t\t\t\t" + v + "\n" +
    //     p + "\t\t\t\t\t" + x
    //   playingField
    "Test"
  }
}
