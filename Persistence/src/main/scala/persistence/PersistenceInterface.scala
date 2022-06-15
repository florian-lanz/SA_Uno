package persistence

import scala.concurrent.Future

trait PersistenceInterface:
  def load(id: String): Future[String]
  def save(json: String): Future[Any]
  def delete(id: String): Future[Any]
