package de.htwg.se.uno

import com.google.inject.{Guice, Injector}
import de.htwg.se.uno.aview.{RestService, Tui}
import de.htwg.se.uno.aview.gui.SwingGui
import de.htwg.se.uno.controller.controllerComponent.{ControllerInterface, GameSizeChanged}
import de.htwg.se.uno.controller.controllerComponent.controllerBaseImpl._
import scala.io.StdIn.readLine

@main def main(): Unit =
  val injector: Injector = Guice.createInjector(new UnoModule)
  val controller: ControllerInterface = Controller()
  controller.createGame(2)

  val restService = RestService(controller)
  val server = restService.start()
  val tui = new Tui(controller)
  val gui = new SwingGui(controller)
  gui.open()

  var input: String = ""
  if Console.in.ready() then
    input = readLine()
    tui.processInputLine(input)
  else
    input = ""
  while input != "q" do
    if Console.in.ready() then
      input = readLine()
      tui.processInputLine(input)
    else
      input = ""
  restService.stop(server)