package persistence

import scala.util.Try

trait PersistenceInterface:
  def load(id: Int): Try[String]
  def save(json: String): Try[Unit]
  def delete(id: Int): Try[Unit]
