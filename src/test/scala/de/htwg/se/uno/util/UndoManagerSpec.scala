package de.htwg.se.uno.util

import de.htwg.se.uno.controller.controllerComponent.controllerBaseImpl.{Controller, PullCommand}
import de.htwg.se.uno.model.gameComponent.gameBaseImpl.Game
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec

class UndoManagerSpec extends AnyWordSpec {
  "A UndoManager" when {
    "new" should {
      val undoManager = new UndoManager()
      val controller = new Controller(Game(4))
      controller.createTestGame()
      val command = new PullCommand(controller)
      "Not be able to undo a Step" in {
        undoManager.undoStep()
        controller.game.getActivePlayer() should be(3)
      }
      "Not be able to redo a Step" in {
        undoManager.redoStep
        controller.game.getActivePlayer() should be(3)
      }
      "be able to do a Step" in {
        controller.game.setSpecialTop(-1)
        undoManager.doStep(command)
        controller.game.getActivePlayer() should be(0)
      }
      "be able to undo a Step" in {
        controller.game.setAnotherPull(true)
        undoManager.undoStep()
        controller.game.getAnotherPull() should be(false)
      }
      "be able to redo a Step" in {
        undoManager.redoStep
        controller.game.getActivePlayer() should be (1)
      }
    }
  }
}
