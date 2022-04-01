package de.htwg.se.uno.model

import de.htwg.se.uno.model.gameComponent.gameBaseImpl._
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec

import scala.collection.mutable.ListBuffer

class EnemySpec extends AnyWordSpec {
  "A Enemy" when {
    "new" should {
      val greenNine = Card(Color.Green, Value.Nine)
      val greenSuspend = Card(Color.Green, Value.Suspend)
      val redZero = Card(Color.Red, Value.Zero)
      val redColorChange = Card(Color.Red, Value.ColorChange)
      val blueNine = Card(Color.Blue, Value.Nine)
      val bluePlusTwo = Card(Color.Blue, Value.PlusTwo)
      val yellowPlusFour = Card(Color.Yellow, Value.PlusFour)
      val yellowSuspend = Card(Color.Yellow, Value.Suspend)
      val specialPlusFour = Card(Color.Special, Value.PlusFour)
      val enemy = Enemy(List(greenNine))
      "be able to push a card from the enemy cards" in {
        enemy.pushCard(greenNine).enemyCards should be (List())
      }
      "not be able to push a card not contained in the enemy cards" in {
        enemy.pushCard(redZero).enemyCards should be (List(greenNine))
      }
      "be able to pull a card" in {
        enemy.pullCard(redZero).enemyCards should be (List(redZero, greenNine))
      }
      "be able to push a draw card" in {
        enemy.shouldPushDrawCard(bluePlusTwo, bluePlusTwo, 2) should be (true)
      }
      "not be able to push a draw card" in {
        enemy.shouldPushDrawCard(bluePlusTwo, greenNine, 0) should be (false)
      }
      "be able to push a basic card with the same color" in {
        enemy.canPushBasicCardWithSameColor(greenNine, greenNine, 0) should be (true)
      }
      "not be able to push a basic card with the same color" in {
        enemy.canPushBasicCardWithSameColor(blueNine, bluePlusTwo, 2) should be (false)
      }
      "be able to push a suspension or direction change card with the same color" in {
        enemy.canPushSuspendOrDirectionChangeWithSameColor(greenSuspend, greenNine, 0) should be (true)
      }
      "not be able to push a suspension or direction change card with the same color" in {
        enemy.canPushSuspendOrDirectionChangeWithSameColor(greenSuspend, redZero, 0) should be (false)
      }
      "be able to push a basic card with the same value" in {
        enemy.canPushBasicCardWithSameValue(blueNine, greenNine, 0) should be (true)
      }
      "not be able to push a basic card with the same value" in {
        enemy.canPushBasicCardWithSameValue(bluePlusTwo, blueNine, 0) should be (false)
      }
      "be able to push a suspension or direction change card with the same value" in {
        enemy.canPushSuspendOrDirectionChangeWithSameValue(greenSuspend, yellowSuspend, 0) should be (true)
      }
      "not be able to push a suspension or direction change card with the same value" in {
        enemy.canPushSuspendOrDirectionChangeWithSameValue(greenSuspend, redZero, 0) should be (false)
      }
      "be able to push a color change card" in {
        enemy.canPushColorChange(redColorChange, greenNine, 0) should be (true)
      }
      "not be able to push a color change card" in {
        enemy.canPushColorChange(redColorChange, yellowPlusFour, 4) should be (false)
      }
      "be able to push on a special card" in {
        enemy.canPushOnSpecial(greenNine, specialPlusFour, 4) should be (true)
      }
      "not be able to push on a special card" in {
        enemy.canPushOnSpecial(greenNine, yellowPlusFour, 4) should be (false)
      }
      "be able to push a plus two card" in {
        enemy.canPushPlusTwo(bluePlusTwo, blueNine, 0) should be (true)
      }
      "not be able to push a plus two card" in {
        enemy.canPushPlusTwo(bluePlusTwo, yellowPlusFour, 4) should be (false)
      }
      "be able to push a plus four card" in {
        enemy.canPushPlusFour(yellowPlusFour, blueNine, 0) should be (true)
      }
      "not be able to push a plus four card" in {
        enemy.canPushPlusFour(yellowPlusFour, greenNine, 0) should be (false)
        enemy.canPushPlusFour(yellowPlusFour, bluePlusTwo, 2) should be (false)
      }
    }
  }
}
