package stresstest

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.StringBody
import stresstest.TestConfig._
import scala.concurrent.duration._

class Stresstest extends Simulation {

  DownloadFeeders.loadFeeders()
  lazy val shopsFeeder = csv("shops.csv").random
  lazy val productsFeeder = csv("products.csv").random
  val random = new util.Random
  val randomFeeder = Iterator.continually(Map("value" -> random.alphanumeric.take(20).mkString(""), "price" -> random.nextInt(100)))

  val httpConf = http
    .baseURL(TestConfig.fullHost)
    .acceptHeader("application/json")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1")


  object Product {
    lazy val operations = exec(http("List Products")
      .get(TestConfig.fullHost+"products"))
      .feed(randomFeeder)
      .exec(http("Post Product")
        .post("http://192.168.0.28:9200/api/v1/products")
        .headers(headersApi)
        .body(new StringBody("""{"name":"${value}", "barcode":"${value}" }""")).asJSON
        .check(status.is(201))
      )
  }

  object Shops {
    lazy val operations = exec(http("List Shops")
      .get(TestConfig.fullHost+"shops"))
      .feed(randomFeeder)
      .exec(http("Post Shop")
        .post("http://192.168.0.28:9200/api/v1/shops")
        .headers(headersApi)
        .body(new StringBody("""{
            "name":"${value}","latitude":"${value}","longitude":"${value}",
            "address":"${value}","location":"${value}","chain":"${value}" }""")).asJSON
        .check(status.is(201))
      )
  }

  object Price {
    lazy val operations = exec(http("List prices")
      .get("found-prices"))
      .feed(shopsFeeder)
      .feed(productsFeeder)
      .feed(randomFeeder)
      .exec(http("Post Price")
        .post("found-prices")
        .headers(headersApi)
        .body(new StringBody("""{"shop_id": "${shop_id}", "product_id": "${product_id}","price": ${price}, "datetime": "2016-04-02T15:51:15-03:00"}""")).asJSON
        .check(status.is(201))
      )
  }

  val products = scenario("Products Operations").exec(Product.operations)
  val shops = scenario("Shops Operations").exec(Shops.operations)
  val prices = scenario("Prices Operations").exec(Price.operations)



  setUp(
      products.inject(rampUsersPerSec(5) to 10 during(10 minutes)),
      shops.inject(   rampUsersPerSec(5) to 10 during(10 minutes)),
      prices.inject( rampUsersPerSec(10) to (150) during(10 minutes))
    .protocols(httpConf))
}
