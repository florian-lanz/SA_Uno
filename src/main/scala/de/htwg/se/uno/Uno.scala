package de.htwg.se.uno

import com.google.inject.{Guice, Injector}
import de.htwg.se.uno.aview.Tui
import de.htwg.se.uno.aview.gui.SwingGui
import de.htwg.se.uno.controller.controllerComponent.{
  ControllerInterface,
  GameSizeChanged
}
import scala.io.StdIn.readLine

@main def main(): Unit =
  val injector: Injector = Guice.createInjector(new UnoModule)
  val controller: ControllerInterface =
    injector.getInstance(classOf[ControllerInterface])
  val tui = new Tui(controller)
  val gui = new SwingGui(controller)
  controller.publish(new GameSizeChanged())

  println(controller.gameToString)
  println(controller.controllerEvent("idle"))

  gui.open()

  var input: String = ""
  input = readLine()
  tui.processInputLine(input)
  while input != "q" do
    input = readLine()
    tui.processInputLine(input)
