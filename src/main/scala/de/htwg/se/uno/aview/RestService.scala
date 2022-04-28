package de.htwg.se.uno.aview

import akka.actor.FSM.Failure
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpMethods, HttpRequest}
import akka.http.scaladsl.server.Directives.*
import de.htwg.se.uno.controller.controllerComponent.{ControllerInterface, GameNotChanged}
import play.api.libs.json.Json

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.swing.Reactor
import scala.util.Success

case class RestService(controller: ControllerInterface):
  implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "my-system")
  implicit val executionContext: ExecutionContextExecutor = system.executionContext

  val interface = "localhost"
  val port = 8080

  def start(): Future[Http.ServerBinding] =
    println(s"server started: http://$interface:$port")
    val route =
      concat(
        get {
          path("") {
            val apiInfo =
              """Available API Routes - Uno:
                |
                |GET    /new-game/<numOfPlayers>
                |GET    /set-card/<card>
                |GET    /get-card
                |GET    /redo
                |GET    /undo
                |GET    /save
                |GET    /load
                |GET    /do-step
                |""".stripMargin
            complete(HttpEntity(ContentTypes.`application/json`, apiInfo))
          }
        },
        get {
          path("new-game" / IntNumber) { numOfPlayers =>
            numOfPlayers match
              case 2 | 3 | 4 =>
                controller.createGame(numOfPlayers)
                complete(HttpEntity(ContentTypes.`application/json`, controller.gameToJson()))
              case _ => complete(HttpEntity(ContentTypes.`application/json`, "Ungültige Anzahl an Spielern!"))
          }
        },
        get {
          path("set-card" / Remaining) { urlCard =>
            val card = urlCard.replace("-", " ")
            if card.length() > 3 then
              card.substring(4) match
                case "blue" =>
                  controller.set(card.substring(0, 3), 1)
                  complete(HttpEntity(ContentTypes.`application/json`, controller.gameToJson()))
                case "green" =>
                  controller.set(card.substring(0, 3), 2)
                  complete(HttpEntity(ContentTypes.`application/json`, controller.gameToJson()))
                case "yellow" =>
                  controller.set(card.substring(0, 3), 3)
                  complete(HttpEntity(ContentTypes.`application/json`, controller.gameToJson()))
                case _ =>
                  controller.set(card.substring(0, 3), 4)
                  complete(HttpEntity(ContentTypes.`application/json`, controller.gameToJson()))
            else
              controller.set(card, 0)
              complete(HttpEntity(ContentTypes.`application/json`, controller.gameToJson()))
          }
        },
        get {
          path("get-card") {
            controller.get()
            complete(HttpEntity(ContentTypes.`application/json`, controller.gameToJson()))
          }
        },
        get {
          path("redo") {
            controller.redo()
            complete(HttpEntity(ContentTypes.`application/json`, controller.gameToJson()))
          }
        },
        get {
          path("undo") {
            controller.undo()
            complete(HttpEntity(ContentTypes.`application/json`, controller.gameToJson()))
          }
        },
        get {
          path("save") {
            controller.save()
            complete(HttpEntity(ContentTypes.`application/json`, controller.gameToJson()))
          }
        },
        get {
          path("load") {
            controller.load()
            complete(HttpEntity(ContentTypes.`application/json`, controller.gameToJson()))
          }
        },
        get {
          path("do-step") {
            if controller.nextTurn() then
              complete(HttpEntity(ContentTypes.`application/json`, controller.gameToJson()))
            else
              controller.enemy()
              complete(HttpEntity(ContentTypes.`application/json`, controller.gameToJson()))
          }
        }
      )
    Http().newServerAt(interface, port).bind(route)

  def stop(server: Future[Http.ServerBinding]): Unit = server.flatMap(_.unbind()).onComplete(_ => println(port + " released"))
