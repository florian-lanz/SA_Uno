package de.htwg.se.uno.controller

import de.htwg.se.uno.controller.controllerComponent.controllerBaseImpl.{Controller, PullCommand, PushCommand}
import de.htwg.se.uno.model.gameComponent.gameBaseImpl.{Card, Color, Enemy, Game, Player, Value}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class PushCommandSpec extends AnyWordSpec with Matchers {
  "A PushCommand" when {
    "new" should {
      val redZero = Card(Color.Red, Value.Zero)
      val blueOne = Card(Color.Blue, Value.One)
      val greenTwo = Card(Color.Green, Value.Two)
      val yellowThree = Card(Color.Yellow, Value.Three)
      val game = Game(
        numOfPlayers = 2,
        coveredCards = List(redZero, blueOne),
        revealedCards = List(redZero),
        player = Player(List(redZero, greenTwo, yellowThree)),
        enemies = List(Enemy(List(greenTwo)), Enemy(), Enemy()),
      )
      val controller = new Controller(game)
      val command = new PushCommand(controller.getCardText(4, 0), 0, controller)
      "Be able to do a Step" in {
        command.doStep()
        controller.game.player.handCards should be (List(greenTwo, yellowThree))
        controller.game.revealedCards should be (List(redZero, redZero))
      }
      "Be able to undo a Step" in {
        controller.game = game
        controller.undoList = controller.fileIo.gameToString(controller.game) :: controller.undoList
        command.doStep()
        controller.game.player.handCards should be (List(greenTwo, yellowThree))
        controller.game.revealedCards should be (List(redZero, redZero))
        command.undoStep()
        controller.game.player.handCards should be (List(redZero, greenTwo, yellowThree))
        controller.game.revealedCards should be (List(redZero))
      }
      "Be able to redo a Step" in {
        controller.game = game
        controller.undoList = controller.fileIo.gameToString(controller.game) :: controller.undoList
        command.doStep()
        controller.game.player.handCards should be (List(greenTwo, yellowThree))
        controller.game.revealedCards should be (List(redZero, redZero))
        command.undoStep()
        controller.game.player.handCards should be (List(redZero, greenTwo, yellowThree))
        controller.game.revealedCards should be (List(redZero))
        command.redoStep()
        controller.game.player.handCards should be (List(greenTwo, yellowThree))
        controller.game.revealedCards should be (List(redZero, redZero))
      }
    }
  }
}
