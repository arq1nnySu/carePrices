#!/bin/bash

# Escenario 2A - 1 nodo virtual de aplicación con 1 CPU; y 2 nodos de BD en réplica (1) => 1 master y 1 slave.

# levantamos el custer con 2 nodos de BD en réplica (1)

../start_custer.sh case2a.mongo.config.ini

# Levantamos la imagen de redis - para usar mongo con redis

../run_image.sh redis

# Ahora si estamos listos para levantar la app como pide el Caso 1A: Pruebas de stress:

# TODO preguntar porque no puedo setear el tamanio de swap 
sleep 20

# v1 config CPU

../run_image.sh careprices -h carepricesC2A --link mongo -e "storage=mongo" --link redis -m 1G --cpu-shares=1

# v2 config CPU

#../run_image.sh careprices -h carepricesmaster --link mongo -e "storage=mongo" --link redis -m 300M --cpu-period=1

# v3 config cpu

#../run_image.sh careprices -h carepricesmaster --link mongo -e "storage=mongo" --link redis -m 300M --cpu-quota=1
