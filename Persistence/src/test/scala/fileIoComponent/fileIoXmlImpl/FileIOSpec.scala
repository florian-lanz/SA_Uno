//package fileIoComponent.fileIoXmlImpl
//
//import model.gameComponent.GameInterface
//import org.scalatest.matchers.should.Matchers
//import org.scalatest.wordspec.AnyWordSpec
//import model.gameComponent.gameBaseImpl.*
//import play.api.libs.json.Json
//
//import scala.util.{Failure, Success}
//
//class FileIOSpec extends AnyWordSpec with Matchers {
//  "A FileIO" when {
//    "new" should {
//      val redZero = Card(Color.Red, Value.Zero)
//      val blueOne = Card(Color.Blue, Value.One)
//      val greenTwo = Card(Color.Green, Value.Two)
//      val yellowThree = Card(Color.Yellow, Value.Three)
//      val oldGameTwoPlayers = Game(
//        numOfPlayers = 2,
//        coveredCards = List(redZero, blueOne),
//        revealedCards = List(blueOne, greenTwo),
//        player = Player(List(greenTwo, yellowThree)),
//        enemies = List(Enemy(List(yellowThree, redZero)), Enemy(), Enemy()),
//        revealedCardEffect = 4,
//        direction = false,
//        alreadyPulled = true
//      )
//      val oldGameThreePlayers = oldGameTwoPlayers.copy(numOfPlayers = 3)
//      val oldGameFourPlayers = oldGameTwoPlayers.copy(numOfPlayers = 4)
//      val fileIO = FileIO()
//      "be able to save and load a game with two players" in {
//        fileIO.save(oldGameTwoPlayers)
//        var game = Game(4).createGame()
//        fileIO.load() match
//          case Success(value) => 
//            game = value.asInstanceOf[Game]
//            game.numOfPlayers should be (oldGameTwoPlayers.numOfPlayers)
//            game.coveredCards should be (oldGameTwoPlayers.coveredCards)
//            game.revealedCards should be (oldGameTwoPlayers.revealedCards)
//            game.player should be (oldGameTwoPlayers.player)
//            game.enemies should be (oldGameTwoPlayers.enemies)
//            game.revealedCardEffect should be (oldGameTwoPlayers.revealedCardEffect)
//            game.direction should be (oldGameTwoPlayers.direction)
//            game.activePlayer should be (oldGameTwoPlayers.activePlayer)
//            game.direction should be (oldGameTwoPlayers.direction)
//            game.alreadyPulled should be (oldGameTwoPlayers.alreadyPulled)
//          case Failure(e) => 1 should be (2)
//      }
//      "be able to save and load a game with three players" in {
//        fileIO.save(oldGameThreePlayers)
//        var game = Game(4).createGame()
//        fileIO.load() match
//          case Success(value) =>
//            game = value.asInstanceOf[Game]
//            game.numOfPlayers should be (oldGameThreePlayers.numOfPlayers)
//            game.coveredCards should be (oldGameThreePlayers.coveredCards)
//            game.revealedCards should be (oldGameThreePlayers.revealedCards)
//            game.player should be (oldGameThreePlayers.player)
//            game.enemies should be (oldGameThreePlayers.enemies)
//            game.revealedCardEffect should be (oldGameThreePlayers.revealedCardEffect)
//            game.direction should be (oldGameThreePlayers.direction)
//            game.activePlayer should be (oldGameThreePlayers.activePlayer)
//            game.direction should be (oldGameThreePlayers.direction)
//            game.alreadyPulled should be (oldGameThreePlayers.alreadyPulled)
//          case Failure(e) => 1 should be (2)
//      }
//      "be able to save and load a game with four players" in {
//        fileIO.save(oldGameFourPlayers)
//        var game = Game(2).createGame()
//        fileIO.load() match
//          case Success(value) =>
//            game = value.asInstanceOf[Game]
//            game.numOfPlayers should be (oldGameFourPlayers.numOfPlayers)
//            game.coveredCards should be (oldGameFourPlayers.coveredCards)
//            game.revealedCards should be (oldGameFourPlayers.revealedCards)
//            game.player should be (oldGameFourPlayers.player)
//            game.enemies should be (oldGameFourPlayers.enemies)
//            game.revealedCardEffect should be (oldGameFourPlayers.revealedCardEffect)
//            game.direction should be (oldGameFourPlayers.direction)
//            game.activePlayer should be (oldGameFourPlayers.activePlayer)
//            game.direction should be (oldGameFourPlayers.direction)
//            game.alreadyPulled should be (oldGameFourPlayers.alreadyPulled)
//          case Failure(e) => 1 should be (2)
//      }
////      "have a gameToJson function that does nothing" in {
////        fileIO.gameToJson(oldGameTwoPlayers) should be (Json.obj())
////      }
//    }
//  }
//}