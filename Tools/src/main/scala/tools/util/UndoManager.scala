package tools.util

import scala.util.Try

class UndoManager:
  private var undoStack: List[Command]= Nil
  private var redoStack: List[Command]= Nil

  def doStep(command: Command): Unit =
    undoStack = command::undoStack
    redoStack = Nil
    command.doStep()

  def undoStep(): Try[Unit] = Try {
    undoStack.head.undoStep()
    redoStack = undoStack.head :: redoStack
    undoStack = undoStack.tail
  }

  def redoStep(): Try[Unit] = Try {
    redoStack.head.redoStep()
    undoStack = redoStack.head :: undoStack
    redoStack = redoStack.tail
  }