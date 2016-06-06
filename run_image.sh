image=$1
name=$1
port=''

function runCareprices {
    echo -e "\e[01;33m!!!!!!!!!!!!!!!!!!!!   Running Careprices   !!!!!!!!!!!!!!!!!!!!!\e[00m"
    name="docker_careprices "
    port="9200:9200 -e PORT=9200"
}

function runMongo {
    echo -e "\e[01;33m!!!!!!!!!!!!!!!!!!!!   Running mongo   !!!!!!!!!!!!!!!!!!!!!\e[00m"
    port="27017:27017"
}

function runMongos {
    echo -e "\e[01;33m!!!!!!!!!!!!!!!!!!!!   Running mongos   !!!!!!!!!!!!!!!!!!!!!\e[00m"
    port="27017:27017"
}


function runMysql {
    echo -e "\e[01;33m!!!!!!!!!!!!!!!!!!!!   Running Mysql   !!!!!!!!!!!!!!!!!!!!!\e[00m"
    port="3306:3306"
}

function runRedis {
    echo -e "\e[01;33m!!!!!!!!!!!!!!!!!!!!   Building Redis   !!!!!!!!!!!!!!!!!!!!!\e[00m"
    port="6379:6379"
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
  "careprices") runCareprices ;;
  "mongo") runMongo ;;
  "mysql") runMysql ;;
  "redis") runRedis ;;
  *) showErrorImage ;;
esac


echo "docker run --name $image -p $port ${@:2} -d $name"
docker run --name $image -p $port ${@:2} -d $name


#--cpu-shares=0
#-m=2g
#sh run_image.sh careprices -m 2G --link mongo --link redis -e "storage=mongo"
#docker run --name mongo1 -p 27017 -d mongo --replSet replSet --smallfiles
#docker run -P --name mongos -p 27017:27017 --link mongo1 --link mongo2 --link mongo3  -d lgatica/mongos --configdb replSet/mongo1:27017,mongo2:27017,mongo3:27017

#rs.initiate()
#rs.add("172.17.0.2:27017")
#rs.add("172.17.0.3:27017")