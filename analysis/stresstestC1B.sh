#!/bin/bash

# Escenario 1B - 1 nodo virtual de aplicación con 2 CPU; y 1 nodo de BD

# creamos la red careprices

docker network create -d bridge --subnet 172.25.0.0/16 careprices

# Levantamos la imagen de mongo

../run_image.sh mongo

# Levantamos la imagen de redis - para usar mongo con redis

../run_image.sh redis

# Levantamos la app careprices y la conectamos con mongo y redis 

../run_image.sh careprices -h carepricesC1B --link mongo -e "storage=mongo" --link redis -m 300M --cpuset-cpus="0,1" #--cpu-shares=