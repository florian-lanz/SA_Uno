package de.htwg.se.uno.controller

import de.htwg.se.uno.controller.controllerComponent.controllerBaseImpl.{Controller, EnemyCommand2}
import de.htwg.se.uno.model.gameComponent.gameBaseImpl.Game
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class EnemyCommand2Spec extends AnyWordSpec with Matchers {
  "A EnemyCommand" when {
    "new" should {
      val controller = new Controller(Game(4))
      controller.createTestGame()
      val command = new EnemyCommand2(controller)
      "Be able to do a Step" in {
        controller.game.setSpecialTop(-1)
        command.doStep
        controller.game.getRedoVariable() should be(false)
      }
      "Be able to undo a Step" in {
        command.undoStep
        controller.game.getUndoVariable() should be(true)
      }
      "Be able to redo a Step" in {
        command.redoStep
        controller.game.getRedoVariable() should be(false)
      }
    }
  }
}