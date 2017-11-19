/*
 * Copyright (C) 2017 the enviroCar community
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.envirocar.processing.ec4geomesa.ingestor;

import java.util.Map;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.envirocar.processing.ec4geomesa.ingestor.utils.MRJsonInputFormat;
import org.locationtech.geomesa.jobs.interop.mapreduce.GeoMesaOutputFormat;
import org.opengis.feature.simple.SimpleFeature;

/**
 *
 * @author dewall
 */
public class FileDataIngestorMR {

    public static void main(String[] args) throws Exception {

//        DataStore dataStore = DataStoreFinder.getDataStore(params);
//        dataStore.createSchema(t);
//        runIngestor("");
    }

    private static void runIngestor(String inputDir,
            Map<String, String> dataStoreConfig) throws Exception {
        Configuration config = new Configuration();

        Job job = Job.getInstance(config);
        job.setJobName("GeoMesa enviroCar Ingest");
        job.setJarByClass(FileDataIngestorMR.class);

        job.setMapperClass(FileDataIngestorMapper.class);
        job.setInputFormatClass(MRJsonInputFormat.class);
        job.setOutputFormatClass(GeoMesaOutputFormat.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputKeyClass(SimpleFeature.class);
        job.setNumReduceTasks(0);

        Path input = new Path(inputDir);
        FileInputFormat.setInputPaths(job, input);
        GeoMesaOutputFormat.configureDataStore(job, dataStoreConfig);

        job.submit();
        if (!job.waitForCompletion(true)) {
            throw new Exception("Job execution failed...");
        }
    }
}
