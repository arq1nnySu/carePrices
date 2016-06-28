#!/bin/bash

# Escenario resistencia y tolerancia a fallos - 1 nodo virtual de aplicación con 1 CPU; y 3 nodos de BD en réplica (2) => 1 master y 2 slave.

# levantamos el custer con 3 nodos de mongo en réplica (2)

../start_custer.sh case2b.mongo.config.ini

# Levantamos la imagen de redis - para usar mongo con redis

../run_image.sh redis

# Levantamos la app careprices y la conectamos con mongo y redis 

../run_image.sh careprices -h carepricesC2B --link mongo -e "storage=mongo" --link redis -m 1G --cpuset-cpus="0"