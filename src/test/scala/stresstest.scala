import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.StringBody
import scala.concurrent.duration._

class ConsultaShopsAgregarPrecios extends Simulation {

  val httpConf = http
    .baseURL("http://localhost:9200/api/v1")
    .acceptHeader("application/json")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1")

  val scncarepricesuser1 =
    scenario("shop-found-prices[3]")
      .exec(http("shopRandom")
        .get("/shops")
        .queryParam("offset", "0")
        .queryParam("limit", "10")
        .asJSON
        .check(jsonPath("$.items[*].id").ofType[String].findAll.saveAs("items")))
      .exec(http("found-prices")
        .post("/found-prices")
        .body(new StringBody("{\n   \"shop_id\": \"e8410c81-357d-11e6-b788-c9bb021a2cee-274f1600\",\n   \"product_id\": \"4646\",\n   \"price\": 122.18,\n   \"datetime\": \"2016-04-02T15:51:15-03:00\"\n}"))
        .asJSON
        .check(status.is(201)))
      .pause(2)
      .exec(http("found-prices")
        .post("/found-prices")
        .body(new StringBody("{\n   \"shop_id\": \"e8410c81-357d-11e6-b788-c9bb021a2cee-274f1600\",\n   \"product_id\": \"7868678\",\n   \"price\": 20.30,\n   \"datetime\": \"2016-04-02T15:51:15-03:00\"\n}"))
        .asJSON
        .check(status.is(201)))
      .pause(2)
      .exec(http("found-prices")
        .post("/found-prices")
        .body(new StringBody("{\n   \"shop_id\": \"e8410c81-357d-11e6-b788-c9bb021a2cee-274f1600\",\n   \"product_id\": \"24242\",\n   \"price\": 30.5,\n   \"datetime\": \"2016-04-02T15:51:15-03:00\"\n}"))
        .asJSON
        .check(status.is(201)))
      .pause(2)


  val scncarepricesuser2 =
    scenario("shop-found-prices[4]")
      .exec(http("shopRandom")
        .get("/shops")
        .queryParam("offset", "0")
        .queryParam("limit", "10")
        .asJSON
        .check(jsonPath("$.items[*].id").ofType[String].findAll.saveAs("items")))
      .exec(http("found-prices")
        .post("/found-prices")
        .body(new StringBody("{\n   \"shop_id\": \"e8410c82-357d-11e6-b788-c9bb021a2cee-274f1600\",\n   \"product_id\": \"46456\",\n   \"price\": 15.9,\n   \"datetime\": \"2016-04-02T15:51:15-03:00\"\n}"))
        .asJSON
        .check(status.is(201)))
      .pause(2)
      .exec(http("found-prices")
        .post("/found-prices")
        .body(new StringBody("{\n   \"shop_id\": \"e8410c82-357d-11e6-b788-c9bb021a2cee-274f1600\",\n   \"product_id\": \"12446456\",\n   \"price\": 25.5,\n   \"datetime\": \"2016-04-02T15:51:15-03:00\"\n}"))
        .asJSON
        .check(status.is(201)))
      .pause(2)
      .exec(http("found-prices")
        .post("/found-prices")
        .body(new StringBody("{\n   \"shop_id\": \"e8410c82-357d-11e6-b788-c9bb021a2cee-274f1600\",\n   \"product_id\": \"353453\",\n   \"price\": 50.5,\n   \"datetime\": \"2016-04-02T15:51:15-03:00\"\n}"))
        .asJSON
        .check(status.is(201)))
      .pause(2)
      .exec(http("found-prices")
        .post("/found-prices")
        .body(new StringBody("{\n   \"shop_id\": \"e8410c82-357d-11e6-b788-c9bb021a2cee-274f1600\",\n   \"product_id\": \"345656\",\n   \"price\": 29.5,\n   \"datetime\": \"2016-04-02T15:51:15-03:00\"\n}"))
        .asJSON
        .check(status.is(201)))
      .pause(2)

  setUp(scncarepricesuser1
    .inject(
      atOnceUsers(10),
      rampUsers(10) over(5 minutes))
    .protocols(httpConf))
}
