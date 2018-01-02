ACCUMULO_PASS := pass
VOLUME_PATH_MAPSERVER := /var/lib/docker-compose/geomesa4ec/pgdata/
VOLUME_PATH_HDFS := /var/lib/docker-compose/geomesa4ec/hadoop/

build: create-docker-compose

create-docker-compose: 
	sed 's@__VOLUME-MAPSERVER__@'"${VOL_MAP}"'@g; s@__ACUMMULO-PASS__@'"${ACCUMULO_PASS}"'@g; s@__VOLUME-HDFS__@'"${VOL_HDFS}"'@g' template/docker-compose.template.yml > ./docker-compose.yml

clean: 
	rm docker-compose.yml