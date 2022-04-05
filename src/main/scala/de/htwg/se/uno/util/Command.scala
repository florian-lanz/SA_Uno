package de.htwg.se.uno.util

import de.htwg.se.uno.controller.controllerComponent.controllerBaseImpl.Controller

trait Command(controller: Controller):
  def doStep(): Unit

  def undoStep(): Unit =
    controller.redoList = controller.fileIo.gameToJson(controller.game).toString :: controller.redoList
    controller.game = controller.fileIo.load(controller.undoList.head)
    controller.undoList = controller.undoList.tail

  def redoStep(): Unit =
    controller.undoList = controller.fileIo.gameToString(controller.game) :: controller.undoList
    controller.game = controller.fileIo.load(controller.redoList.head)
    controller.redoList = controller.redoList.tail