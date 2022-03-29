package de.htwg.se.uno.model.gameComponent.gameBaseImpl

import scala.collection.mutable.ListBuffer

trait InitializeGameStrategy {
  var cardsCovered = new ListBuffer[Card]()
  var cardsRevealed = new ListBuffer[Card]()
  var player: Player = Player(List[Card]())
  var enemy: Enemy = Enemy(List[Card]())
  var enemy2: Enemy = Enemy(List[Card]())
  var enemy3: Enemy = Enemy(List[Card]())

  def initializeGame(numOfPlayers: 2 | 3 | 4): InitializeGameStrategy
}

object InitializeGameStrategy {
  def apply(kind: Int = 0): InitializeGameStrategy = kind match{
    case 0 => new InitializeRandomGameStrategy
    case 1 => new InitializeTestGameStrategy
  }
}
