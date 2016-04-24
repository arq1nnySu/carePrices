package ar.edu.unq.lids.arq2.service

import ar.edu.unq.lids.arq2.configuration
import com.redis._
import com.twitter.finatra.json.FinatraObjectMapper
import ar.edu.unq.lids.arq2

import scala.util.Try

class CacheService{

  lazy val client = new RedisClient(configuration.database.redis.host, configuration.database.redis.port, timeout = 300)
  val mapper = FinatraObjectMapper.create()

  def memorize[T](key:String)(data: => T ): String = {
    Try(
      client.get(key) match{
        case Some(value) => value
        case None => {
          val value = mapper.writePrettyString(data)
          client.setex(key, 60, value)
          value
        }
      }).getOrElse(mapper.writePrettyString(data))

  }



}
