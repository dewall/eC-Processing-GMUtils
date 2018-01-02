# default parameters, replace these
ACCUMULO_PASS := pass

MAPSERVER_IMAGETAG = ec-mapmatching/mapserver
MAPSERVER_DATABASENAME := osm
MAPSERVER_USER := postgres
MAPSERVER_PASS := postgres

VOLUME_PATH_MAPSERVER := /var/lib/docker-compose/geomesa4ec/pgdata/
VOLUME_PATH_HDFS := /var/lib/docker-compose/geomesa4ec/hadoop/


build:
	cd barefoot-mapserver && make
	cd geomesa4ec-setup && make

clean:
	cd barefoot-mapserver && make clean
	cd geomesa4ec-setup && make clean