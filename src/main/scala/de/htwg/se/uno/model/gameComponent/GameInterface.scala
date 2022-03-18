package de.htwg.se.uno.model.gameComponent

import de.htwg.se.uno.model.gameComponent.gameBaseImpl.{Card, Game}

trait GameInterface {
  def toString: String
  def createTestGame(): Game
  def enemy(): Game
  def enemyUndo(): Game
  def pullMove(): Game
  def playerUndo(): Game
  def pushMove(string: String, color: Int): Game
  def getLength(list: Integer): Int
  def getCardText(list: Int, index: Int): String
  def getGuiCardText(list: Int, index: Int): String
  def getNumOfPlayers(): Int
  def createGame(): Game
  def enemyUndo2(): Game
  def enemyUndo3(): Game
  def enemy2(): Game
  def enemy3(): Game
  def nextTurn(): Boolean
  def nextEnemy(): Int
  def setActivePlayer(): Game
  def setDirection(): Game
  def getActivePlayer(): Int
  def getDirection(): Boolean
  def getAnotherPull(): Boolean
  def setAnotherPull(b: Boolean = false): Game
  def getRedoVariable(): Boolean
  def setRedoVariable(b: Boolean = true): Game
  def getUndoVariable(): Boolean
  def setLength(i: Integer): Unit
  def getAllCards(list: Int, index: Int): String
  def setAllCards(list: Int, card: Card): Game
  def clearAllLists(): Game
  def getSpecialTop(): Int
  def setSpecialTop(io: Int): Game
  def shuffle(): Game
  def unshuffle(): Game
  def reshuffle(): Game
}
