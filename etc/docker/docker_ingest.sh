#!/bin/bash
docker exec -it docker_accumulo-master_1 hadoop jar /data/ingestor/target/ingestor-0.1.jar org.envirocar.processing.ec4geomesa.ingestor.MRWebBasedDataIngestor -limit 10