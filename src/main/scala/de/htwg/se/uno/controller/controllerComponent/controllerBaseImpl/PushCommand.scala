package de.htwg.se.uno.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.uno.util.Command
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpMethods, HttpRequest}
import akka.http.scaladsl.unmarshalling.Unmarshaller
import play.api.libs.json.Json
import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}

class PushCommand(string: String, color: Int, controller: Controller, afterPushCommand: () => Unit) extends Command(controller):
  implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "my-system")
  implicit val executionContext: ExecutionContextExecutor = system.executionContext

  override def doStep(): Unit =
    Http().singleRequest(
      HttpRequest(
        method = HttpMethods.POST,
        uri = s"http://${controller.modelHttpServer}/push-move",
        entity = HttpEntity(ContentTypes.`application/json`,
          Json.obj(
            "cardString" -> string,
            "cardColor" -> color,
            "game" -> controller.gameJson
          ).toString
        )
      )
    ).onComplete {
      case Success(value) =>
        Unmarshaller.stringUnmarshaller(value.entity).onComplete {
          case Success(value) =>
            controller.gameJson = Json.parse(value)
            afterPushCommand()
          case Failure(_) => controller.controllerEvent("modelRequestError")
        }
      case Failure(_) => controller.controllerEvent("modelRequestError")
    }