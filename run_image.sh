#!/bin/bash

image=$1
name=$1
p2=$2
p3=$4
n=2

case $p2 in
  "-h") h=$3; n=4; ;;
  "-p") p=$3; n=4; ;;
esac

case $p3 in
  "-h") h=$5; n=6; ;;
  "-p") p=$5; n=6; ;;
esac

function runCareprices {
    echo -e "\e[01;33m!!!!!!!!!!!!!!!!!!!!   Running Careprices   !!!!!!!!!!!!!!!!!!!!!\e[00m" 
    setHost "careprice"
    setPort 9200  
    name="docker_careprices"
    port="$p:$p -e PORT=$p"
    host="$h"
}

function runMongo {
    echo -e "\e[01;33m!!!!!!!!!!!!!!!!!!!!   Running mongo   !!!!!!!!!!!!!!!!!!!!!\e[00m"
    setHost "mongo"
    setPort 27017
    port="$p:$p"    
    host="$h"
}


function runMysql {
    echo -e "\e[01;33m!!!!!!!!!!!!!!!!!!!!   Running Mysql   !!!!!!!!!!!!!!!!!!!!!\e[00m"
    setHost "mysql"
    setPort 3306
    port="$p:$p"
    host="$h"
}

function runRedis {
    echo -e "\e[01;33m!!!!!!!!!!!!!!!!!!!!   Building Redis   !!!!!!!!!!!!!!!!!!!!!\e[00m"
    setHost "redis"
    setPort 6379 
    port="$p:$p"
    host="$h"
}

function setPort () {
   if [ -z ${p} ]; then
      p=$1;
   fi
}

function setHost () {
   if [ -z ${h} ]; then
      h=$1;
   fi
}

function showErrorImage {
    echo -e "\e[01;33m!!!!!!!!!!!!!!!!!!!!  Error en el parametro  !!!!!!!!!!!!!!!!!!!!!\e[00m"
    echo "Las posibles opciones:"
	echo "  * careprices*"
	echo "  * mongo"
	echo "  * redis"
	echo "  * mysql"
	echo ""
	exit 1
}

case $image in
  *careprices*) runCareprices ;;
  "mongo") runMongo ;;
  "mysql") runMysql ;;
  "redis") runRedis ;;
  *) showErrorImage ;;
esac

echo "docker run --name $image -p $port ${@:$n} -h $host -d $name"
docker run --name $image -p $port ${@:$n} -h $host -d $name


#--cpu-shares=0
#-m=2g
#sh run_image.sh careprices -m 2G --link mongo --link redis -e "storage=mongo"

#Ejecutar mongo con replica

#docker run --name mongo  -h 172.17.0.5 -p 27017:27017 -d mongo --replSet rs1 --smallfiles --port 27017

#docker run --name mongo_rs1_1 -h 172.17.0.2 -p 27017 -d mongo --replSet rs1 --smallfiles
#docker run --name mongo_rs1_2 -h 172.17.0.3 -p 27017 -d mongo --replSet rs1 --smallfiles
#docker run --name mongo_rs1_3 -h 172.17.0.4 -p 27017 -d mongo --replSet rs1 --smallfiles

#docker run --name mongo_cfg1 -h  172.17.0.5 -p 27017 -d mongo --configsvr --replSet rs1 --dbpath /data/db --port 27017

#docker run --name mongo_rs2_1 -h 172.17.0.6 -p 27017 -d mongo --replSet rs2 --smallfiles
#docker run --name mongo_rs2_2 -h 172.17.0.7 -p 27017 -d mongo --replSet rs2 --smallfiles
#docker run --name mongo_rs2_3 -h 172.17.0.8 -p 27017 -d mongo --replSet rs2 --smallfiles

#docker run --name mongo_cfg2 -h  172.17.0.9 -p 27017 -d mongo --configsvr --replSet rs2 --dbpath /data/db --port 27017



#docker run --name mongo -h  172.17.0.6 -p 27017:27017 -d lgatica/mongos --port 27017 --configdb 172.17.0.5:27017

#docker run --name careprices -h careprices -p 9200:9200  --link mongo  -m 2G -e PORT=9200 -e storage=mongo -d docker_careprices

#
#rs.initiate()
#rs.add("172.17.0.2:27017")
#rs.add("172.17.0.3:27017")
#rs.add("172.17.0.4:27017")

#rs.add("172.17.0.6:27017")
#rs.add("172.17.0.7:27017")

#docker exec -i -t container_id bash

#./run_image.sh careprices -h carepricesmaster -m=1G --cpu-shares=2 --link mongo -e "storage=mongo" --link redis