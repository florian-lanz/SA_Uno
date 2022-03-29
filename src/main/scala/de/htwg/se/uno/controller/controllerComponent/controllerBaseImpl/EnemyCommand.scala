package de.htwg.se.uno.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.uno.util.Command

class EnemyCommand(controller: Controller) extends Command {
  override def doStep: Unit = {
    controller.undoList = controller.fileIo.gameToJson(controller.game).toString :: controller.undoList
    controller.game = controller.game.enemy()
  }

  override def undoStep: Unit = {
    controller.redoList = controller.fileIo.gameToJson(controller.game).toString :: controller.redoList
    controller.game = controller.fileIo.load(controller.undoList.head)
    controller.undoList = controller.undoList.tail
  }

  override def redoStep: Unit = {
    controller.undoList = controller.fileIo.gameToJson(controller.game).toString :: controller.undoList
    controller.game = controller.fileIo.load(controller.redoList.head)
    controller.redoList = controller.redoList.tail
  }
}