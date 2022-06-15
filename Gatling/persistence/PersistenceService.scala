package persistence

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class PersistenceService extends Simulation {

  private val httpProtocol = http
    .baseUrl("http://localhost:8081")
    .inferHtmlResources()
    .acceptHeader("*/*")
    .acceptEncodingHeader("gzip, deflate")
		.contentTypeHeader("application/json")

  private val scn = scenario("PersistenceService")
		.exec(
			http("Save")
				.post("/save")
				.body(RawFileBody("persistence/recordedsimulation/request.json"))
		)
		.pause(5)
    .exec(
      http("Load")
        .get("/load/0")
    )
		.pause(5)
		.exec(
			http("Delete")
				.get("/delete/0")
		)

	setUp(scn.inject(atOnceUsers(1000))).protocols(httpProtocol)
}
