package de.htwg.se.uno.controller

import de.htwg.se.uno.controller.controllerComponent.controllerBaseImpl.{Controller, EnemyCommand, PullCommand}
import de.htwg.se.uno.model.gameComponent.gameBaseImpl.{Card, Color, Enemy, Game, Player, Value}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class EnemyCommandSpec extends AnyWordSpec with Matchers {
  "A EnemyCommand" when {
    "new" should {
      val redZero = Card(Color.Red, Value.Zero)
      val blueOne = Card(Color.Blue, Value.One)
      val greenTwo = Card(Color.Green, Value.Two)
      val yellowThree = Card(Color.Yellow, Value.Three)
      val game = Game(
        numOfPlayers = 2,
        coveredCards = List(redZero, blueOne),
        revealedCards = List(redZero),
        player = Player(List(greenTwo, yellowThree)),
        enemies = List(Enemy(List(greenTwo)), Enemy(), Enemy()),
      )
      val controller = new Controller(game)
      val command = new EnemyCommand(controller, 0)
      "Be able to do a Step" in {
        command.doStep()
        controller.game.enemies.head.enemyCards should be (List(redZero, greenTwo))
      }
      "Be able to undo a Step" in {
        controller.game = game
        controller.undoList = controller.fileIo.gameToString(controller.game) :: controller.undoList
        command.doStep()
        controller.game.enemies.head.enemyCards should be (List(redZero, greenTwo))
        command.undoStep()
        controller.game.enemies.head.enemyCards should be (List(greenTwo))
      }
      "Be able to redo a Step" in {
        controller.game = game
        controller.undoList = controller.fileIo.gameToString(controller.game) :: controller.undoList
        command.doStep()
        controller.game.enemies.head.enemyCards should be (List(redZero, greenTwo))
        command.undoStep()
        controller.game.enemies.head.enemyCards should be (List(greenTwo))
        command.redoStep()
        controller.game.enemies.head.enemyCards should be (List(redZero, greenTwo))
      }
    }
  }
}
