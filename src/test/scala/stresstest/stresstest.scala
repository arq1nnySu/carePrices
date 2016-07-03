package stresstest

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.StringBody
import stresstest.TestConfig._
import scala.concurrent.duration._

class ConsultaShopsAgregarPrecios extends Simulation {

  DownloadFeeders.loadFeeders()
  lazy val shopsFeeder = csv("shops.csv").random
  lazy val productsFeeder = csv("products.csv").random

  val httpConf = http
    .baseURL(TestConfig.fullHost)
    .acceptHeader("application/json")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1")


  object Product {
    lazy val operations = exec(http("List Products")
      .get("products"))
      .feed(productsFeeder)
      .exec(http("Post Product")
        .post("products")
        .headers(headersApi)
        .body(new StringBody("""{ "id":"${product_id}","name":"${product_name}", "barcode":"${product_barcode}" }""")).asJSON
        .check(status.is(201))
      )
  }

  object Shops {
    lazy val operations = exec(http("List Shops")
      .get("shops"))
      .feed(shopsFeeder)
      .exec(http("Post Shop")
        .post("shops")
        .headers(headersApi)
        .body(new StringBody("""{
            "id":"${shop_id}","name":"${shop_name}","latitude":"${shop_latitude}","longitude":"${shop_longitude}",
            "address":"${shop_address}","location":"${shop_location}","chain":"${shop_chain}" }""")).asJSON
        .check(status.is(201))
      )
  }

  object Price {
    lazy val operations = exec(http("List prices")
      .get("found-prices"))
      .feed(shopsFeeder)
      .feed(productsFeeder)
      .exec(http("Post Price")
        .post("found-prices")
        .headers(headersApi)
        .body(new StringBody("""{"shop_id": "${shop_id}", "product_id": "${product_id}","price": 30, "datetime": "2016-04-02T15:51:15-03:00"}""")).asJSON
        .check(status.is(201))
      )
  }

  val products = scenario("Products Operations").exec(Product.operations)
  val shops = scenario("Shops Operations").exec(Shops.operations)
  val prices = scenario("Prices Operations").exec(Price.operations)

  setUp(
//    products.inject(atOnceUsers(5)),
//    shops.inject(atOnceUsers(5)),
      prices.inject(atOnceUsers(5), rampUsersPerSec(10) to 200 during(10 minutes))
//    .inject(
//      atOnceUsers(30),
//      rampUsers(50) over(1 seconds),
//      constantUsersPerSec(200) during(5 minutes))
    .protocols(httpConf))
}
