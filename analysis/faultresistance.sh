#!/bin/bash

# Escenario Resistencia y tolerancia a fallos - 1 nodo virtual de aplicación con 1 CPU; y 3 nodos de BD en réplica (2) => 1 master y 2 slave.

# levantamos el custer con 3 nodos de BD en réplica (2)

../start_custer.sh case2b.mongo.config.ini

# Levantamos la imagen de redis - para usar mongo con redis

../run_image.sh redis

# Ahora si estamos listos para levantar la app como pide el Caso 1A: Pruebas de stress:

# TODO preguntar porque no puedo setear el tamanio de swap 
sleep 20

# v1 config CPU

../run_image.sh careprices -h carepricesC2B --link mongo -e "storage=mongo" --link redis -m 300M --cpu-shares=1

# v2 config CPU

#../run_image.sh careprices -h carepricesmaster --link mongo -e "storage=mongo" --link redis -m 300M --cpu-period=1

# v3 config cpu

#../run_image.sh careprices -h carepricesmaster --link mongo -e "storage=mongo" --link redis -m 300M --cpu-quota=1
