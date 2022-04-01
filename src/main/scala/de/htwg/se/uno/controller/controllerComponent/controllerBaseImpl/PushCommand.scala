package de.htwg.se.uno.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.uno.util.Command

class PushCommand(string: String, color: Int, controller: Controller) extends Command(controller):
  override def doStep(): Unit =
    controller.undoList = controller.fileIo.gameToJson(controller.game).toString :: controller.undoList
    println(controller.undoList)
    controller.game = controller.game.pushMove(string, color)