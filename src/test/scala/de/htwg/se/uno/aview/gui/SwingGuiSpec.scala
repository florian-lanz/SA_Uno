//package de.htwg.se.uno.aview.gui
//
//import de.htwg.se.uno.controller.controllerComponent.controllerBaseImpl.Controller
//import de.htwg.se.uno.controller.controllerComponent.{ChooseColor, GameChanged, GameEnded}
//import model.gameComponent.gameBaseImpl._
//import org.scalatest.matchers.should.Matchers
//import org.scalatest.wordspec.AnyWordSpec
//
//class SwingGuiSpec extends AnyWordSpec with Matchers {
//  "A SwingGui" when {
//    var controller = new Controller(Game(4))
//    "created" should {
//      controller.createGame(4)
//      val redOne = Card(Color.Red, Value.One)
//      var swingGui = new SwingGui(controller)
//      "Have a title" in {
//        swingGui.title should be("HTWG Uno")
//        swingGui.variableForTests should be(7)
//      }
//      "Change if the activePlayer changes" in {
//        controller.game = controller.game.copyGame(player = Player(redOne :: controller.game.player.handCards), revealedCards = redOne :: controller.game.revealedCards)
//        controller.set("R 1")
//        swingGui.variableForTests should be(4)
//      }
//      "Change if the activePlayer changes a second time" in {
//        controller.game = controller.game.copyGame(enemies = controller.game.enemies.updated(0, Enemy(redOne :: controller.game.enemies.head.enemyCards)), revealedCards = redOne :: controller.game.revealedCards)
//        controller.enemy()
//        swingGui.variableForTests should be(5)
//      }
//      "Change if the activePlayer changes a third time" in {
//        controller.game = controller.game.copyGame(enemies = controller.game.enemies.updated(1, Enemy(redOne :: controller.game.enemies.head.enemyCards)), revealedCards = redOne :: controller.game.revealedCards)
//        controller.enemy()
//        swingGui.variableForTests should be(6)
//      }
//      "Change if the activePlayer changes a fourth time" in {
//        controller = new Controller(Game(3))
//        controller.createGame(3)
//        swingGui = new SwingGui(controller)
//        swingGui.variableForTests should be(3)
//      }
//      "Change if the activePlayer changes a fifth time" in {
//        controller.game = controller.game.changeActivePlayer()
//        controller.publish(new GameChanged)
//        swingGui.variableForTests should be(1)
//      }
//      "Change if the activePlayer changes a sixth time" in {
//        controller.game = controller.game.changeActivePlayer()
//        controller.publish(new GameChanged)
//        swingGui.variableForTests should be(2)
//      }
//      "Have a statusline" in {
//        swingGui.statusline.text should be(controller.controllerEvent("idle"))
//      }
//      "Should change if the Event changes to chooseColor" in {
//        controller.controllerEvent("chooseColor")
//        controller.publish(new ChooseColor)
//        swingGui.statusline.text should be(controller.controllerEvent("chooseColor"))
//      }
//      "Should change if the Event changes to GameEnded" in {
//        controller.controllerEvent("won")
//        controller.publish(new GameEnded)
//        swingGui.statusline.text should be(controller.controllerEvent("won"))
//      }
//      "Should change if the Event changes to GameChanged" in {
//        controller.controllerEvent("yourTurn")
//        controller.publish(new GameChanged)
//        swingGui.statusline.text should be(controller.controllerEvent("yourTurn"))
//      }
//    }
//  }
//}
