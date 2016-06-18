#!/usr/bin/env bash

if [ ! -z $1 ]
then
    configFile=$1
else
    configFile=default.mongo.config.ini
fi

. docker/mongo/$configFile

#echo "${configs}   ${replicas}"

replicas_list=$(echo $replicas | tr "," "\n")

shard_list=()
rs_list=()
currentReplicaNumber=1
net_name="careprices"

docker network create -d bridge --subnet 172.25.0.0/16 $net_name

for replicaSize in $replicas_list
do
    rs_connect=""
	for (( i=1; i<=$replicaSize; i++ ))
    do
        echo  "docker run --net=$net_name -itd --name rs$currentReplicaNumber.mongo$i -h rs$currentReplicaNumber.mongo$i.com --net-alias=rs$currentReplicaNumber.mongo$i.com -p 27017 -d mongo --replSet rs$currentReplicaNumber --smallfiles"
        docker run --net=$net_name -itd --name rs$currentReplicaNumber.mongo$i -h rs$currentReplicaNumber.mongo$i.com --net-alias=rs$currentReplicaNumber.mongo$i.com -p 27017 -d mongo --replSet rs$currentReplicaNumber --smallfiles

        if [ $i -eq 1 ]
          then
            shard_list+=("rs$currentReplicaNumber/rs$currentReplicaNumber.mongo$i.com")
            rs_connect+="rs$currentReplicaNumber.mongo$i"
           else
           rs_connect+=",rs$currentReplicaNumber.mongo$i"
        fi
    done
    ((currentReplicaNumber++))
    rs_list+=($rs_connect)
done


configdb="rs$currentReplicaNumber/"
rs_connect=""
for (( i=1; i<=$configs; i++ ))
do
    echo  "docker run --net=$net_name -itd --name rs$currentReplicaNumber.config$i -h rs$currentReplicaNumber.config$i.com --net-alias=rs$currentReplicaNumber.config$i.com -p 27017 -d mongo --configsvr --replSet rs$currentReplicaNumber --port 27017"
    docker run --net=$net_name -itd --name rs$currentReplicaNumber.config$i -h rs$currentReplicaNumber.config$i.com --net-alias=rs$currentReplicaNumber.config$i.com -p 27017 -d mongo --configsvr --replSet rs$currentReplicaNumber --port 27017
     if [ $i -eq 1 ]
      then
        configdb+="rs$currentReplicaNumber.config$i.com:27017"
        rs_connect+="rs$currentReplicaNumber.config$i"
      else
        configdb+=",rs$currentReplicaNumber.config$i.com:27017"
        rs_connect+=",rs$currentReplicaNumber.config$i"
    fi
done
rs_list+=($rs_connect)

echo "replicas"
for r in "${rs_list[@]}"
do
    index=0
    nodes=$(echo $r | tr "," "\n")
    for node in $nodes
    do
        if [ $index -eq 0 ]
          then
            targetNode=$node
            echo "docker exec -it $targetNode mongo --eval 'rs.initiate();'"
            docker exec -it $targetNode mongo --eval 'rs.initiate();'
          else
            echo "docker exec -it $targetNode mongo --eval \"rs.add('$node:27017');\""
            docker exec -it $targetNode mongo --eval "rs.add('$node:27017');"
        fi
        ((index++))
    done
done

echo "docker run --net=$net_name -itd --name mongo -h mongo.com --net-alias=mongo.com -p 27017:27017 -d lgatica/mongos --port 27017 --configdb $configdb"
docker run --net=$net_name -itd --name mongo -h mongo.com --net-alias=mongo.com -p 27017:27017 -d lgatica/mongos --port 27017 --configdb rs$currentReplicaNumber/$configdb

echo "sleep 20"
sleep 20

echo "sharding"
for t in "${shard_list[@]}"
do
    echo "mongo localhost:27017 --eval \"sh.addShard('$t');\""
    mongo localhost:27017 --eval "sh.addShard('$t');"
done
