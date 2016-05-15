#!/bin/sh -e

database=""
cache=""
app=""
build=""

function selectDataBase () {
    case $1 in
      "mysql") selectMysqlMode ;;
      "mongo") selectMongoMode ;;
      "transient") selectMongoMode ;;
      *) showErrorDataBase ;;
    esac
}

function selectMysqlMode(){
    echo -e "\e[01;33m!!!!!!!!!!!!!!!!!!!!   PERSISTENCIA CON MYSQL   !!!!!!!!!!!!!!!!!!!!!\e[00m"
    database=" -f docker/docker-compose-mysql.yml "
}

function selectMongoMode(){
    echo -e "\e[01;33m!!!!!!!!!!!!!!!!!!!!   PERSISTENCIA CON MONGODB   !!!!!!!!!!!!!!!!!!!!!\e[00m"
    database=" -f docker/docker-compose-mongodb.yml "
}

function selectTransientMode(){
    echo -e "\e[01;33m!!!!!!!!!!!!!!!!!!!!   PERSISTENCIA EN MEMORIA   !!!!!!!!!!!!!!!!!!!!!\e[00m"
    database=" "
}

function showErrorDataBase(){
    echo -e "\e[01;33m!!!!!!!!!!!!!!!!!!!!   Error en el parametro -d o --database    !!!!!!!!!!!!!!!!!!!!!\e[00m"
    echo "Las posibles opciones:"
	echo "  * mysql"
	echo "  * mongo"
	echo "  * transient"
	echo "  * No pasar ese parametro, por default es transient"
	echo ""
	exit 1
}


function selectCache(){
    case $1 in
      "redis") selectRedisCache ;;
      *) showErrorCache ;;
    esac
}

function selectRedisCache(){
    echo -e "\e[01;33m!!!!!!!!!!!!!!!!!!!!   CACHE CON REDIS   !!!!!!!!!!!!!!!!!!!!!\e[00m"
    cache=" -f docker/docker-compose-redis.yml "
}

function showErrorCache(){
    echo -e "\e[01;33m!!!!!!!!!!!!!!!!!!!!  Error en el parametro -c o --cache    !!!!!!!!!!!!!!!!!!!!!\e[00m"
    echo "Las posibles opciones:"
	echo "  * redis"
	echo "  * No pasar ese parametro, por default no usa cache"
	echo ""
	exit 1
}


function selectApp(){
    case $1 in
      "careprices") selectCareprices ;;
      *) showErrorCareprices ;;
    esac
}

function selectCareprices(){
    echo -e "\e[01;33m!!!!!!!!!!!!!!!!!!!!   RUN APP  !!!!!!!!!!!!!!!!!!!!!\e[00m"
    app=" -f docker/docker-compose.yml "
    build=" --build "
}

function showErrorCareprices(){
    echo -e "\e[01;33m!!!!!!!!!!!!!!!!!!!!  Error en el parametro -app !!!!!!!!!!!!!!!!!!!!!\e[00m"
    echo "Las posibles opciones:"
	echo "  * careprices"
	echo ""
	exit 1
}

while [ "$#" -gt 0 ]; do
  case "$1" in
    -d) database="$2"; selectDataBase $database; shift 2 ;;
    -c) cache="$2"; selectCache $cache; shift 2;;
    -app) app="$2"; selectApp $app; shift 2;;

    --database=*) database="${1#*=}"; selectDataBase $database; shift 1;;
    --cache=*) cache="${1#*=}"; selectCache $cache; shift 1;;
    -*) echo "unknown option: $1" >&2; exit 1;;
    *) echo "unknown option: $1"; exit 1;;
  esac
done


docker-compose $app $database $cache up -d $build
