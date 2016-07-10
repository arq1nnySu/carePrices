#!/bin/bash

# Escenario 2A - 1 nodo virtual de aplicación con 1 CPU; y 2 nodos de BD en réplica (1) => 1 master y 1 slave.

# levantamos el custer con 2 nodos de BD en réplica (1)

../start_custer.sh case2a.mongo.config.ini

# Levantamos la imagen de redis - para usar mongo con redis

../run_image.sh redis

# Levantamos la app careprices y la conectamos con mongo y redis 

../run_image.sh careprices -h carepricesC2A --link mongo -e "storage=mongo" --link redis -m 1G --cpuset-cpus="0"
