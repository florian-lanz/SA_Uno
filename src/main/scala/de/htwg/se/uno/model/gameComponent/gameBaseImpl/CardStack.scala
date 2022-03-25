package de.htwg.se.uno.model.gameComponent.gameBaseImpl

import scala.annotation.tailrec
import scala.util.Random

case class CardStack(cardStack: List[Card]):
  def createCoveredCardStack(): CardStack =
    @tailrec
    def cardStackRecursion(cardStack: List[Card], index: Int): List[Card] =
      val value = Value.fromOrdinal(index)
      val newCardStack = if value == Value.PlusFour || value == Value.ColorChange then
        cardStack ::: List.fill(4)(Card(Color.Special, value))
      else
        cardStack ::: List.fill(2)(List(Card(Color.Red, value), Card(Color.Blue, value), Card(Color.Green, value),
          Card(Color.Yellow, value))).flatten
      if index < Value.values.length - 1 then
        cardStackRecursion(newCardStack, index + 1)
      else
        newCardStack
    val cardStack = cardStackRecursion(List(), 0)
    copy(cardStack)

  def shuffle(): CardStack = copy(Random.shuffle(cardStack))

  def addCard(card: Card): CardStack = copy(card :: cardStack)

  def removeCard(): CardStack = copy(cardStack.tail)

  def getTopCard: Card = cardStack.head