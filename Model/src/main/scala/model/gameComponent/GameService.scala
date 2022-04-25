package model.gameComponent

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.*
import com.fasterxml.jackson.annotation.JsonValue
import play.api.libs.json.{JsValue, Json}
import model.gameComponent.gameBaseImpl.Game

import scala.concurrent.ExecutionContextExecutor

case object GameService:
  def main(args: Array[String]): Unit =
    val game = Game()
    implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "my-system")
    implicit val executionContext: ExecutionContextExecutor = system.executionContext

    val interface = "localhost"
    val port = 8082

    println(s"Game service started: http://$interface:$port")

    val route =
      concat (
        get {
          path("") {
            val apiInfo =
              """Available API Routes - Model:
                |
                |POST    /to-string                 -> required arguments: gameJson
                |POST    /enemy                     -> required arguments: enemyIndex, gameJson
                |POST    /pull-move                 -> required arguments: gameJson
                |POST    /push-move                 -> required arguments: cardString, cardColor, gameJson
                |POST    /create-game               -> required arguments: gameSize, gameJson
                |POST    /next-turn                 -> required arguments: gameJson
                |POST    /next-enemy                -> required arguments: gameJson
                |POST    /change-active-player      -> required arguments: gameJson
                |POST    /shuffle                   -> required arguments: gameJson
                |""".stripMargin
            complete(HttpEntity(ContentTypes.`application/json`, apiInfo))
          }
        },
        post {
          path("to-string") {
            entity(as[String]) { request =>
              complete(HttpEntity(ContentTypes.`application/json`, game.gameFromJson(request).toString()))
            }
          }
        },
        post {
          path("enemy") {
            entity(as[String]) { request =>
              val json: JsValue = Json.parse(request)
              val enemyIndex = (json \ "enemyIndex").get.toString.toInt
              val gameString = (json \ "game").get.toString
              complete(HttpEntity(ContentTypes.`application/json`, game.gameFromJson(gameString).enemy(enemyIndex).gameToJson()))
            }
          }
        },
        post {
          path("pull-move") {
            entity(as[String]) { request =>
              complete(HttpEntity(ContentTypes.`application/json`, game.gameFromJson(request).pullMove().gameToJson()))
            }
          }
        },
        post {
          path("push-move") {
            entity(as[String]) { request =>
              val json: JsValue = Json.parse(request)
              val cardString = (json \ "cardString").as[String]
              val cardColor = (json \ "cardColor").get.toString.toInt
              val gameString = (json \ "game").get.toString
              complete(HttpEntity(ContentTypes.`application/json`, game.gameFromJson(gameString).pushMove(cardString, cardColor).gameToJson()))
            }
          }
        },
        post {
          path("create-game") {
            entity(as[String]) { request =>
              val json: JsValue = Json.parse(request)
              val gameSize: 2 | 3 | 4 = (json \ "gameSize").get.toString.toInt match
                case 2 => 2
                case 3 => 3
                case 4 => 4  
                case _ => 2
              complete(HttpEntity(ContentTypes.`application/json`, game.createGame(gameSize).gameToJson()))
            }
          }
        },
        post {
          path("next-turn") {
            entity(as[String]) { request =>
              complete(HttpEntity(ContentTypes.`application/json`, game.gameFromJson(request).nextTurn().toString))
            }
          }
        },
        post {
          path("next-enemy") {
            entity(as[String]) { request =>
              complete(HttpEntity(ContentTypes.`application/json`, game.gameFromJson(request).nextEnemy().toString))
            }
          }
        },
        post {
          path("change-active-player") {
            entity(as[String]) { request =>
              complete(HttpEntity(ContentTypes.`application/json`, game.gameFromJson(request).changeActivePlayer().gameToJson()))
            }
          }
        },
        post {
          path("shuffle") {
            entity(as[String]) { request =>
              complete(HttpEntity(ContentTypes.`application/json`, game.gameFromJson(request).shuffle().gameToJson()))
            }
          }
        },
      )

    Http().newServerAt(interface, port).bind(route)