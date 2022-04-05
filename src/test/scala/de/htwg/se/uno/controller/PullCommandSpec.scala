package de.htwg.se.uno.controller

import de.htwg.se.uno.controller.controllerComponent.controllerBaseImpl.{Controller, PullCommand}
import de.htwg.se.uno.model.gameComponent.gameBaseImpl._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class PullCommandSpec extends AnyWordSpec with Matchers {
  "A PullCommand" when {
    "new" should {
      val redZero = Card(Color.Red, Value.Zero)
      val blueOne = Card(Color.Blue, Value.One)
      val greenTwo = Card(Color.Green, Value.Two)
      val yellowThree = Card(Color.Yellow, Value.Three)
      val game = Game(
        numOfPlayers = 2,
        coveredCards = List(redZero, blueOne),
        revealedCards = List(redZero),
        player = Player(List(greenTwo, yellowThree)),
        enemies = List(Enemy(List(greenTwo)), Enemy(), Enemy()),
      )
      val controller = new Controller(game)
      val command = new PullCommand(controller)
      "Be able to do a Step" in {
        command.doStep()
        controller.game.player.handCards should be (List(redZero, greenTwo, yellowThree))
      }
      "Be able to undo a Step" in {
        controller.game = game
        controller.undoList = controller.fileIo.gameToString(controller.game) :: controller.undoList
        command.doStep()
        controller.game.player.handCards should be (List(redZero, greenTwo, yellowThree))
        command.undoStep()
        controller.game.player.handCards should be (List(greenTwo, yellowThree))
      }
      "Be able to redo a Step" in {
        controller.game = game
        controller.undoList = controller.fileIo.gameToString(controller.game) :: controller.undoList
        command.doStep()
        controller.game.player.handCards should be (List(redZero, greenTwo, yellowThree))
        command.undoStep()
        controller.game.player.handCards should be (List(greenTwo, yellowThree))
        command.redoStep()
        controller.game.player.handCards should be (List(redZero, greenTwo, yellowThree))
      }
    }
  }
}
