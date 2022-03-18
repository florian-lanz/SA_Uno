package de.htwg.se.uno.aview

import de.htwg.se.uno.controller.controllerComponent.{ChooseColor, ControllerInterface, GameChanged, GameEnded, GameNotChanged, GameSizeChanged}

import scala.swing.Reactor
import scala.util.{Failure, Success, Try}


class Tui(controller: ControllerInterface) extends Reactor {
  listenTo(controller)

  def processInputLine(input: String): Try[String] = {
    val wf:Array[String] = input.split("[^a-z^A-Z^ß^ä^ö^ü^Ä^Ö^Ü^0-9/+]+")
    wf(0) match {
      case "q" => {
      System.exit(0)
      Success("Valid Command: " + input)
      }
      case "n" => {
        if (wf.length == 2) {
          controller.createGame(wf(1).toInt)
          Success("Valid Command: " + input)
        } else {
          controller.createGame()
          Success("Valid Command: " + input)
        }
      }
      case "t" => {
        controller.createTestGame()
        Success("Valid Command: " + input)
      }
      case "s" => {
        if (input.length() > 5) {
          if(input.substring(6).equals("blue")) {
            controller.set(input.substring(2, 5), 1)
          } else if (input.substring(6).equals("green")) {
            controller.set(input.substring(2, 5), 2)
          } else if (input.substring(6).equals("yellow")) {
            controller.set(input.substring(2, 5), 3)
          } else {
            controller.set(input.substring(2, 5), 4)
          }
        } else {
          controller.set(input.substring(2))
        }
        Success("valid command: " + input)
      }
      case "g" => {
        controller.get()
        Success("Valid Command: " + input)
      }
      case "r" => {
        controller.redo()
        Success("Valid Command: " + input)
      }
      case "u" => {
        controller.undo()
        Success("Valid Command: " + input)
      }
      case "sv" => {
        controller.save()
        Success("Valid Command: " + input)
      }
      case "ld" => {
        controller.load()
        Success("Valid Command: " + input)
      }
      case "d" => {
        if (controller.nextTurn()) {
          controller.controllerEvent("yourTurn")
          controller.publish(new GameNotChanged)
        } else {
          controller.enemy()
        }
        Success("Valid Command: " + input)
      }
      case _ => controller.controllerEvent("unknownCommand")
        Success("Unknown Command: " + input)
        //Failure(new IllegalArgumentException("Wrong input: " + input))
    }
  }

  reactions += {
      case a: GameSizeChanged => printTui
      case b: GameChanged => printTui
      case c: GameNotChanged => println(controller.controllerEvent("idle"))
      case d: ChooseColor => println(controller.controllerEvent("chooseColor"))
      case e: GameEnded => {
        println(controller.controllerEvent("idle"))
        println("Starte neues Spiel oder beende")
      }
  }

  def printTui: Unit = {
    println(controller.gameToString)
    println(controller.controllerEvent("idle"))
  }
}
