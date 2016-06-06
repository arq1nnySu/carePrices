image=$1
name=$1
param=$2

if [ -z ${param} ]; then
   p=$3; h=$5;
else
   if [ $param = -h ]; then
      h=$3;
   else
      p=$3; h=$5; 
   fi
fi
  
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
  `expr match $image '\(careprices[0-9]\)'`) runCareprices ;;
  "mongo") runMongo ;;
  "mysql") runMysql ;;
  "redis") runRedis ;;
  *) showErrorImage ;;
esac

echo "docker run --name $image -p $port ${@:6} -h $host -d $name"
docker run --name $image -p $port ${@:6} -h $host -d $name


#--cpu-shares=0
#-m=2g
#sh run_image.sh careprices -m 2G --link mongo --link redis -e "storage=mongo"
