# Mirar para cuidar

## Instancias
* [Heroku](http://careprices.herokuapp.com) - Servidor hosteado en Heroku.
* [Amazon](http://careprices.passto.com.ar) - Servidor hosteado en Amazon.

## Documencación de la API:
* [API](http://docs.careprices.apiary.io/#reference/0/product/create-a-new-product?console=1)

## Stack elegido

 * [SBT](http://www.scala-sbt.org)  - Build Tools
 * [Scala](http://www.scala-lang.org) - Lenguaje
 * [Finagle](https://twitter.github.io/finagle/) - Platform Server
 * [TwitterServer](https://twitter.github.io/twitter-server/) - Server API.
 * [Activate](http://activate-framework.org) - Framework de Persitencia

## Bases de datos integradas:
* [Mysql](https://www.mysql.com)
* [MongoDB](https://www.mongodb.org)
* [Redis](http://redis.io) - Como cache


## Congiguración

Tenemos un package object que tiene la configuracion de toda la aplicación.
Muchas de esas configuraciones se leen de variables de entorno (aunque tienen un valor por default)

El objeto es:
```scala
package object configuration {
  object environment {
    val development
    val amazon
    val heroku

    val name
  }
  object server{
    val port
  }
  object database{
    val storage
    val fillData

    object mongo {
      val host
      val port
      val db
      val authentication
    }
    object mysql{
      val user
      val password
      val url
    }
    object redis{
      val host
      val port
    }
  }
}
```


## Ejecución
Para la ejecución hay que ejecutar el object `Server`.

#### Con la configuración por default
Por default el servidor va a escuchar el puerto `8081`. \
Para cambiar eso hay que setear la variable de entorno correspondiente o modificar el puerto por default:
```scala
val port = ":" + Properties.envOrElse("PORT", "8081")
```

Para elegir que motor de base de datos se quiere usar hay que modificar la variable `storage`:
```scala
val storage = Properties.envOrElse("storage", "transient")
```
Los valores posibles son:
* `transient` - Utiliza la memoria
* `mysql` - Utiliza Mysql
* `mongo` - Utiliza MongoDB

Por default la base que utilizada es `transient`.

##### Datos de Prueba.
Podemos elegir si queremos instanciar datos de prueba, puede ser para que cuando trabajemos en modo transient, tener datos en cada corrida o si elegimos alguna de las bases persistente para llenar la base con un pequeño set de datos. \
Para cambiar esta configuracion hay que setear la variable `fill_data`:

```scala
val fillData = Properties.envOrNone("fill_data").map(_=>false).getOrElse(true)
```

## Configuración de las bases de datos

#### MYSQL
Si queremos que se utilice `mysql` debemos configurar:
* `user` - Por default es root.
     ```scala
     val user = Properties.envOrSome("mysql_user", Some("root"))
    ```
* `password` - Por default es root.
    ```scala
     val password = Properties.envOrSome("mysql_pwd", Some("root"))
    ```
* `url` - Aca se configura host y schema :
     ```scala
     val url = Properties.envOrElse("mysql_url", "jdbc:mysql://localhost:8889/activate_test")
    ```

Las tablas las crea el software.

### MongoDB
Si queremos utilizar mongo hay que configurar:
* `host` - Por default es localhost.
     ```scala
     val host = Properties.envOrElse("mongo_host", "localhost")
    ```
* `port` - Por default es 27017.
    ```scala
     val port = Properties.envOrElse("mongo_posts", "27017")
    ```
* `db` - Por default es careprices :
     ```scala
     val db = Properties.envOrElse("mongo_db", "careprices")
    ```
* `authentication` - Por default es None  :
     ```scala
        val authentication = Properties.envOrNone("mongo_auth")
    ```

### Redis
Estamos usando **Redis** como cache de algunos recursos. Todavia no esta muy bien definido hasta que tengamos mas avanzado el TP. Por ahora lo usamos en un solo servicio. \
Si no tiene una conección con el servicio de Redis el servicio ejecuta el código  que genera el valor original. Es decir si no tenemos un servicio de Redis la aplicación sigue funcionando.

Para utilizar Redis necesitamos configurar:

* `host` - Por default es localhost.
     ```scala
     val host = Properties.envOrElse("redis_host", "localhost")
    ```
* `port` - Por default es 6379.
    ```scala
     val port = Properties.envOrElse("redis_port", "6379")
    ```

    