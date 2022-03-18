package de.htwg.se.uno.controller

import de.htwg.se.uno.controller.controllerComponent.controllerBaseImpl.{Controller, PullCommand}
import de.htwg.se.uno.model.gameComponent.gameBaseImpl.Game
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class PullCommandSpec extends AnyWordSpec with Matchers {
  "A PullCommand" when {
    "new" should {
      val controller = new Controller(Game(4))
      controller.createTestGame()
      val command = new PullCommand(controller)
      "Be able to do a Step" in {
        controller.game.setSpecialTop(-1)
        command.doStep
        controller.game.getActivePlayer() should be(0)
      }
      "Be able to undo a Step" in {
        controller.game.setAnotherPull(true)
        command.undoStep
        controller.game.getAnotherPull() should be(false)
      }
      "Be able to redo a Step" in {
        command.redoStep
        controller.game.getActivePlayer() should be (1)
      }
    }
  }
}
