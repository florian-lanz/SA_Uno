package de.htwg.se.uno.model.gameComponent.gameBaseImpl

import scala.annotation.tailrec
import scala.util.Random

case class CardStack(cardStack: List[Card] = List()):
  def createCoveredCardStack(
      amountColorCards: Int = 2,
      amountSpecialCards: Int = 4
  ): CardStack =
    @tailrec
    def cardStackRecursion(
        cardStack: List[Card],
        colorIndex: Int
    ): List[Card] = {

      def cardStackRecursion2(
          cardStack: List[Card],
          color: Color,
          valueIndex: Int
      ): List[Card] = {

        if valueIndex < Value.values.length then
          val value = Value.fromOrdinal(valueIndex)
          val newCardStack =
            if (value == Value.PlusFour || value == Value.ColorChange) && color == Color.Special
            then
              cardStack ::: List.fill(amountSpecialCards)(
                Card(color, value)
              )
            else if value == Value.Zero && color != Color.Special then
              cardStack ::: List(
                Card(color, value)
              )
            else if value != Value.PlusFour && value != Value.ColorChange && color != Color.Special
            then
              cardStack ::: List
                .fill(amountColorCards)(
                  Card(color, value)
                )
            else cardStack
          cardStackRecursion2(newCardStack, color, valueIndex + 1)
        else cardStack
      }

      if colorIndex < Color.values.length then
        val color = Color.fromOrdinal(colorIndex)
        cardStackRecursion(
          cardStackRecursion2(cardStack, color, 0),
          colorIndex + 1
        )
      else cardStack
    }
    val cardStack = cardStackRecursion(List(), 0)
    copy(cardStack)

  def shuffle(): CardStack = copy(Random.shuffle(cardStack))

  def addCard(card: Card): CardStack = copy(card :: cardStack)

  def removeCard(): CardStack = copy(cardStack.tail)

  def getTopCard: Card = cardStack.head
