VOL_MAP := /var/lib/docker-compose/geomesa4ec/pgdata/
VOL_HDFS := /var/lib/docker-compose/geomesa4ec/hadoop/

build: create-docker-compose

create-docker-compose: 
	sed 's@__VOLUME-MAPSERVER__@'"${VOL_MAP}"'@g; s@__VOLUME-HDFS__@'"${VOL_HDFS}"'@g' template/docker-compose.template.yml > ./docker-compose.yml

clean: 
	rm docker-compose.yml