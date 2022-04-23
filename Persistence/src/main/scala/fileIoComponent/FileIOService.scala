package fileIoComponent

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.*
import fileIoComponent.fileIoJsonImpl.FileIO

import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}

case object FileIOService:
  def main(args: Array[String]): Unit =
    val fileIO = FileIO()
    implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "my-system")
    implicit val executionContext: ExecutionContextExecutor = system.executionContext

    val interface = "localhost"
    val port = 8081

    println(s"FileIO service started: http://$interface:$port")

    val route =
      concat (
        get {
          path("load") {
            fileIO.load() match
              case Success(game) => complete(HttpEntity(ContentTypes.`application/json`, game))
              case Failure(e) => complete("Failure")
          }
        },
        post {
          path("save") {
            entity(as[String]) { game =>
              fileIO.save(game) match
                case Success(s) => complete("Success")
                case Failure(e) => complete("Failure")
            }
          }
        }
      )

    Http().newServerAt(interface, port).bind(route)