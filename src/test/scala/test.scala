import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class MySimulation extends Simulation {

  val conf = http.baseURL("http://careprices.herokuapp.com")

  val scn = scenario("Gatling")
    .exec(http("index").get("/"))
    .during(10 second) {
      exec(
        http("json").get("/api/v1/shops")
          .check(jsonPath("$.paging.total")
            .saveAs("total"))
      )
    }

  setUp(scn.inject(atOnceUsers(5)))
    .protocols(conf)
}