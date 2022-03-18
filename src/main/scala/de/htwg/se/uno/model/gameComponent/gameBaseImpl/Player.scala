package de.htwg.se.uno.model.gameComponent.gameBaseImpl

import scala.collection.mutable.{ListBuffer, Stack}

class Player() {
  var handCards = new ListBuffer[Card]()
  var pulledCardsStack = Stack[String]("Start")
  var pushedCardIndexStack = Stack[Integer](-1)
  var pushedCardsStack = Stack[Card]()
  var anotherPullStack = Stack[Boolean]()

  def pushMove(string:String, color : Int, game: Game) : Player = {
    if(equalsCard(string)) {
      val card = getCard(string)
      if (pushable(card, game)) {
        return pushCard(card, color, game)
      }
    }
    this
  }
  def pullMove(game:Game) : Player = {
    if (!game.anotherPull) {
      pull(game)
    } else {
      game.anotherPull = false
      game.special.push(0)
      pulledCardsStack.push("Suspend")
      pushedCardIndexStack.push(-1)
      anotherPullStack.push(false)
      this
    }
  }

  def undo(game: Game) : Player = {
    if (pulledCardsStack.top.equals(" ")) { //Wenn gelegt wurde
      handCards = handCards.take(pushedCardIndexStack.top) :+ pushedCardsStack.top :++ handCards.drop(pushedCardIndexStack.top)
      game.init.cardsRevealed = game.init.cardsRevealed.drop(1)
      if (pushedCardsStack.top.value == Value.DirectionChange) {
        game.setDirection()
      }
      pulledCardsStack.pop()
      pushedCardIndexStack.pop()
      pushedCardsStack.pop()
      game.special.pop()
      game.undoVariable = true
    } else {
      var c = 0
      if (equalsCard(pulledCardsStack.top)) {
        val card = getCard(pulledCardsStack.top)
        game.init.cardsCovered = card +: game.init.cardsCovered
        c = 0
        for (i <- 2 to handCards.length) {
          if (handCards(i - 2).color == card.color && handCards(i - 2).value == card.value && c == 0) {
            handCards = handCards.take(i - 2) ++ handCards.drop(i - 1)
            c = 1
          }
        }
        if (c == 0) {
          handCards = handCards.take(handCards.length - 1)
        }
      }
      pulledCardsStack.pop()
      pushedCardIndexStack.pop()
      game.undoVariable = true
      if(anotherPullStack.top) {
        game.undoVariable = false
        game.anotherPull = false
      }
      anotherPullStack.pop()

      game.special.pop()
      if (game.special.top > 0) {
        for (_ <- 2 to game.special.top) {
          if (equalsCard(pulledCardsStack.top)) {
            val card = getCard(pulledCardsStack.top)
            game.init.cardsCovered = card +: game.init.cardsCovered
            c = 0
            for (i <- 2 to handCards.length) {
              if (handCards(i - 2).color == card.color && handCards(i - 2).value == card.value && c == 0) {
                handCards = handCards.take(i - 2) ++ handCards.drop(i - 1)
                c = 1
              }
            }
            if (c == 0) {
              handCards = handCards.take(handCards.length - 1)
            }
            pulledCardsStack.pop()
            pushedCardIndexStack.pop()
          }
        }
      }
    }
    this
  }

  def pushable(card: Card, game: Game) : Boolean = {
    if (game.init.cardsRevealed.head.value == Value.PlusTwo && card.value != Value.PlusTwo && game.special.top > 0) {
      return false
    } else if (game.init.cardsRevealed.head.value == Value.PlusFour && card.value != Value.PlusFour && game.special.top > 0) {
      return false
    } else if (card.value == Value.PlusFour) {
      for (i <- 1 to handCards.length) {
        if (handCards(i - 1).color == game.init.cardsRevealed.head.color &&
          game.init.cardsRevealed.head.color != Color.Special && game.init.cardsRevealed.head.value != Value.PlusFour) {
          return false
        }
      }
      return true
    } else if (card.color == game.init.cardsRevealed.head.color || card.value == game.init.cardsRevealed.head.value
      || card.value == Value.ColorChange || game.init.cardsRevealed.head.color == Color.Special) {
      return true
    }
    false
  }
  def pushCard(card: Card, color : Int, game: Game) : Player = {
    var c = 0
    var myCard = card
    if(color == 1) {
      myCard = Card(Color.Blue, card.value)
    } else if (color == 2) {
      myCard = Card(Color.Green, card.value)
    } else if (color == 3) {
      myCard = Card(Color.Yellow, card.value)
    } else if (color == 4) {
      myCard = Card(Color.Red, card.value)
    }
    pushedCardsStack.push(card)
    for (i <- 2 to handCards.length) {
      if (handCards(i - 2).color == card.color && handCards(i - 2).value == card.value && c == 0) {
        game.init.cardsRevealed = myCard +: game.init.cardsRevealed
        handCards = handCards.take(i - 2) ++ handCards.drop(i-1)
        pushedCardIndexStack.push(i-2)
        c += 1
        pulledCardsStack.push(" ")
      }
    }
    if (c == 0) {
      game.init.cardsRevealed = myCard +: game.init.cardsRevealed
      handCards = handCards.take(handCards.length - 1)
      pushedCardIndexStack.push(handCards.length-1)
      pulledCardsStack.push(" ")
    }
    if (pushedCardsStack.top.value == Value.DirectionChange) {
      game.setDirection()
      game.special.push(0)
    } else if (pushedCardsStack.top.value == Value.PlusTwo) {
      game.special.push(game.special.top + 2)
    } else if (pushedCardsStack.top.value == Value.PlusFour) {
      game.special.push(game.special.top + 4)
    } else if (pushedCardsStack.top.value == Value.Suspend) {
      game.special.push(-1)
    } else {
      game.special.push(0)
    }
    game.anotherPull = false
    this
  }
  def pull(game: Game) : Player = {
    pulledCardsStack.push(game.init.cardsCovered.head.toString)
    pushedCardIndexStack.push(-1)
    handCards += Card(game.init.cardsCovered.head.color, game.init.cardsCovered.head.value)
    game.init.cardsCovered = game.init.cardsCovered.drop(1)
    if (game.special.top > 0) {
      game.anotherPull = false
      anotherPullStack.push(false)
      for (_ <- 2 to game.special.top) {
        pulledCardsStack.push(game.init.cardsCovered.head.toString)
        pushedCardIndexStack.push(-1)
        handCards += Card(game.init.cardsCovered.head.color, game.init.cardsCovered.head.value)
        game.init.cardsCovered = game.init.cardsCovered.drop(1)
      }
    } else {
      game.anotherPull = true
      game.redoVariable = true
      anotherPullStack.push(true)
    }
    game.special.push(0)
    this
  }

  def equalsCard(s: String): Boolean = {
    for (i <- 1 to handCards.length) {
      if (s.equals(handCards(i - 1).toString)) {
        return true
      }
    }
    false
  }
  def getCard(s: String): Card = {
    var c = 0
    for (i <- 1 to handCards.length) {
      if (s.equals(handCards(i - 1).toString)) {
        c = i - 1
      }
    }
    handCards(c)
  }
}
