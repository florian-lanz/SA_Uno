package model.gameComponent

import model.gameComponent.gameBaseImpl._

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
  def createGame(): Game
  def nextTurn(): Boolean
  def nextEnemy(): Int
  def changeActivePlayer(): Game
  def shuffle(): Game
  def copyGame(numOfPlayers: 2 | 3 | 4 = numOfPlayers, coveredCards: List[Card] = coveredCards, revealedCards: List[Card] = revealedCards,
               player: Player = player, enemies: List[Enemy] = enemies, revealedCardEffect: Int = revealedCardEffect,
               activePlayer: Int = activePlayer, direction: Boolean = direction, alreadyPulled: Boolean = alreadyPulled): Game