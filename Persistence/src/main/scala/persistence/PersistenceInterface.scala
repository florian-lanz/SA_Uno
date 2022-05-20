package persistence

import scala.util.Try

trait PersistenceInterface:
  def load(id: String): Try[String]
  def save(json: String): Try[Unit]
  def delete(id: String): Try[Unit]
