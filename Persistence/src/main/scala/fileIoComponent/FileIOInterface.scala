package fileIoComponent

import model.gameComponent.GameInterface
import play.api.libs.json.JsValue
import scala.util.Try

trait FileIOInterface:
  def load(source: String = ""): Try[GameInterface]
  def save(game: GameInterface): Try[Unit]
  def gameToString(game: GameInterface): String