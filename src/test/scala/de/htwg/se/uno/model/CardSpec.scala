package de.htwg.se.uno.model

import de.htwg.se.uno.model.gameComponent.gameBaseImpl.{Card, Color, Value}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CardSpec extends AnyWordSpec with Matchers {

  "A Card" when {
    "new" should {
      var newCard = Card(Color.Blue, Value.Zero)
      "have color Blue" in {
        newCard.color should be(Color.Blue)
      }
      "have value Zero" in {
        newCard.value should be(Value.Zero)
      }
      "have a nice String representation" in{
        newCard.toString should be("B 0")
      }
      "Have a nice GuiString representation for a Card" in {
        newCard = Card(Color.Blue, Value.Seven)
        newCard.toGuiString should be(" 7 ")
      }
      "Have a nice GuiString representation for another Card" in {
        newCard = Card(Color.Blue, Value.Eight)
        newCard.toGuiString should be(" 8 ")
      }
      "Have a nice GuiString representation for a third Card" in {
        newCard = Card(Color.Blue, Value.Nine)
        newCard.toGuiString should be(" 9 ")
      }
      "Have a nice GuiString representation for a fourth Card" in {
        newCard = Card(Color.Blue, Value.Five)
        newCard.toGuiString should be(" 5 ")
      }
    }
  }
}

