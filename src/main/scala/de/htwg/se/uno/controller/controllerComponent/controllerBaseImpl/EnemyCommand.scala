package de.htwg.se.uno.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.uno.util.Command

class EnemyCommand(controller: Controller) extends Command {
  override def doStep: Unit = {
    controller.game = controller.game.enemy()
  }

  override def undoStep: Unit = {
    controller.game = controller.game.enemyUndo()
  }

  override def redoStep: Unit = {
    controller.game = controller.game.enemy()
  }
}