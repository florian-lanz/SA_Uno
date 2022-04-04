package de.htwg.se.uno.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.uno.util.Command

class PushCommand(string: String, color: Int, controller: Controller) extends Command(controller):
  override def doStep(): Unit =
    controller.game = controller.game.pushMove(string, color)