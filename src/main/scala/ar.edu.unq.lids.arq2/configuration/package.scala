package ar.edu.unq.lids.arq2

import scala.util.Properties

package object configuration {

  object environment {
    val development = "DEVELOPMENT"
    val heroku = "HEROKU"

    val name = ":" + Properties.envOrElse("environment", development)
  }

  object server{
    val port = ":" + Properties.envOrElse("PORT", "9200")

  }

  object response{
    val limitSize = Properties.envOrElse("limit_size", "10").toInt
    val maxLimitSize = Properties.envOrElse("max_limit_size", "1000").toInt
  }

  object database{

    val storage = Properties.envOrElse("storage", "transient")
    val fillData = Properties.envOrNone("fill_data").map(_=>false).getOrElse(true)

    object mongo {
      val host = Properties.envOrElse("mongo_host", "localhost")
      val port = Properties.envOrElse("mongo_posts", "27017").toInt
      val db = Properties.envOrElse("mongo_db", "careprices")
      val authentication = Properties.envOrNone("mongo_auth").map(auth => {
        val split = auth.split(",")
        (split.head, split.last)
      })
    }

    object mysql{
      val user = Properties.envOrSome("mysql_user", Some("root"))
      val password = Properties.envOrSome("mysql_pwd", Some("root"))
      val schema = Properties.envOrElse("mysql_schema", "careprices")
      val url = Properties.envOrElse("mysql_url", "jdbc:mysql://localhost:8889/")
    }

    object redis{
      val host = Properties.envOrElse("redis_host", "localhost")
      val port = Properties.envOrElse("redis_port", "6379").toInt
    }
  }
}



case class ConfigurationResponse(
    environment:Object = configuration.environment,
    server:Object = configuration.server,
    response:Object = configuration.response,
    database:Object = configuration.database,
    mysql:Object = configuration.database.mysql,
    mongo:Object = configuration.database.mongo,
    redis:Object = configuration.database.redis
  )