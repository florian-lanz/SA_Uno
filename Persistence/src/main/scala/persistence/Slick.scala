package persistence
import persistence.sqlTables.GameTable

import scala.util.Try
import slick.dbio.{DBIO, Effect}
import play.api.libs.json.{JsArray, JsBoolean, JsNumber, JsString, JsValue, Json}
import slick.lifted.TableQuery
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.MySQLProfile.api.*

import scala.:+
import scala.collection.IterableOnce.iterableOnceExtensionMethods
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.{Duration, DurationInt}
import scala.util.{Failure, Success}

object Slick extends PersistenceInterface:
  val databaseUrl: String = "jdbc:mysql://" + sys.env.getOrElse("DATABASE_HOST", "localhost:3306") + "/" + sys.env.getOrElse("MYSQL_DATABASE", "uno") + "?serverTimezone=UTC&useSSL=false"
  val databaseUser: String = sys.env.getOrElse("MYSQL_USER", "root")
  val databasePassword: String = sys.env.getOrElse("MYSQL_PASSWORD", "UNO")

  val database = Database.forURL(
    url = databaseUrl,
    driver = "com.mysql.cj.jdbc.Driver",
    user = databaseUser,
    password = databasePassword
  )

  val gameTable = TableQuery.apply[GameTable]

  val setup: DBIOAction[Unit, NoStream, Effect.Schema] = DBIO.seq(gameTable.schema.createIfNotExists)
  database.run(setup)

  override def save(json: String): Try[Unit] =
    println("Saving game in MySQL")
    val gameJson = Json.parse(json)
    Try{
        database.run(gameTable += (
          0,
          (gameJson \ "game" \ "numOfPlayers").get.toString.toInt,
          (gameJson \ "game" \ "activePlayer").get.toString.toInt,
          (gameJson \ "game" \ "direction").get.toString.toBoolean,
          (gameJson \ "game" \ "anotherPull").get.toString.toBoolean,
          (gameJson \ "game" \ "specialCard").get.toString.toInt,
          (gameJson \ "game" \ "playerCards").as[List[String]].mkString(","),
          (gameJson \ "game" \ "openCardStack").as[List[String]].mkString(","),
          (gameJson \ "game" \ "coveredCardStack").as[List[String]].mkString(","),
          (gameJson \ "game" \ "enemy1Cards").as[List[String]].mkString(","),
          (gameJson \ "game" \ "enemy2Cards").as[List[String]].mkString(","),
          (gameJson \ "game" \ "enemy3Cards").as[List[String]].mkString(",")
        ))
      }

  override def load(id: Int): Try[String] =
    println("Loading game from MySQL")
    Try{
      val query = (if id == 0 then sql"""SELECT * FROM GAME ORDER BY ID DESC LIMIT 1""" else sql"""SELECT * FROM GAME WHERE ID = $id""").as[(Int, Int, Int, Boolean, Boolean, Int, String, String, String, String, String, String)]
      val result = Await.result(database.run(query), 2.second)
      Json.obj(
        "game" -> Json.obj(
          "numOfPlayers" -> JsNumber(result(0)(1)),
          "activePlayer" -> JsNumber(result(0)(2)),
          "direction" -> JsBoolean(result(0)(3)),
          "anotherPull" -> JsBoolean(result(0)(4)),
          "specialCard" -> JsNumber(result(0)(5)),
          "playerCards" -> JsArray(for card <- result(0)(6).split(",").toList yield JsString(card)),
          "openCardStack" -> JsArray(for card <- result(0)(7).split(",").toList yield JsString(card)),
          "coveredCardStack" -> JsArray(for card <- result(0)(8).split(",").toList yield JsString(card)),
          "enemy1Cards" -> JsArray(for card <- result(0)(9).split(",").toList yield JsString(card)),
          "enemy2Cards" -> JsArray(for card <- result(0)(10).split(",").toList yield JsString(card)),
          "enemy3Cards" -> JsArray(for card <- result(0)(11).split(",").toList yield JsString(card))
        )
      ).toString()
    }

  override def delete(id: Int): Try[Unit] =
    println("Deleting game in MySQL")
    Try{
      val query = if id == 0 then gameTable.delete else gameTable.filter(_.id === id).delete
      database.run(query)
    }