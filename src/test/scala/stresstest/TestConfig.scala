package stresstest

/**
  * Created by nnydjesus on 7/3/16.
  */
object TestConfig {

  val apiVersion = "/api/v1/"
  val host = "http://localhost:9200"
  val headersApi = Map("Content-Type" -> "application/json")
  val limit = "100"
  val fullHost = host+apiVersion

}
