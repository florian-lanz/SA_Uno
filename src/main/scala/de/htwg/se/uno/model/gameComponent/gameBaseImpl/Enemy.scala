package de.htwg.se.uno.model.gameComponent.gameBaseImpl

import scala.annotation.tailrec
import scala.collection.mutable.{ListBuffer, Stack}
import scala.swing.Color

case class Enemy(enemyCards: List[Card]) {
//  var enemyCards = new ListBuffer[Card]()

//  def enemy(game: Game) : Enemy = {
//    val s = game.toString
//    if(game.nextTurn()) {
//      if (game.init.player.handCards.length <= 3) {
//        ki(game)
//      }
//    } else if (game.getNextEnemy().enemyCards.length <= 3) {
//      ki(game)
//    }
//    if (!game.toString.equals(s))
//      return this
//
//
//    for (i <- 1 to enemyCards.length) {
//      if(pushable1(enemyCards(i-1), game)) {
//        return pushCardEnemy(enemyCards(i-1), game)
//      }
//    }
//    for (i <- 1 to enemyCards.length) {
//      if(pushable2(enemyCards(i-1), game)) {
//        return pushCardEnemy(enemyCards(i-1), game)
//      }
//    }
//    for (i <- 1 to enemyCards.length) {
//      if(pushable3(enemyCards(i-1), game)) {
//        return pushCardEnemy(enemyCards(i-1), game)
//      }
//    }
//    for (i <- 1 to enemyCards.length) {
//      if(pushable4(enemyCards(i-1), game)) {
//        return pushCardEnemy(enemyCards(i-1), game)
//      }
//    }
//    for (i <- 1 to enemyCards.length) {
//      if(pushable5(enemyCards(i-1), game)) {
//        return pushCardEnemy(enemyCards(i-1), game)
//      }
//    }
//    for (i <- 1 to enemyCards.length) {
//      if(pushable6(enemyCards(i-1), game)) {
//        return pushCardEnemy(enemyCards(i-1), game)
//      }
//    }
//    if (!game.anotherPull) {
//      if (enemyCards.length >= 4) {
//        return pullEnemy(game)
//      }
//      for (i <- 1 to enemyCards.length) {
//        if(pushable7(enemyCards(i-1), game)) {
//          return pushCardEnemy(enemyCards(i-1), game)
//        }
//      }
//      for (i <- 1 to enemyCards.length) {
//        if(pushable8(enemyCards(i-1), game)) {
//          return pushCardEnemy(enemyCards(i-1), game)
//        }
//      }
//      for (i <- 1 to enemyCards.length) {
//        if(pushable9(enemyCards(i-1), game)) {
//          return pushCardEnemy(enemyCards(i-1), game)
//        }
//      }
//      return pullEnemy(game)
//    }
//    game.anotherPull = false
//    game.special.push(0)
//    this
//  }

  def pushCard(card: Card): Enemy = {
//    var c = 0
//    var max = 0
//    var myCard = card
//    if (card.color == Color.Special) {
//      for (i <- 0 to 3) {
//        c = 0
//        for (j <- 0 to enemyCards.length - 1) {
//          if (i == 0) {
//            if (enemyCards(j).color == Color.Blue) {
//              c += 1
//            }
//          } else if (i == 1) {
//            if (enemyCards(j).color == Color.Green) {
//              c += 1
//            }
//          } else if (i == 2) {
//            if (enemyCards(j).color == Color.Yellow) {
//              c += 1
//            }
//          } else {
//            if (enemyCards(j).color == Color.Red) {
//              c += 1
//            }
//          }
//        }
//        if (c >= max) {
//          max = c
//          if (i == 0) {
//            myCard = Card(Color.Blue, card.value)
//          } else if (i == 1) {
//            myCard = Card(Color.Green, card.value)
//          } else if (i == 2) {
//            myCard = Card(Color.Yellow, card.value)
//          } else {
//            myCard = Card(Color.Red, card.value)
//          }
//        }
//      }
//    }

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

//    c = 0
//    for (i <- 2 to enemyCards.length) {
//      if (enemyCards(i - 2).color == card.color && enemyCards(i - 2).value == card.value && c == 0) {
////        game.init.cardsRevealed = myCard +: game.init.cardsRevealed
//        enemyCards = enemyCards.take(i - 2) ++ enemyCards.drop(i-1)
//        c += 1
//      }
//    }
//    if (c == 0) {
////      game.init.cardsRevealed = myCard +: game.init.cardsRevealed
//      enemyCards = enemyCards.take(enemyCards.length - 1)
//    }

//    if (card.value == Value.DirectionChange) {
//      game.setDirection()
//      game.special.push(0)
//    } else if (card.value == Value.PlusTwo) {
//      game.special.push(game.special.top + 2)
//    } else if (card.value == Value.PlusFour) {
//      game.special.push(game.special.top + 4)
//    } else if (card.value == Value.Suspend) {
//      game.special.push(-1)
//    } else {
//      game.special.push(0)
//    }
//    game.anotherPull = false
  }
  def pullEnemy(card: Card): Enemy = {
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

  def ki(revealedCard: Card, revealedCardEffect: Int): Enemy = {
    @tailrec
    def kiRecursion1(i: Int): Int = {
      if i < enemyCards.length then
        if canPushPlusFour(enemyCards(i), revealedCard, revealedCardEffect) then
          i
        else kiRecursion1(i + 1)
      else -1
    }
    @tailrec
    def kiRecursion2(i: Int): Int = {
      if i < enemyCards.length then
        if canPushPlusTwo(enemyCards(i), revealedCard, revealedCardEffect) then
          i
        else kiRecursion2(i + 1)
      else -1
    }
    @tailrec
    def kiRecursion3(i: Int): Int = {
      if i < enemyCards.length then
        if (enemyCards(i).value == Value.Suspend || enemyCards(
            i
          ).value == Value.DirectionChange) && revealedCardEffect <= 0 &&
          (enemyCards(i).value == revealedCard.value || enemyCards(
            i
          ).color == revealedCard.color)
        then i
        else kiRecursion3(i + 1)
      else -1
    }
    val index1 = kiRecursion1(0)
    val index =
      if index1 != -1 then index1
      else if kiRecursion2(0) != -1 then kiRecursion2(0)
      else kiRecursion3(0)
    if index != -1 then pushCard(enemyCards(index))
    else copy()

//    val index1 = kiRecursion1(0)
//    if index1 != -1 then
//      copy(pushCard(enemyCards(index1)))
//    else
//      @tailrec
//      def kiRecursion2(i: Int): Int = {
//        if i < enemyCards.length then
//          if canPushPlusTwo(enemyCards(i), revealedCard, revealedCardEffect) then
//            i
//          else
//            kiRecursion2(i + 1)
//        else
//          -1
//      }
//      val index2 = kiRecursion2(0)
//      if index2 != -1 then
//        copy(pushCard(enemyCards(index2)))
//      else
//        @tailrec
//        def kiRecursion3(i: Int): Int = {
//          if i < enemyCards.length then
//            if (enemyCards(i).value == Value.Suspend || enemyCards(i).value == Value.DirectionChange) && revealedCardEffect <= 0 &&
//            (enemyCards(i).value == revealedCard.value || enemyCards(i).color == revealedCard.color) then
//              i
//            else
//              kiRecursion3(i + 1)
//          else
//            -1
//        }
//        val index3 = kiRecursion3(0)
//        if index3 != -1 then
//          copy(pushCard(enemyCards(index3)))
//        else
//          copy()
  }
}
