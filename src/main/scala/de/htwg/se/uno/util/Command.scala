package de.htwg.se.uno.util

import de.htwg.se.uno.controller.controllerComponent.controllerBaseImpl.Controller
import play.api.libs.json.Json

trait Command(controller: Controller):
  def doStep(): Unit

  def undoStep(): Unit = 
    controller.redoList = controller.gameJson.toString :: controller.redoList
    controller.gameJson = Json.parse(controller.undoList.head)
    controller.undoList = controller.undoList.tail

  def redoStep(): Unit =
    controller.undoList = controller.gameJson.toString :: controller.undoList
    controller.gameJson = Json.parse(controller.redoList.head)
    controller.redoList = controller.redoList.tail
