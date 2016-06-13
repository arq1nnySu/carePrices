#!/usr/bin/env bash

#sudo route -n add -net 172.17.0.0 192.168.99.100
#osx
sudo route -n add -net 172.17.0.0 192.168.99.100
#linux
sudo route -net 172.17.0.0 netmask 255.255.0.0 gw 10.2.0.10

docker run -d -p 172.17.0.0:53:53/udp --name skydns crosbymichael/skydns -nameserver 8.8.8.8:53 -domain docker
docker run -d -v /var/run/docker.sock:/docker.sock --name skydock crosbymichael/skydock -ttl 30 -environment dev -s /docker.sock -domain docker -name skydns

#docker run --name mongo.rs1.0 -h rs1-0.mongo -p 27017 -v /usr/local/mongodb:/data/db -d mongo --replSet rs1 --smallfiles

docker run --name rs1-0.mongo -h 172.17.0.2 --dns 172.17.0.0 -p 27017 -d mongo --replSet rs1 --smallfiles
docker run --name rs1-1.mongo -h 172.17.0.3 --dns 172.17.0.0 -p 27017 -d mongo --replSet rs1 --smallfiles
docker run --name rs1-2.mongo -h 172.17.0.4 --dns 172.17.0.0 -p 27017 -d mongo --replSet rs1 --smallfiles

docker exec -it rs1-0.mongo bash
mongo
rs.initiate()
rs.add("172.17.0.3:27017")
rs.add("172.17.0.4:27017")
exit
exit

docker run --name rs2-0.mongo -h 172.17.0.5 -p 27017 -d mongo --replSet rs2 --smallfiles
docker run --name rs2-1.mongo -h 172.17.0.6 -p 27017 -d mongo --replSet rs2 --smallfiles
docker run --name rs2-2.mongo -h 172.17.0.7 -p 27017 -d mongo --replSet rs2 --smallfiles

docker exec -it rs2-0.mongo bash
mongo
rs.initiate()
rs.add("172.17.0.6:27017")
rs.add("172.17.0.7:27017")
exit
exit

docker run --name rs3-0.mongo  -h  172.17.0.8 -p 27017 -d mongo --configsvr --replSet rs3 --port 27017
docker run --name rs3-1.mongo  -h  172.17.0.9 -p 27017 -d mongo --configsvr --replSet rs3 --port 27017

docker exec -it rs3-0.mongo bash
mongo
rs.initiate()
rs.add("172.17.0.9:27017")
exit
exit

docker run --name mongo -h  172.17.0.10 -p 27017:27017 -d lgatica/mongos --port 27017 --configdb rs3/172.17.0.8:27017,172.17.0.9:27017

mongo --host 192.168.99.100:27017
sh.addShard("rs1/172.17.0.2:27017" )
sh.addShard("rs2/172.17.0.6:27017" )

docker run --name careprices -h careprices -p 9200:9200  --link mongo --link redis -m 2G -e PORT=9200 -e storage=mongo -d docker_careprices