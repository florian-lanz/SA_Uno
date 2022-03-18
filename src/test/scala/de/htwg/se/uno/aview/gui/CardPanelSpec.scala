package de.htwg.se.uno.aview.gui

import de.htwg.se.uno.controller.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.uno.model.gameComponent.gameBaseImpl.Game
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.swing.Color

class CardPanelSpec extends AnyWordSpec with Matchers {
  "A CardPanel" when {
    val controller = new Controller(Game(4))
    "created" should {
      controller.createTestGame()
      var cardPanel = new CardPanel(4, 0, controller)
      "have a card Text" in {
        cardPanel.cardText(4, 0) should be(controller.getCardText(4, 0))
      }
      "Have a Card Text per Color" in {
        cardPanel.cardTextColor(4, 0) should be(controller.getCardText(4,0).charAt(0))
      }
      "Have a Color per Card" in {
        cardPanel.cardColor(4,0) should be(new Color(128, 128, 128))
      }
      "Have a second Color per Card" in {
        cardPanel.cardColor(4,2) should be(new Color(255, 0, 0))
      }
      "Have a third Color per Card" in {
        cardPanel.cardColor(4, 4) should be(new Color(255,255,0))
      }
      "Have a fourth Color per Card" in {
        cardPanel.cardColor(3,2) should be(new Color(255,255,255))
      }
      "Have a fifth Color per Card" in {
        cardPanel.cardColor(4,5) should be(new Color(0,255,0))
      }
      "Have a sixth Color per Card" in {
        controller.set("S C", 1)
        cardPanel.cardColor(3, 1) should be(new Color(0,0,255))
      }
      "Have a seventh Color per Card" in {
        cardPanel.cardColor(3, 0) should be(new Color(0,0,0))
      }
      "Have another type of Cards" in {
        cardPanel = new CardPanel(3, 0,controller)
        cardPanel.cardColor(3,0) should be(new Color(0,0,0))
      }
      "Have a third type of Card" in {
        cardPanel = new CardPanel(3, 2,controller)
        cardPanel.cardColor(3,2) should be(new Color(255,255,255))
      }
    }
  }
}
