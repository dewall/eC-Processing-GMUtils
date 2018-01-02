MAPSERVER_IMAGETAG := ec-mapmatching/mapserver

build:
    build-docker

build-docker:
    docker build \
        -t=${MAPSERVER_IMAGETAG} \
        --build-arg DATABASE=${MAPSERVER_DATABASENAME} \
        --build-arg USER=${MAPSERVER_USER} \
        --build-arg PASSW=${MAPSERVER_PASSW} \
        .

ingest-osm:

clean: