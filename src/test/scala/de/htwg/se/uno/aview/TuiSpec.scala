//package de.htwg.se.uno.aview
//
//import de.htwg.se.uno.controller.controllerComponent.GameEnded
//import de.htwg.se.uno.controller.controllerComponent.controllerBaseImpl.Controller
//import model.gameComponent.gameBaseImpl._
//import org.scalatest.matchers.should.Matchers
//import org.scalatest.wordspec.AnyWordSpec
//
//class TuiSpec extends AnyWordSpec with Matchers {
//
//  "A Game Tui" should {
//    val redOne = Card(Color.Red, Value.One)
//    val colorChange = Card(Color.Special, Value.ColorChange)
//    val controller = new Controller(Game(2))
//    val tui = new Tui(controller)
//    "create a Game on input 'n'" in {
//      tui.processInputLine("n")
//      controller.getNumOfPlayers should be(2)
//    }
//    "create a Game on input 'n 3'" in {
//      tui.processInputLine("n 3")
//      controller.getNumOfPlayers should be(3)
//    }
//    /* Geht nicht mehr, da sonst Tests beendet werden.
//    "Do nothing on input 'q" in{
//      tui.processInputLine("q")
//    }
//     */
//    "Only print on bad input like 'Something'" in {
//      val old = controller.gameToString
//      tui.processInputLine("Something")
//      controller.gameToString should be(old)
//    }
//    "Not do anything on input d if it's your turn" in {
//      tui.processInputLine("d")
//      controller.controllerEvent("idle") should be(controller.controllerEvent("yourTurn"))
//    }
//    "Not set a card on input 's S...' without color" in {
//      val old = controller.gameToString
//      tui.processInputLine("s S+4")
//      controller.gameToString should be(old)
//    }
//    "Set a Card on input s [Karte]" in {
//      controller.game = controller.game.copyGame(player = Player(redOne :: controller.game.player.handCards), revealedCards = redOne :: controller.game.revealedCards)
//      tui.processInputLine("s R 1")
//      controller.nextTurn() should be (false)
//    }
//    "Do the Enemys Run" in {
//      controller.game = controller.game.copyGame(enemies = List(Enemy(redOne :: controller.game.enemies.head.enemyCards),
//        Enemy(redOne :: controller.game.enemies(1).enemyCards), Enemy(redOne :: controller.game.enemies(2).enemyCards)),
//        revealedCards = redOne :: controller.game.revealedCards)
//      tui.processInputLine("d")
//      tui.processInputLine("d")
//      tui.processInputLine("d")
//      controller.nextTurn() should be(true)
//    }
//    "Set another Card on input s [Karte]" in {
//      controller.game = controller.game.copyGame(player = Player(colorChange :: controller.game.player.handCards), revealedCards = redOne :: controller.game.revealedCards, revealedCardEffect = 0)
//      tui.processInputLine("s S C blue")
//      controller.nextTurn() should be (false)
//    }
//    "Undo a Step" in {
//      tui.processInputLine("u")
//      controller.nextTurn() should be(true)
//    }
//    "Set a third Card on input s [Karte]" in {
//      controller.game = controller.game.copyGame(player = Player(colorChange :: controller.game.player.handCards), revealedCards = redOne :: controller.game.revealedCards, revealedCardEffect = 0)
//      tui.processInputLine("s S C green")
//      controller.nextTurn() should be(false)
//    }
//    "Undo another Step" in {
//      tui.processInputLine("u")
//      controller.nextTurn() should be(true)
//    }
//    "Set a fourth Card on input s [Karte]" in {
//      controller.game = controller.game.copyGame(player = Player(colorChange :: controller.game.player.handCards), revealedCards = redOne :: controller.game.revealedCards, revealedCardEffect = 0)
//      tui.processInputLine("s S C yellow")
//      controller.nextTurn() should be(false)
//    }
//    "Undo a third Step" in {
//      tui.processInputLine("u")
//      controller.nextTurn() should be(true)
//    }
//    "Set a fifth Card on input s [Karte]" in {
//      controller.game = controller.game.copyGame(player = Player(colorChange :: controller.game.player.handCards), revealedCards = redOne :: controller.game.revealedCards, revealedCardEffect = 0)
//      tui.processInputLine("s S C red")
//      controller.nextTurn() should be(false)
//    }
//    "Undo a fourth Step" in {
//      tui.processInputLine("u")
//      controller.nextTurn() should be(true)
//    }
//    "Dont set a Card on wrong input" in{
//      val old = controller.gameToString
//      tui.processInputLine("s Hi")
//      controller.gameToString should be (old)
//    }
//    "Get a Card on input 'g'" in {
//      val old = controller.gameToString
//      tui.processInputLine("g")
//      controller.gameToString should not be old
//    }
//    "Undo a fifth Step" in {
//      tui.processInputLine("u")
//      controller.nextTurn() should be(true)
//    }
//    "Redo a Step on Input 'r' if possible" in {
//      val old = controller.gameToString
//      tui.processInputLine("g")
//      controller.gameToString should not be old
//
//
//      tui.processInputLine("u")
//      controller.nextTurn() should be(true)
//
//      val old2 = controller.gameToString
//      tui.processInputLine("r")
//      controller.gameToString should not be old2
//    }
//    "Save but not change anything on input 'sv'" in {
//      val old = controller.gameToString
//      tui.processInputLine("sv")
//      controller.gameToString should be (old)
//    }
//    "Load and change the game on input 'ld'" in {
//      controller.createGame(3)
//      val old = controller.gameToString
//      tui.processInputLine("ld")
//      controller.gameToString should not be old
//    }
//    "Do nothing special on Event GameEnded" in {
//      controller.publish(new GameEnded)
//    }
//  }
//}
