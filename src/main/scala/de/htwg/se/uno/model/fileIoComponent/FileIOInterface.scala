package de.htwg.se.uno.model.fileIoComponent

import de.htwg.se.uno.model.gameComponent.GameInterface
import play.api.libs.json.JsValue
import scala.util.Try

trait FileIOInterface:
  def load(source: String = ""): Try[GameInterface]
  def save(game: GameInterface): Try[Unit]
  def gameToString(game: GameInterface): String