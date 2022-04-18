package de.htwg.se.uno

import com.google.inject.AbstractModule
import com.google.inject.name.Names
import de.htwg.se.uno.controller.controllerComponent._
import fileIoComponent._
import model.gameComponent.gameBaseImpl.Game
import model.gameComponent.GameInterface

class UnoModule extends AbstractModule:
  val defaultPlayers: Int = 2

  override def configure(): Unit =
    bindConstant().annotatedWith(Names.named("DefaultPlayers")).to(defaultPlayers)
    bind(classOf[GameInterface]).annotatedWith(Names.named("2 Players")).toInstance(new Game(2).createGame())
    bind(classOf[GameInterface]).annotatedWith(Names.named("3 Players")).toInstance(new Game(3).createGame())
    bind(classOf[GameInterface]).annotatedWith(Names.named("4 Players")).toInstance(new Game(4).createGame())
    bind(classOf[GameInterface]).toInstance(new Game(2).createGame())

    bind(classOf[ControllerInterface]).to(classOf[controllerBaseImpl.Controller])

    bind(classOf[FileIOInterface]).to(classOf[fileIoJsonImpl.FileIO])
    //bind(classOf[FileIOInterface]).to(classOf[fileIoXmlImpl.FileIO])