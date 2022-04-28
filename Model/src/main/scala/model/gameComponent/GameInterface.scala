package model.gameComponent

import model.gameComponent.gameBaseImpl.*

trait GameInterface( val numOfPlayers: 2 | 3 | 4,
                     val coveredCards: List[Card],
                     val revealedCards: List[Card],
                     val player: Player,
                     val enemies: List[Enemy],
                     val revealedCardEffect: Int,
                     val activePlayer: Int,
                     val direction: Boolean,
                     val alreadyPulled: Boolean):
  def toString: String
  def enemy(enemyIndex: Int, kiNeeded: Boolean = true): Game
  def pullMove(): Game
  def pushMove(string: String, color: Int): Game
  def createGame(gameSize: 2 | 3 | 4): Game
  def nextTurn(): Boolean
  def nextEnemy(): Int
  def changeActivePlayer(): Game
  def shuffle(): Game