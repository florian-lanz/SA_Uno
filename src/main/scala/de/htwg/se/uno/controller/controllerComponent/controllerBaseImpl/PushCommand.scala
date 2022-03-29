package de.htwg.se.uno.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.uno.util.Command

class PushCommand(string: String, color : Int, controller: Controller) extends Command  {
  override def doStep: Unit = {
    controller.undoList = controller.fileIo.gameToJson(controller.game).toString :: controller.undoList
    controller.game = controller.game.pushMove(string, color)
  }

  override def undoStep: Unit = {
    controller.game = controller.fileIo.load(controller.undoList.head)
    controller.undoList = controller.undoList.tail
  }

  override def redoStep: Unit = {
    controller.undoList = controller.fileIo.gameToJson(controller.game).toString :: controller.undoList
    controller.game = controller.fileIo.load(controller.redoList.head)
    controller.redoList = controller.redoList.tail
  }
}