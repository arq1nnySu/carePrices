#!/bin/bash
image=$1

function buildCareprices {
    echo -e "\e[01;33m!!!!!!!!!!!!!!!!!!!!   Building Careprices   !!!!!!!!!!!!!!!!!!!!!\e[00m"
    docker-compose -f docker/docker-compose.yml build
}
function buildMongo {
    echo -e "\e[01;33m!!!!!!!!!!!!!!!!!!!!   Building mongo   !!!!!!!!!!!!!!!!!!!!!\e[00m"
    docker pull mongo
}
function buildMysql {
    echo -e "\e[01;33m!!!!!!!!!!!!!!!!!!!!   Building Mysql   !!!!!!!!!!!!!!!!!!!!!\e[00m"
    docker pull mysql
}
function buildRedis {
    echo -e "\e[01;33m!!!!!!!!!!!!!!!!!!!!   Building Redis   !!!!!!!!!!!!!!!!!!!!!\e[00m"
    docker pull redis
}
function showErrorImage {
    echo -e "\e[01;33m!!!!!!!!!!!!!!!!!!!!  Error en el parametro  !!!!!!!!!!!!!!!!!!!!!\e[00m"
    echo "Las posibles opciones:"
	echo "  * careprices"
	echo "  * mongo"
	echo "  * redis"
	echo "  * mysql"
	echo ""
	exit 1
}
case $image in
  "careprices") buildCareprices ;;
  "mongo") buildMongo ;;
  "mysql") buildMysql ;;
  "redis") buildRedis ;;
  *) showErrorImage ;;
esac