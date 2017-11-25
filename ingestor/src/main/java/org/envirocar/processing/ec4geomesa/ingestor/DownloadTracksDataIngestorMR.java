package org.envirocar.processing.ec4geomesa.ingestor;

import java.io.IOException;
import java.util.Map;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.envirocar.processing.ec4geomesa.core.DataStoreInstanceHandler;
import org.envirocar.processing.ec4geomesa.core.feature.MeasurementFeatureProfile;
import org.envirocar.processing.ec4geomesa.core.feature.TrackFeatureProfile;
import org.envirocar.processing.ec4geomesa.ingestor.input.ECRemoteInputFormat;
import org.locationtech.geomesa.jobs.interop.mapreduce.GeoMesaOutputFormat;
import org.opengis.feature.simple.SimpleFeature;

/**
 *
 * @author dewall
 */
public class DownloadTracksDataIngestorMR {

    public static void main(String[] args) throws Exception {
        Options options = getCLOptions();
        CommandLineParser parser = new BasicParser();
        CommandLine cmd = parser.parse(options, args);
        String limit = cmd.getOptionValue("limit");

        runIngestor(limit);
    }

    private static Options getCLOptions() {
        return new Options()
                .addOption(OptionBuilder.withArgName("limit")
                        .hasArg()
                        .withDescription("number of tracks to ingest")
                        .create("limit"));
    }

    private static void runIngestor(String numberOfRecords) throws IOException, InterruptedException, Exception {
        DataStoreInstanceHandler datastore = DataStoreInstanceHandler.getDefaultInstance();
        datastore.createFeatureSchema(new MeasurementFeatureProfile());
        datastore.createFeatureSchema(new TrackFeatureProfile());

        Map<String, String> datastoreConfig = datastore.getDatastoreConfig();
        Configuration config = new Configuration();
        config.set("limit", numberOfRecords);

        Job job = Job.getInstance(config);
        job.setJobName("GeoMesa enviroCar Ingestion");
        job.setJarByClass(DownloadTracksDataIngestorMR.class);

        job.setMapperClass(TracksDataIngestorMapper.class);
        job.setInputFormatClass(ECRemoteInputFormat.class);
        job.setOutputFormatClass(GeoMesaOutputFormat.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputKeyClass(SimpleFeature.class);
        job.setNumReduceTasks(0);

        GeoMesaOutputFormat.configureDataStore(job, datastoreConfig);

        job.submit();
        if (!job.waitForCompletion(true)) {
            throw new Exception("Job execution failed...");
        }
    }
}
