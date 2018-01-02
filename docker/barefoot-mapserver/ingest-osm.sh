
scriptDir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "${scriptDir}" 

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
