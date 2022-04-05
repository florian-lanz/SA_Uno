package de.htwg.se.uno.model

import de.htwg.se.uno.model.gameComponent.gameBaseImpl.{Card, Color, Enemy, Game, Player, Value}
import org.scalatest.matchers.should.Matchers.*
import org.scalatest.wordspec.AnyWordSpec

class GameSpec extends AnyWordSpec {
  "A Game" when {
    "new" should {
      val greenSuspend = Card(Color.Green, Value.Suspend)
      val bluePlusTwo = Card(Color.Blue, Value.PlusTwo)
      val yellowPlusFour = Card(Color.Yellow, Value.PlusFour)
      val redZero = Card(Color.Red, Value.Zero)
      val game = Game(2)
      "Should be able to be copied" in {
        game.copyGame() should be (Game(2))
      }
      "Should be able to create a new game" in {
        game.createGame().numOfPlayers should be (2)
        game.createGame().coveredCards.length should be (93)
        game.createGame().revealedCards.length should be (1)
        game.createGame().player.handCards.length should be (7)
        game.createGame().enemies.head.enemyCards.length should be (7)
        game.createGame().enemies(1).enemyCards.length should be (0)
        game.createGame().enemies(2).enemyCards.length should be (0)
        game.createGame().revealedCardEffect should be (0)
        game.createGame().activePlayer should be (1)
        game.createGame().direction should be (true)
        game.createGame().alreadyPulled should be (false)
      }
      "Should be able to push a player card" in {
//        val oldGameTwoPlayers = Game(
//          numOfPlayers = 2,
//          coveredCards = List(redZero, blueOne),
//          revealedCards = List(blueOne, greenTwo),
//          player = Player(List(greenTwo, yellowThree)),
//          enemies = List(Enemy(List(yellowThree, redZero)), Enemy(), Enemy()),
//          revealedCardEffect = 4,
//          direction = false,
//          alreadyPulled = true
//        )
        game.copyGame() should be (Game(2))
      }


      "Should be able to return the revealed effect of a card" in {
        game.discoverRevealedCardEffect(greenSuspend) should be (-1)
        game.discoverRevealedCardEffect(bluePlusTwo) should be (2)
        game.discoverRevealedCardEffect(yellowPlusFour) should be (4)
        game.discoverRevealedCardEffect(redZero) should be (0)
      }
      "Should be able to return the next enemy" in {
        val gameThreePlayer = Game(numOfPlayers = 3, direction = false)
        val gameFourPlayer = Game(numOfPlayers = 4, direction = false)
        game.nextEnemy() should be (1)
        gameThreePlayer.nextEnemy() should be (2)
        gameFourPlayer.nextEnemy() should be (3)
      }
      "Should be able to return if it is the players turn" in {
        val gameThreePlayer = Game(numOfPlayers = 3, activePlayer = 2)
        gameThreePlayer.nextTurn() should be (true)
        game.nextTurn() should be (false)
      }
      "Should be able to change the active player" in {
        val gameThreePlayer = Game(numOfPlayers = 3, activePlayer = 2)
        gameThreePlayer.changeActivePlayer().activePlayer should be (0)
        game.changeActivePlayer().activePlayer should be (1)
      }
      "Should be able to add a card to a list" in {
        game.coveredCards should be (List())
        game.addCardToList(5, redZero).coveredCards should be (List(redZero))
      }
      "Should be able to reverse a list" in {
        val gameSpec = Game(numOfPlayers = 2, coveredCards = List(redZero, bluePlusTwo, yellowPlusFour, greenSuspend))
        gameSpec.reverseList(5).coveredCards should be (List(greenSuspend, yellowPlusFour, bluePlusTwo, redZero))
      }
      "Should be able to shuffle" in {
        val shuffledGame = Game(numOfPlayers = 2, coveredCards = List(redZero), revealedCards = List(redZero, bluePlusTwo, yellowPlusFour, greenSuspend)).shuffle()
        shuffledGame.coveredCards.length should be (4)
        shuffledGame.revealedCards should be (List(redZero))
      }
      "Should be able to have a string representation of the game" in {
        val newGame = Game(4).createGame()
        newGame.toString should not be ""
      }
    }
  }
}