# Mirar para cuidar

## Instancias
* [Heroku](http://careprices.herokuapp.com) - Servidor hosteado en Heroku.
* [Docker](https://hub.docker.com/r/nnysu/careprices) - Docker Image

## Documentación de la API:
* [API](http://docs.careprices.apiary.io/#reference/0/product/create-a-new-product?console=1)

## Stack elegído

 * [SBT](http://www.scala-sbt.org)  - Build Tools
 * [Scala](http://www.scala-lang.org) - Lenguaje
 * [Finagle](https://twitter.github.io/finagle/) - Platform Server
 * [TwitterServer](https://twitter.github.io/twitter-server/) - Server API.
 * [Activate](http://activate-framework.org) - Framework de Persitencia

## Bases de datos integradas:
* [Mysql](https://www.mysql.com)
* [MongoDB](https://www.mongodb.org)
* [Redis](http://redis.io) - Como cache


## Configuración

Tenemos un package object que tiene la configuracion de toda la aplicación.
Muchas de esas configuraciones se leen de variables de entorno (aunque tienen un valor por default)

El objeto es:
```scala
package object configuration {
  object environment {
    val development
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
Por default la app levanta en el puerto `9200`.

* Desde **IntelliJ**:
    Para la ejecución hay que ejecutar el object `Server`.
* Desde la consola:
    Correr el comando `sbt run` y la app levanta.
* Desde la consola generando un jar:
    Correr el comando  ```sbt assembly ``` que generra el archivo  `carePrices.jar`. Una vez generado se puede correr el jar ejecutando: `java -jar carePrices.jar`





#### Con la configuración por default
Por default el servidor va a escuchar el puerto `9200`.
Para cambiar eso hay que setear la variable de entorno correspondiente o modificar el puerto por default:
```scala
val port = ":" + Properties.envOrElse("PORT", "9200")
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
--       
* `password` - Por default es root.
    
    ```scala
    val password = Properties.envOrSome("mysql_pwd", Some("root"))
    ```
--
* `schema` - Aca se configura el Schema:

    ```scala
    val schema = Properties.envOrElse("mysql_schema", "careprices")
    ```
--
* `url` - Aca se configura Host:
   
    ```scala
    val url = Properties.envOrElse("mysql_url", "jdbc:mysql://localhost:8889/")
    ```

Las tablas las crea el software.

### MongoDB
Si queremos utilizar mongo hay que configurar:
* `host` - Por default es localhost.

    ```scala
     val host = Properties.envOrElse("mongo_host", "localhost")
    ```
--    
* `port` - Por default es 27017.

    ```scala
     val port = Properties.envOrElse("mongo_posts", "27017")
    ```
--
* `db` - Por default es careprices :
    
    ```scala
     val db = Properties.envOrElse("mongo_db", "careprices")
    ```
--
* `authentication` - Por default es None:
    
    ```scala
        val authentication = Properties.envOrNone("mongo_auth")
    ```

### Redis
Estamos usando **Redis** como cache de algunos recursos. Todavia no esta muy bien definido hasta que tengamos mas avanzado el TP.
Por ahora lo usamos en un solo servicio, el de precios.
Si no hay un servidor de redis, no pasa nada, se ignora la chaché y se ejecuta el código original.
Es decir si no tenemos un servicio de Redis la aplicación sigue funcionando.

Para utilizar Redis necesitamos configurar:

* `host` - Por default es localhost.
    ```scala
     val host = Properties.envOrElse("redis_host", "localhost")
    ```
* `port` - Por default es 6379.
    ```scala
     val port = Properties.envOrElse("redis_port", "6379")
    ```


### Docker

Nuestra app corre en una imagen docker con las siguientes catacterísticas:
* Ubuntu 15.10
* Java 8

Estamos usando `docker-compose` para describir algunas dependencias de imágenes:
  * MongoDB
  * Mysql
  * Redis

Cada una de esas imágenes corre en un contenedor distinto que la app y se comunican por red.
Lo que falta de configurar es pasar las variables de entorno  de nuestra app para configurar la base de datos a utilizar.

Lo que no sabemos bien es si es mejor tener un contenedor por servicio o todos los servicios en el mismo contenedor/imagen.


#### Levantar la imágen Docker

Lavantar la imágen tenemos un script `docker_run.sh` que compila las imágenes y las levanta.
Podemos configurar para que utilice la base de datos que querramos o si se quiere utilizar a redis como cache.
Las formas son:

  * `docker_run.sh -d mysql` o `docker_run.sh --database=mysql` - Con esto nuestra app utiliza **mysql** como base de datos.
  * `docker_run.sh -d mongo` o `docker_run.sh --database=mongo` - Con esto nuestra app utiliza **mongodb** como base de datos.
  * `docker_run.sh -c redis` o `docker_run.sh --cache=redis` - Con esto nuestra app utiliza **redis** como cache.
  * Tambien podemos combinar **mysql** con **redis** p **mongo** con **redis**: `docker_run.sh -d mongo -c redis`.

