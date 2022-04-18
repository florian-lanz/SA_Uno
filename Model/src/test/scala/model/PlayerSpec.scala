package model

import model.gameComponent.gameBaseImpl._
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec

class PlayerSpec extends AnyWordSpec {
  "A Player" when {
    "new" should {
      val greenNine = Card(Color.Green, Value.Nine)
      val redZero = Card(Color.Red, Value.Zero)
      val bluePlusTwo = Card(Color.Blue, Value.PlusTwo)
      val yellowPlusFour = Card(Color.Yellow, Value.PlusFour)
      val player = Player(List(greenNine))
      "Can not push a card" in {
        player.canPush(greenNine, bluePlusTwo, 2) should be (false)
        player.canPush(greenNine, yellowPlusFour, 4) should be (false)
        player.canPush(yellowPlusFour, greenNine, 0) should be (false)
        player.canPush(greenNine, redZero, 0) should be (false)
      }
      "Can push a card" in {
        player.canPush(greenNine, greenNine, 0) should be (true)
        player.canPush(yellowPlusFour, redZero, 0) should be (true)
      }
      "Should be able to push a card contained in the hand cards" in {
        player.pushCard(greenNine).handCards should be (List())
      }
      "Should not be able to push a card not contained in the hand cards" in {
        player.pushCard(redZero).handCards should be (List(greenNine))
      }
      "Should be able to pull a card" in {
        player.pullCard(redZero).handCards should be (List(redZero, greenNine))
      }
      "Should be able to get a card from his hand cards" in {
        player.findCard("G 9") should be (Some(greenNine))
      }
      "Should not be able to get a card not contained in his hand cards" in {
        player.findCard("R 9") should be (None)
      }
    }
  }
}