package ar.edu.unq.lids.arq2

import net.fwbrasil.activate.ActivateContext
import net.fwbrasil.activate.storage.mongo.async.AsyncMongoStorage
import net.fwbrasil.activate.storage.relational.PooledJdbcRelationalStorage
import net.fwbrasil.activate.storage.relational.idiom.mySqlDialect

import scala.util.Properties

// Initially, must be created the persistence context. It must be a singleton, so it makes sense to declare as "object".
object CartePriceActivateContext extends ActivateContext {

	//val storage = new TransientMemoryStorage

	//val storage = new PrevaylerStorage

	val mongoStorage = new AsyncMongoStorage {
		val host = "localhost"
		override val port = 27017
		val db = "products"
		override val authentication = None
	}

	lazy val mysqlStorage = new PooledJdbcRelationalStorage {
		val jdbcDriver = "com.mysql.jdbc.Driver"
		val user = Some("root")
		val password = Some("root")
		val url = "jdbc:mysql://localhost:8889/activate_test"
		val dialect = mySqlDialect
	}

	lazy val storage = Properties.envOrElse("storage", "mongo") match{
		case "mongo" => mongoStorage
		case "mysql" => mysqlStorage
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
