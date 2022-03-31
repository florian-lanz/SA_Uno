package de.htwg.se.uno.model.gameComponent.gameBaseImpl

import scala.annotation.tailrec
import scala.collection.mutable.{ListBuffer, Stack}
import scala.swing.Color

case class Enemy(enemyCards: List[Card] = List()) {

  def pushCard(card: Card): Enemy = {
    @tailrec
    def pushCardRecursion(i: Int): Int = {
      if i < enemyCards.length then
        if enemyCards(i).color == card.color && enemyCards(
            i
          ).value == card.value
        then i
        else pushCardRecursion(i + 1)
      else -1
    }
    val index = pushCardRecursion(0)

    if index != -1 then
      copy(enemyCards.take(index) ++ enemyCards.drop(index + 1))
    else copy()
  }

  def pullCard(card: Card): Enemy = {
    copy(card :: enemyCards)

//    enemyCards += Card(game.init.cardsCovered.head.color, game.init.cardsCovered.head.value)
//    game.init.cardsCovered = game.init.cardsCovered.drop(1)
//    if (game.special.top > 0) {
//      game.anotherPull = false
//      for (_ <- 2 to game.special.top) {
//        enemyCards += Card(game.init.cardsCovered.head.color, game.init.cardsCovered.head.value)
//        game.init.cardsCovered = game.init.cardsCovered.drop(1)
//      }
//    } else {
//      game.anotherPull = true
//      game.redoVariable = true
//    }
//    game.special.push(0)
//    this
  }

  def shouldPushDrawCard(
      card: Card,
      revealedCard: Card,
      revealedCardEffect: Int
  ): Boolean = {
    ((revealedCard.value == Value.PlusTwo && card.value == Value.PlusTwo) ||
      (revealedCard.value == Value.PlusFour && card.value == Value.PlusFour)) && revealedCardEffect > 0
  }
  def canPushBasicCardWithSameColor(
      card: Card,
      revealedCard: Card,
      revealedCardEffect: Int
  ): Boolean = {
    card.color == revealedCard.color && card.value != Value.Suspend &&
    card.value != Value.DirectionChange && card.value != Value.PlusTwo && card.color != Color.Special &&
    revealedCardEffect <= 0
  }
  def canPushSuspendOrDirectionChangeWithSameColor(
      card: Card,
      revealedCard: Card,
      revealedCardEffect: Int
  ): Boolean = {
    card.color == revealedCard.color && card.color != Color.Special &&
    revealedCardEffect <= 0 && card.value != Value.PlusTwo
  }
  def canPushBasicCardWithSameValue(
      card: Card,
      revealedCard: Card,
      revealedCardEffect: Int
  ): Boolean = {
    card.value == revealedCard.value && card.color != Color.Special &&
    card.value != Value.Suspend && card.value != Value.DirectionChange && card.value != Value.PlusTwo &&
    revealedCardEffect <= 0
  }
  def canPushSuspendOrDirectionChangeWithSameValue(
      card: Card,
      revealedCard: Card,
      revealedCardEffect: Int
  ): Boolean = {
    card.value == revealedCard.value && card.color != Color.Special &&
    revealedCardEffect <= 0 && card.value != Value.PlusTwo
  }
  def canPushColorChange(
      card: Card,
      revealedCard: Card,
      revealedCardEffect: Int
  ): Boolean = {
    card.value == Value.ColorChange && revealedCardEffect <= 0
  }
  def canPushOnSpecial(
      card: Card,
      revealedCard: Card,
      revealedCardEffect: Int
  ): Boolean = {
    revealedCard.color == Color.Special
  }
  def canPushPlusTwo(
      card: Card,
      revealedCard: Card,
      revealedCardEffect: Int
  ): Boolean = {
    card.value == Value.PlusTwo && (revealedCard.value == card.value ||
      revealedCard.color == card.color)
  }
  def canPushPlusFour(
      card: Card,
      revealedCard: Card,
      revealedCardEffect: Int
  ): Boolean = {
    if card.value == Value.PlusFour && !(revealedCard.value == Value.PlusTwo && revealedCardEffect > 0)
    then
      @tailrec
      def canPushPlusFourRecursion(i: Int): Boolean = {
        if i < enemyCards.length then
          if enemyCards(i).color == revealedCard.color &&
            revealedCard.color != Color.Special && revealedCard.value != Value.PlusFour
          then false
          else canPushPlusFourRecursion(i + 1)
        else true
      }
      canPushPlusFourRecursion(0)
    else false
  }
}
