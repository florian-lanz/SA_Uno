package de.htwg.se.uno.model.gameComponent.gameBaseImpl

import scala.collection.mutable.ListBuffer

class InitializeTestGameStrategy extends InitializeGameStrategy {
  override def initializeGame(numOfPlayers: Int): InitializeTestGameStrategy = {
    cardsCovered = new ListBuffer[Card]()
    cardsRevealed = new ListBuffer[Card]()
    enemy.enemyCards = new ListBuffer[Card]()
    player.handCards = new ListBuffer[Card]()
    enemy2.enemyCards = new ListBuffer[Card]()
    enemy3.enemyCards = new ListBuffer[Card]()
    var cards = new ListBuffer[Card]()
    for (color <- Color.values) {
      for (value <- Value.values) {
        if (value == Value.Zero && color != Color.Special) {
          cards += Card(color, value)
        } else if (color == Color.Special && (value == Value.ColorChange || value == Value.PlusFour)) {
          for (_ <- 0 to 3)
            cards += Card(color, value)
        } else if (color != Color.Special && (value != Value.PlusFour && value != Value.ColorChange)) {
          for (_ <- 0 to 1)
            cards += Card(color, value)
        }
      }
    }
    for (_ <- 1 to 108) {
      cardsCovered = cardsCovered :+ cards(0)
      cards = cards.take(0) ++ cards.drop(1)
    }

    cardsRevealed = cardsRevealed :+ cardsCovered(0)
    cardsCovered = cardsCovered.drop(1)


    player.handCards = player.handCards :+ cardsCovered(99)
    cardsCovered = cardsCovered.take(99) ++ cardsCovered.drop(100)
    enemy.enemyCards = enemy.enemyCards :+ cardsCovered(99)
    cardsCovered = cardsCovered.take(99) ++ cardsCovered.drop(100)
    enemy2.enemyCards = enemy2.enemyCards :+ cardsCovered(99)
    cardsCovered = cardsCovered.take(99) ++ cardsCovered.drop(100)
    enemy3.enemyCards = enemy3.enemyCards :+ cardsCovered(99)
    cardsCovered = cardsCovered.take(99) ++ cardsCovered.drop(100)

    player.handCards = player.handCards :+ cardsCovered(99)
    cardsCovered = cardsCovered.take(99) ++ cardsCovered.drop(100)
    enemy.enemyCards = enemy.enemyCards :+ cardsCovered(99)
    cardsCovered = cardsCovered.take(99) ++ cardsCovered.drop(100)
    enemy2.enemyCards = enemy2.enemyCards :+ cardsCovered(99)
    cardsCovered = cardsCovered.take(99) ++ cardsCovered.drop(100)
    enemy3.enemyCards = enemy3.enemyCards :+ cardsCovered(99)
    cardsCovered = cardsCovered.take(99)

    player.handCards = player.handCards :+ cardsCovered(0)
    cardsCovered = cardsCovered.drop(1)
    enemy.enemyCards = enemy.enemyCards :+ cardsCovered(0)
    cardsCovered = cardsCovered.drop(1)
    enemy2.enemyCards = enemy2.enemyCards :+ cardsCovered(0)
    cardsCovered = cardsCovered.drop(1)
    enemy3.enemyCards = enemy3.enemyCards :+ cardsCovered(0)
    cardsCovered = cardsCovered.drop(1)

    player.handCards = player.handCards :+ cardsCovered(0)
    cardsCovered = cardsCovered.drop(1)
    enemy.enemyCards = enemy.enemyCards :+ cardsCovered(0)
    cardsCovered = cardsCovered.drop(1)
    enemy2.enemyCards = enemy2.enemyCards :+ cardsCovered(0)
    cardsCovered = cardsCovered.drop(1)
    enemy3.enemyCards = enemy3.enemyCards :+ cardsCovered(0)
    cardsCovered = cardsCovered.drop(1)

    player.handCards = player.handCards :+ cardsCovered(23)
    cardsCovered = cardsCovered.take(23) ++ cardsCovered.drop(24)
    enemy.enemyCards = enemy.enemyCards :+ cardsCovered(23)
    cardsCovered = cardsCovered.take(23) ++ cardsCovered.drop(24)
    enemy2.enemyCards = enemy2.enemyCards :+ cardsCovered(23)
    cardsCovered = cardsCovered.take(23) ++ cardsCovered.drop(24)
    enemy3.enemyCards = enemy3.enemyCards :+ cardsCovered(23)
    cardsCovered = cardsCovered.take(23) ++ cardsCovered.drop(24)

    player.handCards = player.handCards :+ cardsCovered(48)
    cardsCovered = cardsCovered.take(48) ++ cardsCovered.drop(49)
    enemy.enemyCards = enemy.enemyCards :+ cardsCovered(48)
    cardsCovered = cardsCovered.take(48) ++ cardsCovered.drop(49)
    enemy2.enemyCards = enemy2.enemyCards :+ cardsCovered(48)
    cardsCovered = cardsCovered.take(48) ++ cardsCovered.drop(49)
    enemy3.enemyCards = enemy3.enemyCards :+ cardsCovered(48)
    cardsCovered = cardsCovered.take(48) ++ cardsCovered.drop(49)

    player.handCards = player.handCards :+ cardsCovered(10)
    cardsCovered = cardsCovered.take(10) ++ cardsCovered.drop(11)
    enemy.enemyCards = enemy.enemyCards :+ cardsCovered(10)
    cardsCovered = cardsCovered.take(10) ++ cardsCovered.drop(11)
    enemy2.enemyCards = enemy2.enemyCards :+ cardsCovered(29)
    cardsCovered = cardsCovered.take(29) ++ cardsCovered.drop(30)
    enemy3.enemyCards = enemy3.enemyCards :+ cardsCovered(29)
    cardsCovered = cardsCovered.take(29) ++ cardsCovered.drop(30)

    player.handCards = player.handCards :+ cardsCovered(10)
    cardsCovered = cardsCovered.take(10) ++ cardsCovered.drop(11)
    enemy.enemyCards = enemy.enemyCards :+ cardsCovered(10)
    cardsCovered = cardsCovered.take(10) ++ cardsCovered.drop(11)
    enemy2.enemyCards = enemy2.enemyCards :+ cardsCovered(27)
    cardsCovered = cardsCovered.take(27) ++ cardsCovered.drop(28)
    enemy3.enemyCards = enemy3.enemyCards :+ cardsCovered(27)
    cardsCovered = cardsCovered.take(27) ++ cardsCovered.drop(28)

    player.handCards = player.handCards :+ cardsCovered(10)
    cardsCovered = cardsCovered.take(10) ++ cardsCovered.drop(11)
    enemy.enemyCards = enemy.enemyCards :+ cardsCovered(10)
    cardsCovered = cardsCovered.take(10) ++ cardsCovered.drop(11)
    enemy2.enemyCards = enemy2.enemyCards :+ cardsCovered(25)
    cardsCovered = cardsCovered.take(25) ++ cardsCovered.drop(26)
    enemy3.enemyCards = enemy3.enemyCards :+ cardsCovered(25)
    cardsCovered = cardsCovered.take(25) ++ cardsCovered.drop(26)

    player.pulledCardsStack.popAll()
    player.pulledCardsStack.push("Start")
    player.pushedCardIndexStack.popAll()
    player.pushedCardIndexStack.push(-1)
    player.pushedCardsStack.popAll()
    player.anotherPullStack.popAll()
    enemy.pulledCardsStack.popAll()
    enemy.pulledCardsStack.push("Start")
    enemy.pushedCardIndexStack.popAll()
    enemy.pushedCardIndexStack.push(-1)
    enemy.pushedCardsStack.popAll()
    enemy.anotherPullStack.popAll()
    enemy2.pulledCardsStack.popAll()
    enemy2.pulledCardsStack.push("Start")
    enemy2.pushedCardIndexStack.popAll()
    enemy2.pushedCardIndexStack.push(-1)
    enemy2.pushedCardsStack.popAll()
    enemy2.anotherPullStack.popAll()
    enemy3.pulledCardsStack.popAll()
    enemy3.pulledCardsStack.push("Start")
    enemy3.pushedCardIndexStack.popAll()
    enemy3.pushedCardIndexStack.push(-1)
    enemy3.pushedCardsStack.popAll()
    enemy3.anotherPullStack.popAll()

    this
  }
}
