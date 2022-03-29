package de.htwg.se.uno.model.gameComponent.gameBaseImpl

import scala.annotation.tailrec

case class Player(handCards: List[Card]) {
//  var handCards = new ListBuffer[Card]()

//  def pushMove(string: String, color: Int, game: Game): Player = {
//    val cardOption = getCard(string)
//    if cardOption.isDefined then
//      val card = cardOption.get
//      if pushable(card, game) then
//        pushCard(card)
//      else
//        copy()
//    else
//      copy()
//  }

//  def pullMove(game: Game): Player = {
//    if (!game.alreadyPulled) {
//      pull(game)
//    } else {
//      game.alreadyPulled = false
//      game.special.push(0)
//      this
//    }
//  }

  def canPush(card: Card, revealedCard: Card, revealedCardEffect: Int): Boolean = {
    if revealedCard.value == Value.PlusTwo && card.value != Value.PlusTwo && revealedCardEffect > 0 then
      false
    else if revealedCard.value == Value.PlusFour && card.value != Value.PlusFour && revealedCardEffect > 0 then
      false
    else if card.value == Value.PlusFour then
      @tailrec
      def canPushRecursion(i: Int): Boolean = {
        if i < handCards.length then
          if handCards(i).color == revealedCard.color && revealedCard.color != Color.Special && revealedCard.value != Value.PlusFour then
            false
          else
            canPushRecursion(i + 1)
        else
          true
      }
      canPushRecursion(0)
    else if card.color == revealedCard.color || card.value == revealedCard.value || card.value == Value.ColorChange ||
      revealedCard.color == Color.Special then
      true
    else
      false
  }

  def pushCard(card: Card): Player = {
    @tailrec
    def pushCardRecursion(i: Int): Int = {
      if i < handCards.length then
        if handCards(i).color == card.color && handCards(i).value == card.value then
          i
        else
          pushCardRecursion(i + 1)
      else
        -1
    }
    val index = pushCardRecursion(0)

    if index != -1 then
      copy(handCards.take(index) ++ handCards.drop(index + 1))
    else
      copy()

//    var alreadyPushed = false

//    for (i <- 2 to handCards.length) {
//      if (handCards(i - 2).color == card.color && handCards(i - 2).value == card.value && !alreadyPushed) {
////        game.init.cardsRevealed = myCard +: game.init.cardsRevealed
//        handCards = handCards.take(i - 2) ++ handCards.drop(i-1)
//        alreadyPushed = true
//      }
//    }
//    if (!alreadyPushed) {
////      game.init.cardsRevealed = myCard +: game.init.cardsRevealed
//      handCards = handCards.take(handCards.length - 1)
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
//    game.alreadyPulled = false
//    this
  }

  def pullCard(card: Card): Player = {
    copy(card :: handCards)

//    handCards += Card(game.init.cardsCovered.head.color, game.init.cardsCovered.head.value)
//    game.init.cardsCovered = game.init.cardsCovered.drop(1)
//    if (game.special.top > 0) {
//      game.alreadyPulled = false
//      for (_ <- 2 to game.special.top) {
//        handCards += Card(game.init.cardsCovered.head.color, game.init.cardsCovered.head.value)
//        game.init.cardsCovered = game.init.cardsCovered.drop(1)
//      }
//    } else {
//      game.alreadyPulled = true
//      game.redoVariable = true
//    }
//    game.special.push(0)
//    this
  }

  def getCard(s: String): Option[Card] = {
    @tailrec
    def getCardRecursion(i: Int): Int = {
      if i < handCards.length then
        if s.equals(handCards(i).toString) then
          i
        else
          getCardRecursion(i + 1)
      else
        -1
    }
    val index = getCardRecursion(0)
    if index != -1 then
      Some(handCards(index))
    else
      None
  }
}
