package ar.edu.unq.lids.arq2

import net.fwbrasil.activate.ActivateContext
import net.fwbrasil.activate.storage.memory.TransientMemoryStorage
import net.fwbrasil.activate.storage.mongo.async.AsyncMongoStorage
import net.fwbrasil.activate.storage.relational.PooledJdbcRelationalStorage
import net.fwbrasil.activate.storage.relational.idiom.mySqlDialect

import scala.util.Properties

// Initially, must be created the persistence context. It must be a singleton, so it makes sense to declare as "object".
object CartePriceActivateContext extends ActivateContext {



	//val storage = new PrevaylerStorage

	lazy val mongoStorage = new AsyncMongoStorage {
		val host = configuration.database.mongo.host
		override val port = configuration.database.mongo.port
		val db = configuration.database.mongo.db
		override val authentication = configuration.database.mongo.authentication
	}

	lazy val mysqlStorage = new PooledJdbcRelationalStorage {
		val jdbcDriver = "com.mysql.jdbc.Driver"
		val user = configuration.database.mysql.user
		val password = configuration.database.mysql.password
		val url = configuration.database.mysql.url+configuration.database.mysql.schema
		val dialect = mySqlDialect
	}

	val transientStorage = new TransientMemoryStorage

	lazy val storage = configuration.database.storage match{
		case "mongo" => mongoStorage
		case "mysql" => mysqlStorage
		case "transient" => transientStorage
	}

	//	val storage = new PooledJdbcRelationalStorage {
	//		val jdbcDriver = "oracle.jdbc.driver.OracleDriver"
	//		val user = Some("USER")
	//		val password = Some("PASS")
	//		val url = "jdbc:oracle:thin:@localhost:1521:oracle"
	//		val dialect = oracleDialect
	//	}

	//	val storage = new PooledJdbcRelationalStorage {
	//		val jdbcDriver = "org.postgresql.Driver"
	//		val user = Some("postgres")
	//		val password = None
	//		val url = "jdbc:postgresql://127.0.0.1/postgres"
	//		val dialect = postgresqlDialect
	//	}

}
