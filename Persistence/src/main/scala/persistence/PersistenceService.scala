package persistence

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpMethods, HttpRequest}
import akka.http.scaladsl.server.Directives.*
import persistence.dbComponent.{MongoDB, Slick}

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn
import scala.util.{Failure, Success}

case object PersistenceService:
  def main(args: Array[String]): Unit =
    val persistence = MongoDB
    implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "my-system")
    implicit val executionContext: ExecutionContextExecutor = system.executionContext

    val interface = "persistence-service"
    val port = 8081

    val route =
      concat(
        get {
          path("") {
            val apiInfo =
              """Available API Routes - Persistence:
                |
                |GET     /load
                |POST    /save    -> required arguments: gameJson
                |""".stripMargin
            complete(HttpEntity(ContentTypes.`application/json`, apiInfo))
          }
        },
        get {
          path("load" / Remaining) { id =>
            persistence.load(id) match
              case Success(game) => complete(HttpEntity(ContentTypes.`application/json`, game))
              case Failure(e) =>
                println(e.printStackTrace())
                complete("Failure")
          }
        },
        get {
          path("delete" / Remaining) { id =>
            persistence.delete(id) match
              case Success(game) => complete("Success")
              case Failure(e) =>
                println(e.printStackTrace())
                complete("Failure")
          }
        },
        post {
          path("save") {
            entity(as[String]) { game =>
              println("HERE 1")
              println(game)
              persistence.save(game) match
                case Success(s) => complete("Success")
                case Failure(e) => complete("Failure")
            }
          }
        }
      )

    Http().newServerAt(interface, port).bind(route)

    println(s"FileIO service started: http://$interface:$port")
    println("Press return to stop")
    StdIn.readLine()
