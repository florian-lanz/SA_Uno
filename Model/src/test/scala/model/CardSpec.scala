package model

import model.gameComponent.gameBaseImpl.{Card, Color, Value}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CardSpec extends AnyWordSpec with Matchers {
  "A Card" when {
    "new" should {
      val card = Card(Color.Blue, Value.Zero)
      "have color Blue" in {
        card.color should be (Color.Blue)
      }
      "have value Zero" in {
        card.value should be (Value.Zero)
      }
      "have a nice String representation" in{
        card.toString should be ("B 0")
      }
      "Have a nice GuiString representation for a Card" in {
        card.toGuiString should be (" 0 ")
      }
    }
  }
}

