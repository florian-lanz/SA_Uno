package de.htwg.se.uno.model.fileIoComponent

import de.htwg.se.uno.model.gameComponent.GameInterface
import play.api.libs.json.JsValue

trait FileIOInterface:
  def load(source: String = ""): GameInterface
  def save(game: GameInterface): Unit
  def gameToJson(game: GameInterface): JsValue