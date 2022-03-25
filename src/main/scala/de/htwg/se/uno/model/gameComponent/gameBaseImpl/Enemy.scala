package de.htwg.se.uno.model.gameComponent.gameBaseImpl

import scala.collection.mutable.{ListBuffer, Stack}
import scala.swing.Color

class Enemy() {
  var enemyCards = new ListBuffer[Card]()

  def enemy(game: Game) : Enemy = {
    val s = game.toString
    if(game.nextTurn()) {
      if (game.init.player.handCards.length <= 3) {
        ki(game)
      }
    } else if (game.getNextEnemy().enemyCards.length <= 3) {
      ki(game)
    }
    if (!game.toString.equals(s))
      return this


    for (i <- 1 to enemyCards.length) {
      if(pushable1(enemyCards(i-1), game)) {
        return pushCardEnemy(enemyCards(i-1), game)
      }
    }
    for (i <- 1 to enemyCards.length) {
      if(pushable2(enemyCards(i-1), game)) {
        return pushCardEnemy(enemyCards(i-1), game)
      }
    }
    for (i <- 1 to enemyCards.length) {
      if(pushable3(enemyCards(i-1), game)) {
        return pushCardEnemy(enemyCards(i-1), game)
      }
    }
    for (i <- 1 to enemyCards.length) {
      if(pushable4(enemyCards(i-1), game)) {
        return pushCardEnemy(enemyCards(i-1), game)
      }
    }
    for (i <- 1 to enemyCards.length) {
      if(pushable5(enemyCards(i-1), game)) {
        return pushCardEnemy(enemyCards(i-1), game)
      }
    }
    for (i <- 1 to enemyCards.length) {
      if(pushable6(enemyCards(i-1), game)) {
        return pushCardEnemy(enemyCards(i-1), game)
      }
    }
    if (!game.anotherPull) {
      if (enemyCards.length >= 4) {
        return pullEnemy(game)
      }
      for (i <- 1 to enemyCards.length) {
        if(pushable7(enemyCards(i-1), game)) {
          return pushCardEnemy(enemyCards(i-1), game)
        }
      }
      for (i <- 1 to enemyCards.length) {
        if(pushable8(enemyCards(i-1), game)) {
          return pushCardEnemy(enemyCards(i-1), game)
        }
      }
      for (i <- 1 to enemyCards.length) {
        if(pushable9(enemyCards(i-1), game)) {
          return pushCardEnemy(enemyCards(i-1), game)
        }
      }
      return pullEnemy(game)
    }
    game.anotherPull = false
    game.special.push(0)
    this
  }

  def undo(game: Game) : Enemy = {
    if(pulledCardsStack.top.equals(" ")) {
      enemyCards = enemyCards.take(pushedCardIndexStack.top) :+ pushedCardsStack.top :++ enemyCards.drop(pushedCardIndexStack.top)
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
      for(i <- 1 to enemyCards.length) {
        if(enemyCards(i - 1).toString.equals(pulledCardsStack.top) && c == 0) {
          c = i
        }
      }
      if(c == 0) {
        pulledCardsStack.pop()
        pushedCardIndexStack.pop()
        game.special.pop()
        game.undoVariable = true
        return this
      }
      val card = enemyCards(c-1)
      c = 0
      game.init.cardsCovered = card +: game.init.cardsCovered
      for (i <- 2 to enemyCards.length) {
        if (enemyCards(i - 2).color == card.color && enemyCards(i - 2).value == card.value && c == 0) {
          enemyCards = enemyCards.take(i - 2) ++ enemyCards.drop(i - 1)
          c = 1
        }
      }
      if (c == 0) {
        enemyCards = enemyCards.take(enemyCards.length - 1)
      }
      pulledCardsStack.pop()
      pushedCardIndexStack.pop()
      game.undoVariable = true
      if (anotherPullStack.top) {
        game.undoVariable = false
        game.anotherPull = false
      }
      anotherPullStack.pop()


      game.special.pop()
      if(game.special.top > 0) {
        for (_ <- 2 to game.special.top) {
          var c = 0
          for(i <- 1 to enemyCards.length) {
            if(enemyCards(i - 1).toString.equals(pulledCardsStack.top) && c == 0) {
              c = i
            }
          }
          if(c == 0) {
            pulledCardsStack.pop()
            pushedCardIndexStack.pop()
            game.special.pop()
            game.undoVariable = true
            return this
          }
          val card = enemyCards(c-1)
          c = 0
          game.init.cardsCovered = card +: game.init.cardsCovered
          for (i <- 2 to enemyCards.length) {
            if (enemyCards(i - 2).color == card.color && enemyCards(i - 2).value == card.value && c == 0) {
              enemyCards = enemyCards.take(i - 2) ++ enemyCards.drop(i - 1)
              c = 1
            }
          }
          if (c == 0) {
            enemyCards = enemyCards.take(enemyCards.length - 1)
          }
          pulledCardsStack.pop()
          pushedCardIndexStack.pop()
        }
      }
    }
    this
  }

  def pushCardEnemy(card: Card, game: Game) : Enemy = {
    var c = 0
    var max = 0
    var myCard = card
    if (card.color == Color.Special) {
      for (i <- 0 to 3) {
        c = 0
        for (j <- 0 to enemyCards.length - 1) {
          if (i == 0) {
            if (enemyCards(j).color == Color.Blue) {
              c += 1
            }
          } else if (i == 1) {
            if (enemyCards(j).color == Color.Green) {
              c += 1
            }
          } else if (i == 2) {
            if (enemyCards(j).color == Color.Yellow) {
              c += 1
            }
          } else {
            if (enemyCards(j).color == Color.Red) {
              c += 1
            }
          }
        }
        if (c >= max) {
          if (i == 0) {
            max = c
            myCard = Card(Color.Blue, card.value)
          } else if (i == 1) {
            max = c
            myCard = Card(Color.Green, card.value)
          } else if (i == 2) {
            max = c
            myCard = Card(Color.Yellow, card.value)
          } else {
            max = c
            myCard = Card(Color.Red, card.value)
          }
        }
      }
    }
    c = 0
    for (i <- 2 to enemyCards.length) {
      if (enemyCards(i - 2).color == card.color && enemyCards(i - 2).value == card.value && c == 0) {
        game.init.cardsRevealed = myCard +: game.init.cardsRevealed
        enemyCards = enemyCards.take(i - 2) ++ enemyCards.drop(i-1)
        c += 1
      }
    }
    if (c == 0) {
      game.init.cardsRevealed = myCard +: game.init.cardsRevealed
      enemyCards = enemyCards.take(enemyCards.length - 1)
    }
    if (card.value == Value.DirectionChange) {
      game.setDirection()
      game.special.push(0)
    } else if (card.value == Value.PlusTwo) {
      game.special.push(game.special.top + 2)
    } else if (card.value == Value.PlusFour) {
      game.special.push(game.special.top + 4)
    } else if (card.value == Value.Suspend) {
      game.special.push(-1)
    } else {
      game.special.push(0)
    }
    game.anotherPull = false
    this
  }
  def pullEnemy(game: Game) : Enemy = {
    pulledCardsStack.push(game.init.cardsCovered.head.toString)
    pushedCardIndexStack.push(-1)
    enemyCards += Card(game.init.cardsCovered.head.color, game.init.cardsCovered.head.value)
    game.init.cardsCovered = game.init.cardsCovered.drop(1)
    if (game.special.top > 0) {
      game.anotherPull = false
      anotherPullStack.push(false)
      for (_ <- 2 to game.special.top) {
        pulledCardsStack.push(game.init.cardsCovered.head.toString)
        pushedCardIndexStack.push(-1)
        enemyCards += Card(game.init.cardsCovered.head.color, game.init.cardsCovered.head.value)
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

  def pushable1(card : Card, game : Game) : Boolean = {
    if (((game.init.cardsRevealed.head.value == Value.PlusTwo && card.value == Value.PlusTwo) ||
          (game.init.cardsRevealed.head.value == Value.PlusFour && card.value == Value.PlusFour)) && game.special.top > 0)  {
      true
    } else {
      false
    }
  }
  def pushable2(card : Card, game : Game) : Boolean = {
    if (card.color == game.init.cardsRevealed.head.color && card.value != Value.Suspend &&
          card.value != Value.DirectionChange && card.value != Value.PlusTwo && card.color != Color.Special &&
          game.special.top <= 0)  {
      true
    } else {
      false
    }
  }
  def pushable3(card : Card, game : Game) : Boolean = {
    if (card.color == game.init.cardsRevealed.head.color && card.color != Color.Special &&
          game.special.top <= 0 && card.value != Value.PlusTwo) {
      true
    } else {
      false
    }
  }
  def pushable4(card : Card, game : Game) : Boolean = {
    if (card.value == game.init.cardsRevealed.head.value && card.color != Color.Special &&
          card.value != Value.Suspend && card.value != Value.DirectionChange && card.value != Value.PlusTwo &&
          game.special.top <= 0) {
      true
    } else {
      false
    }
  }
  def pushable5(card : Card, game : Game) : Boolean = {
    if (card.value == game.init.cardsRevealed.head.value && card.color != Color.Special &&
      game.special.top <= 0 && card.value != Value.PlusTwo) {
      true
    } else {
      false
    }
  }
  def pushable6(card : Card, game : Game) : Boolean = {
    if (card.value == Value.ColorChange && game.special.top <= 0) {
      true
    } else {
      false
    }
  }
  def pushable7(card : Card, game : Game) : Boolean = {
    if (game.init.cardsRevealed.head.color == Color.Special) {
      true
    } else {
      false
    }
  }
  def pushable8(card : Card, game : Game) : Boolean = {
    if (card.value == Value.PlusTwo && (game.init.cardsRevealed.head.value == card.value ||
          game.init.cardsRevealed.head.color == card.color) && game.special.top <= 0) {
      true
    } else {
      false
    }
  }
  def pushable9(card : Card, game : Game) : Boolean = {
    if (card.value == Value.PlusFour && game.init.cardsRevealed.head.value != Value.PlusTwo) {
      for (i <- 1 to enemyCards.length) {
        if (enemyCards(i - 1).color == game.init.cardsRevealed.head.color &&
            game.init.cardsRevealed.head.color != Color.Special && game.init.cardsRevealed.head.value != Value.PlusFour) {
          return false
        }
      }
      true
    } else {
      false
    }
  }

  def ki(game : Game) : Enemy = {
    for (i <- 1 to enemyCards.length) {
      if(pushable9(enemyCards(i - 1), game)) {
        return pushCardEnemy(enemyCards(i-1), game)
      }
    }
    for (i <- 1 to enemyCards.length) {
      if(enemyCards(i - 1).value == Value.PlusTwo && game.init.cardsRevealed.head.value != Value.PlusFour &&
        (enemyCards(i - 1).value == game.init.cardsRevealed.head.value ||
          enemyCards(i - 1).color == game.init.cardsRevealed.head.color)) {
        return pushCardEnemy(enemyCards(i-1), game)
      }
    }
    for (i <- 1 to enemyCards.length) {
      if((enemyCards(i - 1).value == Value.Suspend || enemyCards(i - 1).value == Value.DirectionChange) &&
        ((game.init.cardsRevealed.head.value != Value.PlusTwo &&
          game.init.cardsRevealed.head.value != Value.PlusFour) || game.special.top <= 0) &&
          (enemyCards(i - 1).value == game.init.cardsRevealed.head.value ||
          enemyCards(i - 1).color == game.init.cardsRevealed.head.color)) {
        return pushCardEnemy(enemyCards(i-1), game)
      }
    }
    this
  }
}
