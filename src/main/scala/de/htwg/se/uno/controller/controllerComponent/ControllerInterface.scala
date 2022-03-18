package de.htwg.se.uno.controller.controllerComponent

import scala.swing.{Color, Publisher}

trait ControllerInterface extends Publisher {

  def createGame(size: Int = 7):Unit
  def createTestGame():Unit
  def gameToString: String
  def set(string:String, color : Int = 0): Unit
  def get(): Unit
  def enemy(): Unit
  def undo(): Unit
  def redo(): Unit
  def save(): Unit
  def load(): Unit
  def won(): Unit
  def getCardText(list : Int, index : Int) : String
  def getGuiCardText(list : Int, index : Int) : String
  def getLength(list : Int) : Int
  def controllerEvent(string : String) : String
  def getNumOfPlayers: Int
  def nextTurn() : Boolean
  def getHs2: String
  def nextEnemy() : Int
}


import scala.swing.event.Event

class GameChanged extends Event
class GameNotChanged extends Event
class GameSizeChanged extends Event
class GameEnded extends Event
class ChooseColor extends Event