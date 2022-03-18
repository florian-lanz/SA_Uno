package de.htwg.se.uno.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.uno.util.Command

class ShuffleCommand(controller: Controller) extends Command  {
  override def doStep: Unit = {
    controller.game = controller.game.shuffle()
  }

  override def undoStep: Unit = {
    controller.game = controller.game.unshuffle()
  }

  override def redoStep: Unit = {
    controller.game = controller.game.reshuffle()
  }
}