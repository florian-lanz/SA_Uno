package de.htwg.se.uno.model.gameComponent.gameBaseImpl

import scala.annotation.tailrec

case class Player(handCards: List[Card] = List()):
  def canPush(card: Card, revealedCard: Card, revealedCardEffect: Int): Boolean =
    if revealedCard.value == Value.PlusTwo && card.value != Value.PlusTwo && revealedCardEffect > 0 then false
    else if revealedCard.value == Value.PlusFour && card.value != Value.PlusFour && revealedCardEffect > 0 then false
    else if card.value == Value.PlusFour then
      @tailrec
      def canPushRecursion(i: Int): Boolean =
        if i < handCards.length then
          if handCards(i).color == revealedCard.color && revealedCard.color != Color.Special && revealedCard.value != Value.PlusFour 
          then false
          else canPushRecursion(i + 1)
        else true
      canPushRecursion(0)
    else if card.color == revealedCard.color || card.value == revealedCard.value || card.value == Value.ColorChange || revealedCard.color == Color.Special 
    then true
    else false

  def pushCard(card: Card): Player =
    @tailrec
    def pushCardRecursion(i: Int): Int =
      if i < handCards.length then
        if handCards(i).color == card.color && handCards(i).value == card.value then i
        else pushCardRecursion(i + 1)
      else -1
    val index = pushCardRecursion(0)
    if index != -1 then copy(handCards.take(index) ++ handCards.drop(index + 1)) else copy()

  def pullCard(card: Card): Player = copy(card :: handCards)

  def getCard(s: String): Option[Card] = 
    @tailrec
    def getCardRecursion(i: Int): Int = 
      if i < handCards.length then
        if s.equals(handCards(i).toString) then i
        else getCardRecursion(i + 1)
      else -1
    val index = getCardRecursion(0)
    if index != -1 then Some(handCards(index)) else None