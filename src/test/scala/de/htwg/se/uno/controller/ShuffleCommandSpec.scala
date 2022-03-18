package de.htwg.se.uno.controller

import de.htwg.se.uno.controller.controllerComponent.controllerBaseImpl.{Controller, ShuffleCommand}
import de.htwg.se.uno.model.gameComponent.gameBaseImpl.Game
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ShuffleCommandSpec extends AnyWordSpec with Matchers {
  "A ShuffleCommand" when {
    "new" should {
      val controller = new Controller(Game(4))
      controller.createTestGame()
      val command = new ShuffleCommand(controller)
      "Be able to do a Step" in {
        controller.set("R 1")
        controller.game.getLength(3) should be(2)
        command.doStep
        controller.game.getLength(3) should be(1)
      }
      "Be able to undo a Step" in {
        command.undoStep
        controller.game.getLength(3) should be(2)
      }
      "Be able to redo a Step" in {
        command.redoStep
        controller.game.getLength(3) should be(1)
      }
    }
  }
}
