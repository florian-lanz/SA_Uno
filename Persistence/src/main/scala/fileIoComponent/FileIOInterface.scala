package fileIoComponent

import model.gameComponent.GameInterface
import play.api.libs.json.JsValue
import scala.util.Try

trait FileIOInterface:
  def load(): Try[String]
  def save(game: String): Try[Unit]