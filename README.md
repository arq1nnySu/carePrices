
[![Build Status](https://travis-ci.org/arq1nnySu/carePrices.svg)](https://travis-ci.org/arq1nnySu/carePrices)


# Mirar para cuidar

## Instancia
* [Heroku](http://careprices.herokuapp.com) - Servidor hosteado en Heroku.

## Monitoreo
* [NewRelic](https://rpm.newrelic.com/accounts/1280223/applications)

## Logging
* [Logentries](https://logentries.com/app/0ab5f9c6)


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
  object response{
    val limitSize
    val maxLimitSize
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

Las herramientas que estamos utilizando son:
* `docker`
* `docker-compose`

Estamos utilizando además de nuestra imágen, las siguientes imágenes:
* `MongoDB`
* `Mysql`
* `Redis`

Cada una de esas imágenes corre en un contenedor distinto y se las puede conectar por red.

#### Levantar la imágen Docker

Lo primero que tenemos que asegurarnos es que tenemos todas las imágenes que queremos utilizar.
Para ello tenemos un script que baja y configura cada imágen:

* `sh build_image.sh careprices` - Baja la imágen de `Ubuntu` y la configura para poder correr ahí nuestra app.
* `sh build_image.sh mongo` - Baja la imágen de `mongo`.
* `sh build_image.sh mysql` - Baja la imágen de `mysql`.
* `sh build_image.sh redis` - Baja la imágen de `redis`.

Una vez que bajamos las imágenes que queremos utilizar, podemos levantarlas.
Para ello también contamos con un script:

  * `sh run_image.sh mongo -m 2G` - Levanta la imágen de mongo en un contenedor con 2 GB de ram
  * `sh run_image.sh redis -m 1G` - Levanta la imágen de redis en un contenedor con 1 GB de ram.
  * `sh run_image.sh careprices -m 2G ` - Levanta la imágen de la app en un 
  contenedor con 2GB de ram. Si queremos usar mongo le agregamos los siguientes parámetros: `--link mongo -e "storage=mongo"`. 
  Si queremos utilizar la cache de redis: `--link redis`

Ahora con este mismo script podemos setear `hostname` y el `puerto` de la siguiente manera:

  * `sh run_image.sh mongo -h mongoMaster` - Levanta la imágen de mongo con el hostname: mongoMaster.
  * `sh run_image.sh redis -p 2345` - Levanta la imágen de redis en el puerto: 2345.
  * `sh run_image.sh careprices -p 9300 -h careprices1` - Levanta la imágen de la app en el puerto 9300 con el hostname careprices1.

  Si levantamos las imagenes sin ninguno de estos parametros, cada una de las imagenes tiene un puerto y un hostname por defecto.
  
  
  Nuestros contenedores se crean en una red llamada `careprices` para que todos esten en la misma red.
  A cada contenedor le configuramos un alias en la red para el dns interno.
  Por ello, es posible que al levantar las imágenes tire un error de que la red ya esta creada, pero lo hacemos para que se cree si no existe.
  
#### Cluster de Mongo

Tenemos un script `start_cluster.sh` que crea un cluster de mongo. A este script le podemos decir con que configuracion queremos levantar el cluster.
Por default levanta la configuración: `docker/mongo/default.mongo.conf` que tiene el siguiente contenido:

```bash
     configs=2
     replicas=3,3
```

Esto significa que:
* Crea 3 instancias de Mongo Config Server
* Crea 3 Replicas de 3 nodos caada una
* Crea un sharding con las 3 replicas
* Levanta el MongoS y lo deja listo para que nuestra app lo pueda usar.

El diagrama del cluster quedaría asi:


  
### Monitoreo 

Tenemos integrado **newrelic** en la imagen de docker, para monitorear el server y en la app.<br/>
La información del server se encuentra en la sección **Servers** de **newrelic**.<br/>
La información de la aplicación se encuentra en la sección **APM** de **newrelic** con el nombre de  `Careprice (Developmnent)`.

Para el monitoreo de la aplicación tenemos dos integraciones:

* El primero esta integrado al server container, que monotorea todo el estado de la app. Pero esta integración no es completa, 
    muestra todas las transacciones en una, bajo el nombre de **/NettyDispatcher** (aparentemente es una limitación del framework elegido).

* El segundo es utilizando la api de **newrelic** creamos un `filtro` para tracear todos los request y responses que realiza nuestra app.


### Test de carga.
Tenemos un plan de **JMeter** (en desarrollo) para poder medir el límite junto con los tiempos de respuesta y hacer 
una comparación de del rendimiento utilizando las distintas bases de datos e incluyendo o no la caché.<br/>
  El archivo esta en `jmeter/stressTest.jmx`


### Logging

Loggeamos a logentries.