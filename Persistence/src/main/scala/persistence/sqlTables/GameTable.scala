package persistence.sqlTables

import slick.jdbc.MySQLProfile.api._

class GameTable(tag: Tag) extends Table[(Int, Int, Int, Boolean, Boolean, Int, String, String, String, String, String, String)](tag, "GAME"):
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def numOfPlayers = column[Int]("NUM_OF_PLAYERS")
  def activePlayer = column[Int]("ACTIVE_PLAYER")
  def direction = column[Boolean]("DIRECTION")
  def anotherPull = column[Boolean]("ANOTHER_PULL")
  def specialCard = column[Int]("SPECIAL_CARD")
  def playerCards = column[String]("PLAYER_CARDS")
  def openCardStack = column[String]("OPEN_CARD_STACK")
  def coveredCardStack = column[String]("COVERED_CARD_STACK")
  def enemyCards1 = column[String]("ENEMY_ONE_CARDS")
  def enemyCards2 = column[String]("ENEMY_TWO_CARDS")
  def enemyCards3 = column[String]("ENEMY_THREE_CARDS")

  override def * = (id, numOfPlayers, activePlayer, direction, anotherPull, specialCard, playerCards, openCardStack, coveredCardStack, enemyCards1, enemyCards2, enemyCards3)