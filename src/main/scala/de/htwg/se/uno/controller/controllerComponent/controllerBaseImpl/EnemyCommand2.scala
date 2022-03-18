package de.htwg.se.uno.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.uno.util.Command

class EnemyCommand2(controller: Controller) extends Command {
  override def doStep: Unit = {
    controller.game = controller.game.enemy2()
  }

  override def undoStep: Unit = {
    controller.game = controller.game.enemyUndo2()
  }

  override def redoStep: Unit = {
    controller.game = controller.game.enemy2()
  }
}