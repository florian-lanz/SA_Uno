package de.htwg.se.uno

import com.google.inject.AbstractModule
import com.google.inject.name.Names
import de.htwg.se.uno.controller.controllerComponent._
import de.htwg.se.uno.model.fileIoComponent.FileIOInterface
import de.htwg.se.uno.model.fileIoComponent.fileIoXmlImpl.FileIO
import de.htwg.se.uno.model.gameComponent.gameBaseImpl.Game
import de.htwg.se.uno.model.gameComponent.GameInterface

class UnoModule extends AbstractModule {
  val defaultPlayers:Int = 2

  override def configure() = {
    bindConstant().annotatedWith(Names.named("DefaultPlayers")).to(defaultPlayers)
    bind(classOf[GameInterface]).to(classOf[Game])
    bind(classOf[ControllerInterface]).to(classOf[controllerBaseImpl.Controller])

    bind(classOf[GameInterface]).annotatedWith(Names.named("2 Players")).toInstance(new Game(2))
    bind(classOf[GameInterface]).annotatedWith(Names.named("3 Players")).toInstance(new Game(3))
    bind(classOf[GameInterface]).annotatedWith(Names.named("4 Players")).toInstance(new Game(4))
//    bind[GameInterface].annotatedWithName("2 Players").toInstance(new Game(2))
//    bind[GameInterface].annotatedWithName("3 Players").toInstance(new Game(3))
//    bind[GameInterface].annotatedWithName("4 Players").toInstance(new Game(4))

    //bind(classOf[FileIOInterface]).to(classOf[fileIoJsonImpl.FileIO])
    bind(classOf[FileIOInterface]).to(classOf[FileIO])
  }
}
