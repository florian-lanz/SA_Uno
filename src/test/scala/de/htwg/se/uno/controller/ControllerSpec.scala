package de.htwg.se.uno.controller

import de.htwg.se.uno.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.uno.model.gameComponent.gameBaseImpl.Game
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.language.reflectiveCalls

class ControllerSpec extends AnyWordSpec with Matchers {

  "A Controller" when {
    "it's a Publisher" should {
      val game = Game(2)
      val controller = new Controller(game)
      "Be able to create a game with 2 Players" in {
        controller.createGame(2)
        controller.getNumOfPlayers should be(2)
      }
      "Be able to create a new Game with 4 Players" in{
        controller.createGame(4)
        controller.getNumOfPlayers should be(4)
      }
      "Be able to create a new Game with 3 Players" in{
        controller.createGame(3)
        controller.getNumOfPlayers should be(3)
      }
      "Be able to create a new Test Game" in{
        controller.createTestGame()
        controller.getNumOfPlayers should be(4)
      }

      "Not Push a Special card without a color" in {
        controller.set(controller.getCardText(4, 1))
        controller.getHs2 should be(controller.getCardText(4, 1))
      }
      "not push a Card if the Card is not pushable" in {
        val old = controller.gameToString
        controller.set(controller.getCardText(4, 4))
        controller.gameToString should be(old)
      }
      "be able to push a Special Card with a color" in {
        controller.set(controller.getCardText(4,1), 4)
        controller.controllerEvent("idle") should be(controller.controllerEvent("enemyTurn"))
      }
      "not push a Card if it's not the players turn" in {
        val old = controller.gameToString
        controller.set(controller.getCardText(4, 1))
        controller.gameToString should be(old)
      }
      "Not pull a Card if it's not the players turn" in {
        val old = controller.gameToString
        controller.get()
        controller.gameToString should be(old)
      }


      "Should be able to undo a Step" in {
        controller.undo()
        controller.controllerEvent("idle") should be(controller.controllerEvent("undo"))
      }


      "Pull a Card if it is allowed" in {
        controller.get()
        controller.controllerEvent("idle") should be(controller.controllerEvent("yourTurn"))
      }
      "Not Pull another Card if a Card was already Pulled" in {
        controller.get()
        controller.controllerEvent("idle") should be(controller.controllerEvent("enemyTurn"))
      }
      "Do the enemy's runs if it's the enemys turn" in {
        controller.enemy()
        controller.enemy()
        controller.enemy()
        controller.controllerEvent("idle") should be(controller.controllerEvent("yourTurn"))
      }
      "Should be able to undo a second Step" in {
        controller.undo()
        controller.controllerEvent("idle") should be(controller.controllerEvent("undo"))
      }
      "Do the enemy's run so that the enemy pulls and then can't do anything" in {
        controller.set("S C", 1)
        controller.enemy()
        controller.enemy()
        controller.enemy()
        controller.set("R+2")
        controller.enemy()
        controller.enemy()
        controller.enemy()
        controller.get()
        controller.enemy()
        controller.enemy()
        controller.enemy()
        controller.set("Y 4")
        controller.enemy()
        controller.enemy()
      }
      "Should be able to undo a third step" in {
        controller.undo()
        controller.controllerEvent("idle") should be(controller.controllerEvent("undo"))
      }
      "Not Pull a Card if the player has to suspend" in {
        controller.game.setSpecialTop(-1)
        controller.get()
        controller.controllerEvent("idle") should be(controller.controllerEvent("pullCardNotAllowed"))
      }
      "Should be able to undo a fourth step" in {
        controller.undo()
        controller.controllerEvent("idle") should be(controller.controllerEvent("undo"))
      }

      "Should be able to redo a Step" in{
        controller.redo()
        controller.controllerEvent("idle") should be(controller.controllerEvent("redo"))
      }
      "Should be able to check if nobody has won" in {
        controller.won()
        controller.controllerEvent("idle") should be(controller.controllerEvent("redo"))
      }

      "Should be able to check if the enemy 1 has won" in {
        controller.game.setLength(1)
        controller.won()
        controller.controllerEvent("idle") should be(controller.controllerEvent("lost"))
      }
      "Should be able to check if the player has won" in {
        controller.game.setLength(4)
        controller.won()
        controller.controllerEvent("idle") should be(controller.controllerEvent("won"))
      }
      "Should be able to check if the enemy 2 has won" in {
        controller.game.setLength(2)
        controller.won()
        controller.controllerEvent("idle") should be(controller.controllerEvent("lost"))
      }
      "Should be able to check if the enemy 3 has won" in {
        controller.game.setLength(3)
        controller.controllerEvent("yourTurn")
        controller.won()
        controller.controllerEvent("idle") should be(controller.controllerEvent("lost"))
      }


      "Should Have a String Representation of the game" in {
        controller.gameToString should be (controller.game.toString)
      }
      "Should be able to get the cardText of a Card" in {
        controller.getCardText(0,0) should be("Uno")
      }
      "Should be able to get the guiCardText of a Card" in {
        controller.getGuiCardText(0,0) should be("Uno")
      }
      "Should be able to get the Length of a List" in {
        controller.getLength(1) should be(6)
      }
      "Should be able to get the number of Players" in {
        controller.getNumOfPlayers should be(4)
      }
      "Should be able to check if it's the players turn" in {
        controller.nextTurn() should be(false)
      }
      "Should be able to return the Help String 2 of the controller" in {
        controller.getHs2 should be("S C")
      }
      "Should be able to get the next Enemy" in {
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
        controller.game.setLength(5)
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