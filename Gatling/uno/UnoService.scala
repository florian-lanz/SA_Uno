package uno

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class UnoService extends Simulation {

  private val httpProtocol = http
    .baseUrl("http://localhost:8080")
    .inferHtmlResources()
    .acceptHeader("*/*")
    .acceptEncodingHeader("gzip, deflate")
		.contentTypeHeader("application/json")

  private val scn = scenario("PersistenceService")
		.exec(
			http("NewGame")
				.get("/new-game/2")
		)
		.pause(5)
		.exec(
			http("SetCard")
				.get("/set-card/R-5")
		)
		.pause(5)
		.exec(
			http("GetCard")
				.get("/get-card")
		)
		.pause(5)
		.exec(
			http("Redo")
				.get("/redo")
		)
		.pause(5)
		.exec(
			http("Undo")
				.get("/undo")
		)
		.pause(5)
		.exec(
			http("Save")
				.get("/save")
		)
		.pause(5)
		.exec(
			http("Load")
				.get("/load")
		)
		.pause(5)
		.exec(
			http("Delete")
				.get("/delete")
		)
		.pause(5)
		.exec(
			http("DoStep")
				.get("/do-step")
		)

	setUp(scn.inject(atOnceUsers(100))).protocols(httpProtocol)
}
