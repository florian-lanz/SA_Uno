package de.htwg.se.uno.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.uno.util.Command

class EnemyCommand3(controller: Controller) extends Command {
  override def doStep: Unit = {
    controller.game = controller.game.enemy3()
  }

  override def undoStep: Unit = {
    controller.game = controller.game.enemyUndo3()
  }

  override def redoStep: Unit = {
    controller.game = controller.game.enemy3()
  }
}