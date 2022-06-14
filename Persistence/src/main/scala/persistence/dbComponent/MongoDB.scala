package persistence.dbComponent

import persistence.PersistenceInterface
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.model.Filters.*
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.*
import org.mongodb.scala.model.Updates.set
import org.mongodb.scala.model.Filters.*
import org.mongodb.scala.model.Sorts.descending
import org.mongodb.scala.result.{DeleteResult, InsertOneResult, UpdateResult}

import scala.concurrent.{Await, ExecutionContextExecutor, Future}
import scala.util.Try
import concurrent.duration.DurationInt

object MongoDB extends PersistenceInterface:
  val uri: String = "mongodb://root:UNO@" + sys.env.getOrElse("MONGODB_HOST", "localhost:27017")
  val client: MongoClient = MongoClient(uri)
  val database: MongoDatabase = client.getDatabase("uno")
  val gameCollection: MongoCollection[Document] = database.getCollection("game")

  override def save(json: String): Future[Any] =
    val game: Document = Document.apply(json)
    gameCollection.insertOne(game).toFuture()

  override def load(id: String): Future[String] =
      implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "SingleRequest")
      implicit val executionContext: ExecutionContextExecutor = system.executionContext
      if id.equals("0") then
        gameCollection.find().limit(1).sort(descending("_id")).projection(excludeId()).head().map(_.toJson)
      else
        gameCollection.find(equal("_id", new ObjectId(id))).projection(excludeId()).head().map(_.toJson)

  override def delete(id: String): Future[Any] =
    println(s"Deleting game in MongoDB")
    if id.equals("0") then
      gameCollection.deleteMany(notEqual("_id", new ObjectId("000000000000000000000000"))).toFuture()
    else
      gameCollection.deleteOne(equal("_id", new ObjectId(id))).toFuture()