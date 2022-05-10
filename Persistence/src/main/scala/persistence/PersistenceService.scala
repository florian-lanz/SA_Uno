package persistence

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.{as, complete, concat, entity, get, path, post}
import fileIoComponent.fileIoJsonImpl.FileIO

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn
import scala.util.{Failure, Success}

case object PersistenceService:
  def main(args: Array[String]): Unit =
    val persistence = Slick
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
          path("load") {
            println("LOAD 1")
            persistence.load() match
              case Success(game) => complete(HttpEntity(ContentTypes.`application/json`, game))
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
