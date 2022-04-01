package de.htwg.se.uno.model.gameComponent

import de.htwg.se.uno.model.gameComponent.gameBaseImpl.{Card, Game}

trait GameInterface:
  def toString: String
  def createTestGame(): Game
  def enemy(enemyIndex: Int, kiNeeded: Boolean = true): Game
  def pullMove(): Game
  def pushMove(string: String, color: Int): Game
  def getLength(list: Integer): Int
  def getCardText(list: Int, index: Int): String
  def getGuiCardText(list: Int, index: Int): String
  def getNumOfPlayers: 2 | 3 | 4
  def createGame(): Game
  def nextTurn(): Boolean
  def nextEnemy(): Int
  def setActivePlayer(): Game
  def setDirection(): Game
  def getActivePlayer: Int
  def getDirection: Boolean
  def getAnotherPull: Boolean
  def setAnotherPull(b: Boolean = false): Game
  def getAllCards(list: Int, index: Int): String
  def setAllCards(list: Int, card: Card): Game
  def clearAllLists(): Game
  def getRevealedCardEffect: Int
  def shuffle(): Game
  def setRevealedCardEffect(io: Int): Game
  def reverseList(list: Int): Game