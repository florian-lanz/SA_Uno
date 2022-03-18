package de.htwg.se.uno.model.fileIoComponent

import de.htwg.se.uno.model.gameComponent.GameInterface

trait FileIOInterface {
  def load: GameInterface
  def save(game: GameInterface): Unit
}
