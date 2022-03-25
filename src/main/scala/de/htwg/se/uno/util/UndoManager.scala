package de.htwg.se.uno.util

class UndoManager {
  private var undoStack: List[Command]= Nil
  private var redoStack: List[Command]= Nil
  def doStep(command: Command) = {
    undoStack = command::undoStack
    redoStack = Nil
    command.doStep
  }
  def undoStep(): Boolean = {
    undoStack match {
      case Nil => false
      case head::stack =>
        head.undoStep
        undoStack=stack
        redoStack= head::redoStack
        true
    }
  }
  def redoStep = {
    redoStack match {
      case Nil => 
      case head::stack => {
        head.redoStep
        redoStack=stack
        undoStack=head::undoStack
      }
    }
  }
}
