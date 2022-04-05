package de.htwg.se.uno.controller

import de.htwg.se.uno.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.uno.model.gameComponent.gameBaseImpl._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.language.reflectiveCalls

class ControllerSpec extends AnyWordSpec with Matchers {
  "A Controller" when {
    "it's a Publisher" should {
      val colorChange = Card(Color.Special, Value.ColorChange)
      val redOne = Card(Color.Red, Value.One)
      val yellowTwo = Card(Color.Yellow, Value.Two)
      val game = Game(2)
      val controller = new Controller(game)
      "Be able to create a game with 2 Players" in {
        controller.createGame(2)
        controller.getNumOfPlayers should be(2)
      }
      "Be able to create a new Game with 3 Players" in{
        controller.createGame(3)
        controller.getNumOfPlayers should be(3)
      }
      "Be able to create a new Game with 4 Players" in{
        controller.createGame(4)
        controller.getNumOfPlayers should be(4)
      }
      "Not Push a Special card without a color" in {
        controller.game = controller.game.copyGame(player = Player(colorChange :: controller.game.player.handCards), revealedCards = redOne :: controller.game.revealedCards)
        controller.set(controller.getCardText(4, 0))
        controller.getHs2 should be(controller.getCardText(4, 0))
      }
      "not push a Card if the Card is not pushable" in {
        controller.game = controller.game.copyGame(player = Player(redOne :: controller.game.player.handCards), revealedCards = yellowTwo :: controller.game.revealedCards)
        controller.set(controller.getCardText(4, 0))
        controller.nextTurn() should be(true)
      }
      "be able to push a Special Card with a color" in {
        controller.game = controller.game.copyGame(player = Player(colorChange :: controller.game.player.handCards), revealedCards = redOne :: controller.game.revealedCards)
        controller.set(controller.getCardText(4,0), 4)
        controller.controllerEvent("idle") should be(controller.controllerEvent("enemyTurn"))
      }
      "not push a Card if it's not the players turn" in {
        controller.createGame(2)
        controller.game = controller.game.copyGame(player = Player(List(redOne)))
        controller.game = controller.game.changeActivePlayer()
        controller.set(controller.getCardText(4, 0))
        controller.controllerEvent("idle") should be(controller.controllerEvent("enemyTurn"))
      }
      "Not pull a Card if it's not the players turn" in {
        controller.createGame(2)
        controller.game = controller.game.changeActivePlayer()
        controller.get()
        controller.controllerEvent("idle") should be(controller.controllerEvent("enemyTurn"))
      }

      "Should be able to undo a Step" in {
        controller.createGame(2)
        controller.game = controller.game.copyGame(player = Player(colorChange :: controller.game.player.handCards), revealedCards = redOne :: controller.game.revealedCards)
        controller.set(controller.getCardText(4,0), 4)
        controller.undo()
        controller.controllerEvent("idle") should be(controller.controllerEvent("undo"))
      }

      "Pull a Card if it is allowed" in {
        controller.get()
        controller.controllerEvent("idle") should be(controller.controllerEvent("yourTurn"))
      }
      "Not Pull another Card if a Card was already Pulled" in {
        controller.createGame(4)
        controller.get()
        controller.controllerEvent("idle") should be(controller.controllerEvent("yourTurn"))
        controller.get()
        controller.controllerEvent("idle") should be(controller.controllerEvent("enemyTurn"))
      }
      "Do the enemy's runs if it's the enemys turn" in {
        controller.game = controller.game.copyGame(enemies = List(Enemy(List(redOne, yellowTwo)), Enemy(List(redOne, yellowTwo)), Enemy(List(redOne, yellowTwo))), revealedCards = redOne :: controller.game.revealedCards)
        controller.enemy()
        controller.enemy()
        controller.enemy()
        controller.controllerEvent("idle") should be(controller.controllerEvent("yourTurn"))
      }
      "Should be able to undo a second Step" in {
        controller.undo()
        controller.controllerEvent("idle") should be(controller.controllerEvent("undo"))
      }
      "Should have a Enemy pull twice if he can't push a Card" in {
        controller.createGame(2)
        controller.game = controller.game.copyGame(enemies = List(Enemy(List(redOne)), Enemy(), Enemy()), coveredCards = List(redOne), revealedCards = List(yellowTwo))
        controller.game = controller.game.changeActivePlayer()
        controller.enemy()
        controller.game.activePlayer should be(0)
        controller.game.alreadyPulled should be(true)
        controller.enemy()
        controller.game.activePlayer should be(1)
        controller.game.alreadyPulled should be(false)
      }
      "Not Pull a Card if the player has to suspend" in {
        controller.createGame(4)
        controller.game = controller.game.copyGame(revealedCardEffect = -1)
        controller.get()
        controller.controllerEvent("idle") should be(controller.controllerEvent("pullCardNotAllowed"))
      }
      "Should be able to undo a third step" in {
        controller.createGame(4)
        controller.game = controller.game.copyGame(revealedCardEffect = -1)
        controller.get()
        controller.controllerEvent("idle") should be(controller.controllerEvent("pullCardNotAllowed"))
        controller.undo()
        controller.controllerEvent("idle") should be(controller.controllerEvent("undo"))
      }

      "Should be able to redo a Step" in{
        controller.createGame(4)
        controller.game = controller.game.copyGame(revealedCardEffect = -1)
        controller.get()
        controller.controllerEvent("idle") should be(controller.controllerEvent("pullCardNotAllowed"))
        controller.undo()
        controller.controllerEvent("idle") should be(controller.controllerEvent("undo"))
        controller.redo()
        controller.controllerEvent("idle") should be(controller.controllerEvent("redo"))
      }
      "Should be able to check if nobody has won" in {
        controller.createGame(4)
        controller.won()
        controller.controllerEvent("idle") should be(controller.controllerEvent("yourTurn"))
      }

      "Should be able to check if the enemy 1 has won" in {
        controller.createGame(4)
        controller.game = controller.game.copyGame(numOfPlayers = 2, enemies = List(Enemy(), Enemy(), Enemy()), player = Player(List(Card(Color.Blue, Value.Zero))))
        controller.won()
        controller.controllerEvent("idle") should be(controller.controllerEvent("lost"))
      }
      "Should be able to check if the player has won" in {
        controller.createGame(4)
        controller.game = controller.game.copyGame(player = Player(), enemies = List(Enemy(List(redOne)), Enemy(List(redOne)), Enemy(List(redOne))))
        controller.won()
        controller.controllerEvent("idle") should be(controller.controllerEvent("won"))
      }
      "Should be able to check if the enemy 2 has won" in {
        controller.createGame(4)
        controller.game = controller.game.copyGame(player = Player(List(redOne)), enemies = List(Enemy(List(redOne)), Enemy(), Enemy(List(redOne))))
        controller.won()
        controller.controllerEvent("idle") should be(controller.controllerEvent("lost"))
      }
      "Should be able to check if the enemy 3 has won" in {
        controller.createGame(4)
        controller.game = controller.game.copyGame(player = Player(List(redOne)), enemies = List(Enemy(List(redOne)), Enemy(List(redOne)), Enemy()))
        controller.won()
        controller.controllerEvent("idle") should be(controller.controllerEvent("lost"))
      }

      "Should Have a String Representation of the game" in {
        controller.createGame(4)
        controller.gameToString should be (controller.game.toString)
      }
      "Should be able to get the cardText of a Card" in {
        controller.createGame(4)
        controller.game = controller.game.copyGame(player = Player(redOne :: controller.game.player.handCards), revealedCards = List(redOne))
        controller.getCardText(4,0) should be("R 1")
        controller.getCardText(3,1) should be("R 1")
        controller.getCardText(3,2) should be("Do Step")
        controller.getCardText(0, 0) should be("Uno")
      }
      "Should be able to get the guiCardText of a Card" in {
        controller.createGame(4)
        controller.game = controller.game.copyGame(player = Player(redOne :: controller.game.player.handCards), revealedCards = List(redOne))
        controller.getGuiCardText(4,0) should be(" 1 ")
        controller.getGuiCardText(3,1) should be(" 1 ")
        controller.getGuiCardText(3,2) should be("Do Step")
        controller.getGuiCardText(0, 0) should be("Uno")
      }
      "Should be able to get the Length of a List" in {
        controller.createGame(4)
        controller.getLength(0) should be(7)
        controller.getLength(1) should be(7)
        controller.getLength(2) should be(7)
        controller.getLength(3) should be(1)
        controller.getLength(4) should be(7)
        controller.getLength(5) should be(79)
      }
      "Should be able to get the number of Players" in {
        controller.createGame(4)
        controller.game.numOfPlayers should be(4)
      }
      "Should be able to check if it's the players turn" in {
        controller.createGame(4)
        controller.nextTurn() should be(true)
      }
      "Should be able to return the Help String 2 of the controller" in {
        controller.createGame(4)
        controller.game = controller.game.copyGame(player = Player(colorChange :: controller.game.player.handCards), revealedCards = redOne :: controller.game.revealedCards)
        controller.set(controller.getCardText(4, 0))
        controller.getHs2 should be("S C")
      }
      "Should be able to get the next Enemy" in {
        controller.createGame(4)
        controller.game = controller.game.changeActivePlayer()
        controller.nextEnemy() should be(1)
      }

      "Should be able to save the game" in {
        controller.save()
        controller.controllerEvent("idle") should be(controller.controllerEvent("save"))
      }
      "Should be able to load the game" in {
        controller.load()
        controller.controllerEvent("idle") should be(controller.controllerEvent("load"))
      }
      "Should be able to shuffle the covered cards" in {
        controller.createGame(4)
        controller.game = controller.game.copyGame(coveredCards = List(redOne, yellowTwo))
        controller.shuffle()
        controller.controllerEvent("idle") should be(controller.controllerEvent("shuffled"))
      }


      "Should be able to update the state to pushCardNotAllowed Event" in {
        controller.controllerEvent("pushCardNotAllowed")
        controller.controllerEvent("idle") should be("Du kannst diese Karte nicht legen")
      }
      "Should be able to update the state to enemys turn" in {
        controller.controllerEvent("enemyTurn")
        controller.controllerEvent("idle") should be("Gegner ist an der Reihe")
      }
      "Should be able to update the state to pullCardNotAllowed Event" in {
        controller.controllerEvent("pullCardNotAllowed")
        controller.controllerEvent("idle") should be("Du kannst keine Karte ziehen")
      }
      "Should be able to update the state to unknownCommand Event" in {
        controller.controllerEvent("unknownCommand")
        controller.controllerEvent("idle") should be("Befehl nicht bekannt")
      }
      "Should be able to update the state to your turn" in {
        controller.controllerEvent("yourTurn")
        controller.controllerEvent("idle") should be("Du bist dran. Mögliche Befehle: q, n [2 | 3 | 4], t, s Karte [Farbe], g, u, r, d, sv, ld")
      }
      "Should be able to update the state to won Event" in {
        controller.controllerEvent("won")
        controller.controllerEvent("idle") should be("Glückwunsch, du hast gewonnen!")
      }
      "Should be able to update the state to lost Event" in {
        controller.controllerEvent("lost")
        controller.controllerEvent("idle") should be("Du hast leider verloren")
      }
      "Should be able to update the state to undo Event" in {
        controller.controllerEvent("undo")
        controller.controllerEvent("idle") should be("Zug rückgängig gemacht")
      }
      "Should be able to update the state to redo Event" in {
        controller.controllerEvent("redo")
        controller.controllerEvent("idle") should be("Zug wiederhergestellt")
      }
      "Should be able to update the state to chooseColor Event" in {
        controller.controllerEvent("chooseColor")
        controller.controllerEvent("idle") should be(controller.controllerEvent("chooseColor"))
      }
      "Should be able to update the state to save Event" in {
        controller.controllerEvent("save")
        controller.controllerEvent("idle") should be(controller.controllerEvent("save"))
      }
      "Should be able to update the state to load Event" in {
        controller.controllerEvent("load")
        controller.controllerEvent("idle") should be(controller.controllerEvent("load"))
      }
      "Should be able to update the state to shuffled Event" in {
        controller.controllerEvent("shuffled")
        controller.controllerEvent("idle") should be(controller.controllerEvent("shuffled"))
      }
      "Should not change the state on input idle" in {
        controller.controllerEvent("idle") should be(controller.controllerEvent("shuffled"))
      }
    }
  }
}