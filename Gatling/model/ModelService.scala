package model

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class ModelService extends Simulation {

  private val httpProtocol = http
    .baseUrl("http://localhost:8082")
    .inferHtmlResources()
    .acceptHeader("*/*")
    .acceptEncodingHeader("gzip, deflate")
		.contentTypeHeader("application/json")

  private val scn = scenario("ModelService")
		.exec(
			http("MainRoute")
				.get("/")
		)
		.pause(5)
		.exec(
			http("Enemy")
				.post("/enemy")
				.body(RawFileBody("model/recordedsimulation/request_1.json"))
		)
		.pause(5)
		.exec(
			http("ToString")
				.post("/to-string")
				.body(RawFileBody("model/recordedsimulation/request.json"))
		)
		.pause(5)
		.exec(
			http("PullMove")
				.post("/pull-move")
				.body(RawFileBody("model/recordedsimulation/request.json"))
		)
		.pause(5)
		.exec(
			http("PushMove")
				.post("/push-move")
				.body(RawFileBody("model/recordedsimulation/request_2.json"))
		)
		.pause(5)
		.exec(
			http("CreateGame")
				.post("/create-game")
				.body(RawFileBody("model/recordedsimulation/request_3.json"))
		)
		.pause(5)
		.exec(
			http("NextTrun")
				.post("/next-turn")
				.body(RawFileBody("model/recordedsimulation/request.json"))
		)
		.pause(5)
		.exec(
			http("NextEnemy")
				.post("/next-enemy")
				.body(RawFileBody("model/recordedsimulation/request.json"))
		)
		.pause(5)
		.exec(
			http("ChangeActivePlayer")
				.post("/change-active-player")
				.body(RawFileBody("model/recordedsimulation/request.json"))
		)
		.pause(5)
		.exec(
			http("Shuffle")
				.post("/shuffle")
				.body(RawFileBody("model/recordedsimulation/request.json"))
		)


	setUp(scn.inject(atOnceUsers(1000))).protocols(httpProtocol)
}
