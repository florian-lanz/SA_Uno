package de.htwg.se.uno

import com.google.inject.{Guice, Injector}
import de.htwg.se.uno.aview.Tui
import de.htwg.se.uno.aview.gui.SwingGui
import de.htwg.se.uno.controller.controllerComponent.{ControllerInterface, GameSizeChanged}
import scala.io.StdIn.readLine

object Uno {
  val injector: Injector = Guice.createInjector(new UnoModule)
  val controller: ControllerInterface = injector.getInstance(classOf[ControllerInterface])
  val tui = new Tui(controller)
  val gui = new SwingGui(controller)
  controller.publish(new GameSizeChanged())

  def main(args: Array[String]): Unit = {
    var input: String = ""

    println(controller.gameToString)
    println(controller.controllerEvent("idle"))

    gui.open()

    input = readLine()
    val s = tui.processInputLine(input).get
    while (input != "q") {
      input = readLine()
      val s = tui.processInputLine(input).get
    }
  }
}
