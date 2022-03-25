package de.htwg.se.uno.model

import de.htwg.se.uno.model.gameComponent.gameBaseImpl._
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec

import scala.collection.mutable.ListBuffer

class EnemySpec extends AnyWordSpec {
  "A Enemy" when {
    "new" should {
      var newGame = Game(4)
      newGame.init = InitializeGameStrategy(1)
      newGame.init = newGame.init.initializeGame(4)
      "Be able to do the enemys run" in {
        newGame = newGame.createTestGame()
        newGame.init.player.handCards = newGame.init.player.handCards.drop(8)
        newGame.activePlayer = 3
        newGame.init.enemy3.enemy(newGame) should be (newGame.init.enemy3)
      }
      "Be able to do the enemy's run again" in {
        newGame = newGame.createTestGame()
        newGame.init.enemy2.enemyCards = newGame.init.enemy2.enemyCards.drop(8)
        newGame.activePlayer = 1
        newGame.init.enemy.enemy(newGame) should be (newGame.init.enemy)
      }
      "Be able to do the enemy's run a third time" in {
        newGame = newGame.createTestGame()
        newGame.pushMove("R+2", 0)
        newGame.activePlayer = 1
        newGame.init.enemy.enemy(newGame) should be (newGame.init.enemy)
      }
      "Be able to do the enemy's run a fourth time" in {
        newGame = newGame.createTestGame()
        newGame.activePlayer = 1
        newGame.init.enemy.enemy(newGame) should be (newGame.init.enemy)
      }
      "Be able to do the enemy's run a fifth time" in {
        newGame = newGame.createTestGame()
        newGame.init.enemy.enemyCards = newGame.init.enemy.enemyCards.take(2) ++ newGame.init.enemy.enemyCards.drop(4)
        newGame.activePlayer = 1
        newGame.init.enemy.enemy(newGame) should be (newGame.init.enemy)
      }
      "Be able to do the enemy's run a sixth time" in {
        newGame = newGame.createTestGame()
        newGame.init.cardsRevealed = Card(Color.Blue, Value.Four) +: newGame.init.cardsRevealed
        newGame.activePlayer = 1
        newGame.init.enemy.enemy(newGame) should be (newGame.init.enemy)
      }
      "Be able to do the enemy's run a seventh time" in {
        newGame = newGame.createTestGame()
        newGame.init.cardsRevealed = Card(Color.Blue, Value.DirectionChange) +: newGame.init.cardsRevealed
        newGame.activePlayer = 1
        newGame.init.enemy.enemy(newGame) should be (newGame.init.enemy)
      }
      "Be able to do the enemy's run a eigth time" in{
        newGame = newGame.createTestGame()
        newGame.init.enemy.enemyCards = Card(Color.Special, Value.ColorChange) +: newGame.init.enemy.enemyCards.drop(9)
        newGame.activePlayer = 1
        newGame.init.enemy.enemy(newGame) should be (newGame.init.enemy)
      }
      "Be able to do the enemy's run a nineth time" in{
        newGame = newGame.createTestGame()
        newGame.init.enemy.enemyCards = newGame.init.enemy.enemyCards.drop(3)
        newGame.init.cardsRevealed = Card(Color.Blue, Value.Five) +: newGame.init.cardsRevealed
        newGame.activePlayer = 1
        newGame.init.enemy.enemy(newGame) should be (newGame.init.enemy)
      }
      "Be able to do the enemy's run a tenth time" in{
        newGame = newGame.createTestGame()
        newGame.init.cardsRevealed = Card(Color.Special, Value.ColorChange) +: newGame.init.cardsRevealed
        newGame.init.enemy.enemyCards = newGame.init.enemy.enemyCards.drop(7)
        newGame.activePlayer = 1
        newGame.init.enemy.enemy(newGame) should be (newGame.init.enemy)
      }
      "Be able to do the enemy's run a eleventh time" in{
        newGame = newGame.createTestGame()
        newGame.init.enemy.enemyCards = newGame.init.enemy.enemyCards.drop(8)
        newGame.activePlayer = 1
        newGame.init.enemy.enemy(newGame) should be (newGame.init.enemy)
      }
      "Be able to do the enemy's run a twelth time" in{
        newGame = newGame.createTestGame()
        newGame.init.enemy.enemyCards = newGame.init.enemy.enemyCards.take(1)
        newGame.activePlayer = 1
        newGame.init.enemy.enemy(newGame) should be (newGame.init.enemy)
      }
      "Be able to do the enemy's run a thirteenth time" in{
        newGame = newGame.createTestGame()
        newGame.init.cardsRevealed = Card(Color.Blue, Value.Five) +: newGame.init.cardsRevealed
        newGame.init.enemy.enemyCards = newGame.init.enemy.enemyCards.drop(7)
        newGame.activePlayer = 1
        newGame.init.enemy.enemy(newGame) should be (newGame.init.enemy)
      }
      "Be able to do the enemy's run a fourteenth time" in {
        newGame = newGame.createTestGame()
        newGame.init.cardsRevealed = Card(Color.Blue, Value.Five) +: newGame.init.cardsRevealed
        newGame.init.enemy.enemyCards = newGame.init.enemy.enemyCards.drop(7)
        newGame.activePlayer = 1
        newGame.anotherPull = true
        newGame.init.enemy.enemy(newGame) should be(newGame.init.enemy)
      }

      "Be able to push a Card of the Enemy's Cards" in {
        newGame = newGame.createTestGame()
        newGame.init.enemy.enemyCards = Card(Color.Yellow, Value.Six) +: newGame.init.enemy.enemyCards.take(2) :++
          Card(Color.Yellow, Value.Seven) +: newGame.init.enemy.enemyCards.drop(2) :++ Card(Color.Green, Value.Seven) +:
          ListBuffer[Card](Card(Color.Blue, Value.Seven))
        newGame.init.enemy.pushCardEnemy(Card(Color.Special, Value.ColorChange), newGame) should be(newGame.init.enemy)
      }
      "Be able to push another Card of the Enemy's Cards" in {
        newGame = newGame.createTestGame()
        newGame.init.enemy.pushCardEnemy(Card(Color.Red, Value.PlusTwo), newGame) should be(newGame.init.enemy)
      }
      "Be able to push a third Card of the Enemy's Cards" in {
        newGame = newGame.createTestGame()
        newGame.init.enemy.pushCardEnemy(Card(Color.Red, Value.Suspend), newGame) should be(newGame.init.enemy)
      }
      "Be able to push a fourth Card of the Enemy's Cards" in {
        newGame = newGame.createTestGame()
        newGame.init.enemy.pushCardEnemy(Card(Color.Red, Value.DirectionChange), newGame) should be(newGame.init.enemy)
      }
      "Be able to push a fifth Card of the Enemy's Cards" in {
        newGame = newGame.createTestGame()
        newGame.init.enemy.pushCardEnemy(Card(Color.Special, Value.PlusFour), newGame) should be(newGame.init.enemy)
      }

      "Be able to pull a Card" in {
        newGame = newGame.createTestGame()
        newGame.init.enemy.pullEnemy(newGame) should be(newGame.init.enemy)
      }
      "Be able to pull another Card" in {
        newGame = newGame.createTestGame()
        newGame.special.push(4)
        newGame.init.enemy.pullEnemy(newGame) should be(newGame.init.enemy)
      }

      "Should have a ki" in {
        newGame = newGame.createTestGame()
        newGame.init.enemy.enemyCards = newGame.init.enemy.enemyCards.take(2)
        newGame.init.enemy.ki(newGame) should be(newGame.init.enemy)
      }
      "Should have a second ki" in {
        newGame = newGame.createTestGame()
        newGame.init.enemy.ki(newGame) should be(newGame.init.enemy)
      }
      "Should have a third ki" in {
        newGame = newGame.createTestGame()
        newGame.init.enemy.enemyCards = newGame.init.enemy.enemyCards.take(8)
        newGame.init.enemy.ki(newGame) should be(newGame.init.enemy)
      }
    }
  }
}
