package de.htwg.se.uno

import com.google.inject.AbstractModule
import com.google.inject.name.Names
import de.htwg.se.uno.controller.controllerComponent._
import de.htwg.se.uno.model.fileIoComponent.FileIOInterface
import de.htwg.se.uno.model.fileIoComponent._
import de.htwg.se.uno.model.gameComponent.gameBaseImpl.Game
import de.htwg.se.uno.model.gameComponent.GameInterface

class UnoModule extends AbstractModule {
  val defaultPlayers: Int = 2

  override def configure(): Unit = {
    bindConstant()
      .annotatedWith(Names.named("DefaultPlayers"))
      .to(defaultPlayers)
    bind(classOf[GameInterface]).to(classOf[Game])
    bind(classOf[ControllerInterface]).to(
      classOf[controllerBaseImpl.Controller]
    )

    bind(classOf[GameInterface])
      .annotatedWith(Names.named("2 Players"))
      .toInstance(Game(2).createGame())
    bind(classOf[GameInterface])
      .annotatedWith(Names.named("3 Players"))
      .toInstance(Game(3).createGame())
    bind(classOf[GameInterface])
      .annotatedWith(Names.named("4 Players"))
      .toInstance(Game(4).createGame())

    bind(classOf[FileIOInterface]).to(classOf[fileIoJsonImpl.FileIO])
    // bind(classOf[FileIOInterface]).to(classOf[fileIoXmlImpl.FileIO])
  }
}
