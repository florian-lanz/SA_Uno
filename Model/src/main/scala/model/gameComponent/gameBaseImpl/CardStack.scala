package model.gameComponent.gameBaseImpl

import scala.annotation.tailrec
import scala.util.Random

case class CardStack(cardStack: List[Card] = List()):
  def createCoveredCardStack(amountColorCards: Int = 2, amountSpecialCards: Int = 4): CardStack =
    val numberCards = (1 to 12).map(value => (0 to 3).map(color => List.fill(amountColorCards)(Card(Color.fromOrdinal(color), Value.fromOrdinal(value)))).toList.flatten).toList.flatten
    val zeroCards = (0 to 3).map(color => Card(Color.fromOrdinal(color), Value.Zero)).toList
    val specialCards = (13 to 14).map(value => List.fill(amountSpecialCards)(Card(Color.Special, Value.fromOrdinal(value)))).toList.flatten
    copy(zeroCards ::: numberCards ::: specialCards)

  def shuffle(): CardStack = copy(Random.shuffle(cardStack))
  
  def addColoredSpecialCards(): CardStack =
    copy(cardStack ::: (13 to 14).map(value => (0 to 3).map(color => List(Card(Color.fromOrdinal(color), Value.fromOrdinal(value)))).toList.flatten).toList.flatten)