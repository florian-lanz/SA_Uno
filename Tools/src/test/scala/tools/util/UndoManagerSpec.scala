package tools.util

import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec

class testCommand extends Command:
  var testState: Int = 0

  override def doStep(): Unit = testState += 1

  override def undoStep(): Unit = testState -= 1

  override def redoStep(): Unit = testState += 1

class UndoManagerSpec extends AnyWordSpec {
  "A UndoManager" when {
    "new" should {
      val undoManager = new UndoManager()
      "Not be able to undo a Step" in {
        val command = testCommand()
        undoManager.undoStep()
        command.testState should be(0)
      }
      "Not be able to redo a Step" in {
        val command = testCommand()
        undoManager.redoStep()
        command.testState should be(0)
      }
      "be able to do a Step" in {
        val command = testCommand()
        undoManager.doStep(command)
        command.testState should be(1)
      }
      "be able to undo a Step" in {
        val command = testCommand()
        undoManager.doStep(command)
        command.testState should be(1)
        undoManager.undoStep()
        command.testState should be(0)
      }
      "be able to redo a Step" in {
        val command = testCommand()
        undoManager.doStep(command)
        command.testState should be(1)
        undoManager.undoStep()
        command.testState should be(0)
        undoManager.redoStep()
        command.testState should be(1)
      }
    }
  }
}
