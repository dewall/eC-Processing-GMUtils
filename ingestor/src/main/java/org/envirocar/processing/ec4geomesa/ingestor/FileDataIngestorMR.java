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
