#!/bin/bash

# Escenario 1A - 1 nodo virtual de aplicación con 1 CPU; y 1 nodo de BD

# creamos la red careprices

docker network create -d bridge --subnet 172.25.0.0/16 careprices

# Levantamos la imagen de mongo

../run_image.sh mongo

# Levantamos la imagen de redis - para usar mongo con redis

../run_image.sh redis

# Ahora si estamos listos para levantar la app como pide el Caso 1A: Pruebas de stress:

# TODO preguntar porque no puedo setear el tamanio de swap = 0

# v1 config CPU

../run_image.sh careprices -h carepricesC1A --link mongo -e "storage=mongo" --link redis -m 300M --cpu-shares=1

# v2 config CPU

#../run_image.sh careprices -h carepricesmaster --link mongo -e "storage=mongo" --link redis -m 300M --cpu-period=1

# v3 config cpu

#../run_image.sh careprices -h carepricesmaster --link mongo -e "storage=mongo" --link redis -m 300M --cpu-quota=1
