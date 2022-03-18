package de.htwg.se.uno.model.gameComponent.gameBaseImpl

import scala.collection.mutable.ListBuffer

class InitializeRandomGameStrategy extends InitializeGameStrategy {
  override def initializeGame(numOfPlayers: 2 | 3 | 4): InitializeRandomGameStrategy = {
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
    var n = 108
    for (_ <- 0 to 107) {
      val r = new scala.util.Random
      val p = 1 + r.nextInt(n)
      cardsCovered = cards(p - 1) +: cardsCovered
      cards = cards.take(p - 1) ++ cards.drop(p)
      n -= 1
    }
    for (i <- 1 to 7) {
      player.handCards = cardsCovered.head +: player.handCards
      cardsCovered = cardsCovered.drop(1)
      enemy.enemyCards = cardsCovered.head +: enemy.enemyCards
      cardsCovered = cardsCovered.drop(1)
      if(numOfPlayers >= 3) {
        enemy2.enemyCards = cardsCovered.head +: enemy2.enemyCards
        cardsCovered = cardsCovered.drop(1)
        if (numOfPlayers == 4) {
          enemy3.enemyCards = cardsCovered.head +: enemy3.enemyCards
          cardsCovered = cardsCovered.drop(1)
        }
      }
    }
    cardsRevealed = cardsCovered(0) +: cardsRevealed
    cardsCovered = cardsCovered.drop(1)

    player.pulledCardsStack.popAll()
    player.pulledCardsStack.push("Start")
    player.pushedCardIndexStack.popAll()
    player.pushedCardIndexStack.push(-1)
    player.pushedCardsStack.popAll()
    player.pulledCardsStack.popAll()
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
