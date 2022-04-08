package de.htwg.se.uno.util

import de.htwg.se.uno.controller.controllerComponent.GameChanged
import de.htwg.se.uno.controller.controllerComponent.controllerBaseImpl.Controller

import scala.util.{Failure, Success}

trait Command(controller: Controller):
  def doStep(): Unit

  def undoStep(): Unit =
    controller.redoList = controller.fileIo.gameToString(controller.game) :: controller.redoList
    val result = controller.fileIo.load(controller.undoList.head)
    result match {
      case Success(value) => 
        controller.game = value
        controller.undoList = controller.undoList.tail
      case Failure(e) =>
        controller.redoList = controller.redoList.tail
        controller.controllerEvent("couldNotUndo")
        controller.publish(new GameChanged)
    }

  def redoStep(): Unit =
    controller.undoList = controller.fileIo.gameToString(controller.game) :: controller.undoList
    val result = controller.fileIo.load(controller.redoList.head)
    result match {
      case Success(value) => 
        controller.game = value
        controller.redoList = controller.redoList.tail
      case Failure(e) =>
        controller.undoList = controller.undoList.tail
        controller.controllerEvent("couldNotRedo")
        controller.publish(new GameChanged)
    }