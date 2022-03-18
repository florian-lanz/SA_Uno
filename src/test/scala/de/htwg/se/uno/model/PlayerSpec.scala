package de.htwg.se.uno.model

import de.htwg.se.uno.model.gameComponent.gameBaseImpl._
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec

class PlayerSpec extends AnyWordSpec {
  "A Player" when {
    "new" should {
      var newGame = Game(4)
      newGame.init = InitializeGameStrategy(1)
      newGame.init = newGame.init.initializeGame(4)
      "Should be able to do a pushMove" in {
        newGame.init.player.pushMove("R 1", 0, newGame) should be(newGame.init.player)
      }
      "Should be able to not do a pushMove" in {
        newGame.init.player.pushMove("Hey", 0, newGame) should be(newGame.init.player)
      }

      "Should be able to do a pullMove" in {
        newGame.init.player.pullMove(newGame) should be(newGame.init.player)
      }
      "Should be able to not do a pullMove" in {
        newGame.init.player.pullMove(newGame) should be(newGame.init.player)
      }

      "Should be able to undo a Move" in {
        newGame = newGame.createTestGame()
        newGame.init.player.pulledCardsStack.push(" ")
        newGame.init.player.pushedCardIndexStack.push(2)
        newGame.init.player.pushedCardsStack.push(Card(Color.Blue, Value.DirectionChange))
        newGame.special.push(0)
        newGame.init.player.undo(newGame) should be(newGame.init.player)
      }
      "Should be able to undo a third move" in {
        newGame = newGame.createTestGame()
        newGame.init.player.pulledCardsStack.push("R 1")
        newGame.init.player.pushedCardIndexStack.push(-1)
        newGame.init.player.anotherPullStack.push(true)
        newGame.special.push(0)
        newGame.init.player.undo(newGame) should be(newGame.init.player)
      }
      "Should be able to undo a fourth move" in {
        newGame = newGame.createTestGame()
        newGame.init.player.pulledCardsStack.push("Start")
        newGame.init.player.pushedCardIndexStack.push(-1)
        newGame.init.player.anotherPullStack.push(true)
        newGame.special.push(4)
        newGame.special.push(0)
        newGame.init.player.pulledCardsStack.push("R 1")
        newGame.init.player.pushedCardIndexStack.push(-1)
        newGame.init.player.pulledCardsStack.push("R S")
        newGame.init.player.pushedCardIndexStack.push(-1)
        newGame.init.player.pulledCardsStack.push("R+2")
        newGame.init.player.pushedCardIndexStack.push(-1)
        newGame.init.player.undo(newGame) should be(newGame.init.player)
      }

      "Should check if a card is pushable" in {
        newGame = newGame.createTestGame()
        newGame.init.cardsRevealed = Card(Color.Red, Value.PlusTwo) +: newGame.init.cardsRevealed
        newGame.special.push(2)
        newGame.init.player.pushable(Card(Color.Red, Value.One), newGame) should be(false)
      }
      "Should check if a second card is pushable" in {
        newGame = newGame.createTestGame()
        newGame.init.cardsRevealed = Card(Color.Red, Value.PlusFour) +: newGame.init.cardsRevealed
        newGame.special.push(2)
        newGame.init.player.pushable(Card(Color.Red, Value.One), newGame) should be(false)
      }
      "Should check if a third card is pushable" in {
        newGame = newGame.createTestGame()
        newGame.init.player.pushable(Card(Color.Special, Value.PlusFour), newGame) should be(false)
      }
      "Should check if a fourth card is pushable" in {
        newGame = newGame.createTestGame()
        newGame.init.cardsRevealed = Card(Color.Blue, Value.Nine) +: newGame.init.cardsRevealed
        newGame.init.player.pushable(Card(Color.Special, Value.PlusFour), newGame) should be(true)
      }
      "Should check if a fifth card is pushable" in {
        newGame = newGame.createTestGame()
        newGame.init.player.pushable(Card(Color.Red, Value.One), newGame) should be(true)
      }
      "Should check if a sixth card is pushable" in {
        newGame = newGame.createTestGame()
        newGame.init.cardsRevealed = Card(Color.Special, Value.ColorChange) +: newGame.init.cardsRevealed
        newGame.init.player.pushable(Card(Color.Red, Value.One), newGame) should be(true)
      }
      "Should check if a seventh card is pushable" in {
        newGame = newGame.createTestGame()
        newGame.init.cardsRevealed = Card(Color.Blue, Value.Nine) +: newGame.init.cardsRevealed
        newGame.init.player.pushable(Card(Color.Red, Value.One), newGame) should be(false)
      }

      "Should be able to push a Card" in {
        newGame = newGame.createTestGame()
        newGame.init.player.pushCard(Card(Color.Special, Value.ColorChange), 1, newGame) should be(newGame.init.player)
      }
      "Should be able to push a second Card" in {
        newGame = newGame.createTestGame()
        newGame.init.player.pushCard(Card(Color.Special, Value.ColorChange), 2, newGame) should be(newGame.init.player)
      }
      "Should be able to push a third Card" in {
        newGame = newGame.createTestGame()
        newGame.init.player.pushCard(Card(Color.Special, Value.ColorChange), 3, newGame) should be(newGame.init.player)
      }
      "Should be able to push a fourth Card" in {
        newGame = newGame.createTestGame()
        newGame.init.player.pushCard(Card(Color.Special, Value.PlusFour), 4, newGame) should be(newGame.init.player)
      }
      "Should be able to push a fifth Card" in {
        newGame = newGame.createTestGame()
        newGame.init.player.pushCard(Card(Color.Red, Value.PlusTwo), 0, newGame) should be(newGame.init.player)
      }
      "Should be able to push a sixth Card" in {
        newGame = newGame.createTestGame()
        newGame.init.player.pushCard(Card(Color.Red, Value.DirectionChange), 0, newGame) should be(newGame.init.player)
      }
      "Should be able to push a seventh Card" in {
        newGame = newGame.createTestGame()
        newGame.init.player.pushCard(Card(Color.Red, Value.Suspend), 0, newGame) should be(newGame.init.player)
      }

      "Should be able to pull a Card" in {
        newGame = newGame.createTestGame()
        newGame.special.push(4)
        newGame.init.player.pull(newGame) should be(newGame.init.player)
      }
      "Should be able to pull a second Card" in {
        newGame = newGame.createTestGame()
        newGame.init.player.pull(newGame) should be(newGame.init.player)
      }

      "Should check if a String equals a card" in {
        newGame.init.player.equalsCard("R 1") should be(true)
      }
      "Should check if a String equals no card" in {
        newGame.init.player.equalsCard("Hey") should be(false)
      }

      "Should get the Carf of a String" in {
        newGame.init.player.getCard("R 1") should be(Card(Color.Red, Value.One))
      }
    }
  }
}
