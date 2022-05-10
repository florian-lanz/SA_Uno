package persistence

import scala.util.Try

trait PersistenceInterface:
  def load(): Try[String]
  def save(json: String): Try[Unit]
