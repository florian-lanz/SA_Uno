package model

import model.gameComponent.gameBaseImpl._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CardStackSpec extends AnyWordSpec with Matchers {
  "A CardStack" when {
    "new" should {
      val cardStack = CardStack()
      "have an empty list of cards" in {
        cardStack.cardStack should be (List())
      }
      "be able to create a covered card stack" in {
        cardStack.createCoveredCardStack().cardStack.length should be (108)
      }
      "be able to get shuffled" in {
        cardStack.createCoveredCardStack().shuffle().cardStack should not be cardStack.cardStack
      }
    }
  }
}
