package de.htwg.se.uno.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.uno.util.Command

class EnemyCommand(controller: Controller, enemyIndex: Int) extends Command(controller):
  override def doStep(): Unit =
    controller.undoList = controller.fileIo.gameToJson(controller.game).toString :: controller.undoList
    println(controller.undoList)
    controller.game = controller.game.enemy(enemyIndex)