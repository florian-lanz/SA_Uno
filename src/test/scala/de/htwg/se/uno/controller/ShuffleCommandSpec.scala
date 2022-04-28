//package de.htwg.se.uno.controller
//
//import de.htwg.se.uno.controller.controllerComponent.controllerBaseImpl.{Controller, ShuffleCommand}
//import model.gameComponent.gameBaseImpl.{Card, Color, Enemy, Game, Player, Value}
//import org.scalatest.matchers.should.Matchers
//import org.scalatest.wordspec.AnyWordSpec
//
//class ShuffleCommandSpec extends AnyWordSpec with Matchers {
//  "A ShuffleCommand" when {
//    "new" should {
//      val redZero = Card(Color.Red, Value.Zero)
//      val blueOne = Card(Color.Blue, Value.One)
//      val greenTwo = Card(Color.Green, Value.Two)
//      val yellowThree = Card(Color.Yellow, Value.Three)
//      val game = Game(
//        numOfPlayers = 2,
//        coveredCards = List(redZero, blueOne),
//        revealedCards = List(redZero, yellowThree, greenTwo),
//        player = Player(List(redZero, greenTwo, yellowThree)),
//        enemies = List(Enemy(List(greenTwo)), Enemy(), Enemy()),
//      )
//      val controller = new Controller(game)
//      val command = new ShuffleCommand(controller)
//      "Be able to do a Step" in {
//        command.doStep()
//        controller.game.revealedCards should be (List(redZero))
//        controller.game.coveredCards.length should be (4)
//      }
//      "Be able to undo a Step" in {
//        controller.game = game
//        controller.undoList = controller.fileIo.gameToString(controller.game) :: controller.undoList
//        command.doStep()
//        controller.game.revealedCards should be (List(redZero))
//        controller.game.coveredCards.length should be (4)
//        command.undoStep()
//        controller.game.revealedCards should be (List(redZero, yellowThree, greenTwo))
//        controller.game.coveredCards should be (List(redZero, blueOne))
//      }
//      "Be able to redo a Step" in {
//        controller.game = game
//        controller.undoList = controller.fileIo.gameToString(controller.game) :: controller.undoList
//        command.doStep()
//        controller.game.revealedCards should be (List(redZero))
//        controller.game.coveredCards.length should be (4)
//        val shuffledCoveredCards = controller.game.coveredCards
//        command.undoStep()
//        controller.game.revealedCards should be (List(redZero, yellowThree, greenTwo))
//        controller.game.coveredCards should be (List(redZero, blueOne))
//        controller.game.coveredCards should not be (shuffledCoveredCards)
//        command.redoStep()
//        controller.game.revealedCards should be (List(redZero))
//        controller.game.coveredCards.length should be (4)
//        controller.game.coveredCards should be (shuffledCoveredCards)
//      }
//    }
//  }
//}
