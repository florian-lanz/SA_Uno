//package de.htwg.se.uno.model.gameComponent.gameBaseImpl
//
//class InitializeTestGameStrategy extends InitializeGameStrategy {
//  override def initializeGame(
//      numOfPlayers: 2 | 3 | 4
//  ): InitializeTestGameStrategy = {
//    cardsCovered = CardStack().createCoveredCardStack().cardStack
//
//    cardsRevealed = cardsRevealed :+ cardsCovered(0)
//    cardsCovered = cardsCovered.drop(1)
//
//    // player.handCards = player.handCards :+ cardsCovered(99)
//    // cardsCovered = cardsCovered.take(99) ++ cardsCovered.drop(100)
//    // enemy.enemyCards = enemy.enemyCards :+ cardsCovered(99)
//    // cardsCovered = cardsCovered.take(99) ++ cardsCovered.drop(100)
//    // enemy2.enemyCards = enemy2.enemyCards :+ cardsCovered(99)
//    // cardsCovered = cardsCovered.take(99) ++ cardsCovered.drop(100)
//    // enemy3.enemyCards = enemy3.enemyCards :+ cardsCovered(99)
//    // cardsCovered = cardsCovered.take(99) ++ cardsCovered.drop(100)
//
//    // player.handCards = player.handCards :+ cardsCovered(99)
//    // cardsCovered = cardsCovered.take(99) ++ cardsCovered.drop(100)
//    // enemy.enemyCards = enemy.enemyCards :+ cardsCovered(99)
//    // cardsCovered = cardsCovered.take(99) ++ cardsCovered.drop(100)
//    // enemy2.enemyCards = enemy2.enemyCards :+ cardsCovered(99)
//    // cardsCovered = cardsCovered.take(99) ++ cardsCovered.drop(100)
//    // enemy3.enemyCards = enemy3.enemyCards :+ cardsCovered(99)
//    // cardsCovered = cardsCovered.take(99)
//
//    // player.handCards = player.handCards :+ cardsCovered(0)
//    // cardsCovered = cardsCovered.drop(1)
//    // enemy.enemyCards = enemy.enemyCards :+ cardsCovered(0)
//    // cardsCovered = cardsCovered.drop(1)
//    // enemy2.enemyCards = enemy2.enemyCards :+ cardsCovered(0)
//    // cardsCovered = cardsCovered.drop(1)
//    // enemy3.enemyCards = enemy3.enemyCards :+ cardsCovered(0)
//    // cardsCovered = cardsCovered.drop(1)
//
//    // player.handCards = player.handCards :+ cardsCovered(0)
//    // cardsCovered = cardsCovered.drop(1)
//    // enemy.enemyCards = enemy.enemyCards :+ cardsCovered(0)
//    // cardsCovered = cardsCovered.drop(1)
//    // enemy2.enemyCards = enemy2.enemyCards :+ cardsCovered(0)
//    // cardsCovered = cardsCovered.drop(1)
//    // enemy3.enemyCards = enemy3.enemyCards :+ cardsCovered(0)
//    // cardsCovered = cardsCovered.drop(1)
//
//    // player.handCards = player.handCards :+ cardsCovered(23)
//    // cardsCovered = cardsCovered.take(23) ++ cardsCovered.drop(24)
//    // enemy.enemyCards = enemy.enemyCards :+ cardsCovered(23)
//    // cardsCovered = cardsCovered.take(23) ++ cardsCovered.drop(24)
//    // enemy2.enemyCards = enemy2.enemyCards :+ cardsCovered(23)
//    // cardsCovered = cardsCovered.take(23) ++ cardsCovered.drop(24)
//    // enemy3.enemyCards = enemy3.enemyCards :+ cardsCovered(23)
//    // cardsCovered = cardsCovered.take(23) ++ cardsCovered.drop(24)
//
//    // player.handCards = player.handCards :+ cardsCovered(48)
//    // cardsCovered = cardsCovered.take(48) ++ cardsCovered.drop(49)
//    // enemy.enemyCards = enemy.enemyCards :+ cardsCovered(48)
//    // cardsCovered = cardsCovered.take(48) ++ cardsCovered.drop(49)
//    // enemy2.enemyCards = enemy2.enemyCards :+ cardsCovered(48)
//    // cardsCovered = cardsCovered.take(48) ++ cardsCovered.drop(49)
//    // enemy3.enemyCards = enemy3.enemyCards :+ cardsCovered(48)
//    // cardsCovered = cardsCovered.take(48) ++ cardsCovered.drop(49)
//
//    // player.handCards = player.handCards :+ cardsCovered(10)
//    // cardsCovered = cardsCovered.take(10) ++ cardsCovered.drop(11)
//    // enemy.enemyCards = enemy.enemyCards :+ cardsCovered(10)
//    // cardsCovered = cardsCovered.take(10) ++ cardsCovered.drop(11)
//    // enemy2.enemyCards = enemy2.enemyCards :+ cardsCovered(29)
//    // cardsCovered = cardsCovered.take(29) ++ cardsCovered.drop(30)
//    // enemy3.enemyCards = enemy3.enemyCards :+ cardsCovered(29)
//    // cardsCovered = cardsCovered.take(29) ++ cardsCovered.drop(30)
//
//    // player.handCards = player.handCards :+ cardsCovered(10)
//    // cardsCovered = cardsCovered.take(10) ++ cardsCovered.drop(11)
//    // enemy.enemyCards = enemy.enemyCards :+ cardsCovered(10)
//    // cardsCovered = cardsCovered.take(10) ++ cardsCovered.drop(11)
//    // enemy2.enemyCards = enemy2.enemyCards :+ cardsCovered(27)
//    // cardsCovered = cardsCovered.take(27) ++ cardsCovered.drop(28)
//    // enemy3.enemyCards = enemy3.enemyCards :+ cardsCovered(27)
//    // cardsCovered = cardsCovered.take(27) ++ cardsCovered.drop(28)
//
//    // player.handCards = player.handCards :+ cardsCovered(10)
//    // cardsCovered = cardsCovered.take(10) ++ cardsCovered.drop(11)
//    // enemy.enemyCards = enemy.enemyCards :+ cardsCovered(10)
//    // cardsCovered = cardsCovered.take(10) ++ cardsCovered.drop(11)
//    // enemy2.enemyCards = enemy2.enemyCards :+ cardsCovered(25)
//    // cardsCovered = cardsCovered.take(25) ++ cardsCovered.drop(26)
//    // enemy3.enemyCards = enemy3.enemyCards :+ cardsCovered(25)
//    // cardsCovered = cardsCovered.take(25) ++ cardsCovered.drop(26)
//
//    this
//  }
//}
