package model.gameComponent.gameBaseImpl

import scala.annotation.tailrec

case class Player(handCards: List[Card] = List()):
  def canPush(card: Card, revealedCard: Card, revealedCardEffect: Int): Boolean =
    if revealedCard.value == Value.PlusTwo && card.value != Value.PlusTwo && revealedCardEffect > 0 then false
    else if revealedCard.value == Value.PlusFour && card.value != Value.PlusFour && revealedCardEffect > 0 then false
    else if card.value == Value.PlusFour then handCards.forall(card => !(card.color == revealedCard.color && revealedCard.color != Color.Special && revealedCard.value != Value.PlusFour))
    else if card.color == revealedCard.color || card.value == revealedCard.value || card.value == Value.ColorChange || revealedCard.color == Color.Special 
    then true
    else false

  def pushCard(card: Card): Player = copy(handCards diff List(card))

  def pullCard(card: Card): Player = copy(card :: handCards)

  def findCard(s: String): Option[Card] = handCards.find(card => card.toString.equals(s))