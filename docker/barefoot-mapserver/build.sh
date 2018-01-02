#!/usr/bin/env bash
set -e

INGEST_OSM=true
OSM_DL_LINK="http://download.geofabrik.de/europe/germany/nordrhein-westfalen-latest.osm.pbf"

docker_tag="ec-mapmatching/mapserver"

mapserver_dbname="osmdata"
mapserver_user="osmuser"
mapserver_pass="pass"
mapserver_mode="slim"

scriptDir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "${scriptDir}" #&& mkdir data

# parsing opts
# u = mapserver username
# p = mapserver password
# s = mapserver database name
# i = boolean value for data ingestion
# d = osm.pbf download link
while getopts u:p:s:i:d: option
do 
    case "${option}" in 
    u)  mapserver_user=${OPTARG};;
    p)  mapserver_pass=${OPTARG};;
    s)  mapserver_dbname=${OPTARG};;
    d)  OSM_DL_LINK=${OPTARG};;
    i)  if [ ${OPTARG} = "true" ] || [ ${OPTARG} = "false" ]; then
            INGEST_OSM=${OPTARG}
        fi;;
    esac
done


# build docker image
docker build -t="ec-mapmatching/mapserver" .

echo "Docker image has been successfully created";
if [ $INGEST_OSM = "false" ]; then
    return 0    


# run the docker image
docker run -d \
    -v $scriptDir/data:/data \
    --name mapserver \
    ec-mapmatching/mapserver 

# wait a certain amount of time for the database to be initialized...
sleep 15

# execute the import stuff...
docker exec -it mapserver /bin/bash --rcfile /root/.bashrc -ci \
    "wget -O /data/osm-data.osm.pbf $OSM_DL_LINK && /mnt/map/osm/import.sh /data/osm-data.osm.pbf $mapserver_dbname $mapserver_user $mapserver_pass /mnt/map/tools/road-types.json $mapserver_mode"

# wait a certain amount of time, otherwise it gets stuck
sleep 15

#... and commit the changes. 
docker commit mapserver ec-mapmatching/mapserver

# wait a certain amount of time
sleep 10

docker stop mapserver && docker rm mapserver