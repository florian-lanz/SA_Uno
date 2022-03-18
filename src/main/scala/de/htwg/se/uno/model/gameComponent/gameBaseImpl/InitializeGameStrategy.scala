package de.htwg.se.uno.model.gameComponent.gameBaseImpl

import scala.collection.mutable.ListBuffer

trait InitializeGameStrategy {
  var cardsCovered = new ListBuffer[Card]()
  var cardsRevealed = new ListBuffer[Card]()
  var player = new Player
  var enemy = new Enemy
  var enemy2 = new Enemy
  var enemy3 = new Enemy

  def initializeGame(numOfPlayers: Int): InitializeGameStrategy
}

object InitializeGameStrategy {
  def apply(kind: Int = 0): InitializeGameStrategy = kind match{
    case 0 => new InitializeRandomGameStrategy
    case 1 => new InitializeTestGameStrategy
  }
}
