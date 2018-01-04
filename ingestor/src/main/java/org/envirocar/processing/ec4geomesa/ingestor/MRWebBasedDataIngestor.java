package org.envirocar.processing.ec4geomesa.ingestor;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
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
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.envirocar.processing.ec4geomesa.core.GeoMesaConfig;
import org.envirocar.processing.ec4geomesa.core.GeoMesaDataStoreModule;
import org.envirocar.processing.ec4geomesa.core.feature.MeasurementFeatureStore;
import org.envirocar.processing.ec4geomesa.core.feature.TrackFeatureStore;
import org.envirocar.processing.ec4geomesa.ingestor.input.DownloadTracksInputFormat;
import org.locationtech.geomesa.jobs.interop.mapreduce.GeoMesaOutputFormat;
import org.opengis.feature.simple.SimpleFeature;

/**
 *
 * @author dewall
 */
public class MRWebBasedDataIngestor {

    private static final Logger LOGGER = Logger.getLogger(MRWebBasedDataIngestor.class);

    public static final String OPTION_CHUNKSIZE = "chunksize";
    public static final String OPTION_LIMIT = "limit";
    public static final int OPTION_CHUNKSIZE_DEFAULT = 100;
    public static final int OPTION_LIMIT_DEFAULT = 100;

    public static void main(String[] args) throws Exception {
        BasicConfigurator.configure();
        Options options = getCLOptions();
        CommandLineParser parser = new BasicParser();
        CommandLine cmd = parser.parse(options, args);
        
        // Get option values
        int limit = getLimitOptionValue(cmd);
        int chunkSize = getChunkSizeOptionValue(cmd);
        
        Injector injector = Guice.createInjector(new GeoMesaDataStoreModule());
        Map<String, String> datastoreConfig = injector.getInstance(
                Key.get(Map.class, Names.named(GeoMesaConfig.GEOMESACONFIG)));

        TrackFeatureStore trackStore = injector
                .getInstance(TrackFeatureStore.class);
        trackStore.createTable();

        MeasurementFeatureStore measurementStore = injector
                .getInstance(MeasurementFeatureStore.class);
        measurementStore.createTable();

        runIngestor(limit, chunkSize, datastoreConfig);
    }

    private static Options getCLOptions() {
        return new Options()
                .addOption(OptionBuilder.withArgName(OPTION_LIMIT)
                        .hasArg()
                        .withDescription("Total number of tracks to ingest")
                        .create(OPTION_LIMIT))
                .addOption(OptionBuilder.withArgName(OPTION_CHUNKSIZE)
                        .hasArg()
                        .withDescription("Chunksize for track batches to download")
                        .create(OPTION_CHUNKSIZE));
    }

    private static int getLimitOptionValue(CommandLine cmd) {
        String limitValue = cmd.getOptionValue(OPTION_LIMIT);
        return limitValue != null ? Integer.parseInt(limitValue) : OPTION_LIMIT_DEFAULT;
    }

    private static int getChunkSizeOptionValue(CommandLine cmd) {
        String chunkValue = cmd.getOptionValue(OPTION_CHUNKSIZE);
        return chunkValue != null ? Integer.parseInt(chunkValue) : OPTION_CHUNKSIZE_DEFAULT;
    }

    private static void runIngestor(int limit, int chunksize, Map<String, String> datastoreConfig) throws IOException,
            InterruptedException, Exception {

        Configuration config = new Configuration();
        config.setInt(OPTION_LIMIT, limit);
        config.setInt(OPTION_CHUNKSIZE, chunksize);

        Job job = Job.getInstance(config);
        job.setJobName("GeoMesa enviroCar Ingestion");
        job.setJarByClass(MRWebBasedDataIngestor.class);

        job.setMapperClass(TracksDataIngestorMapper.class);
        job.setInputFormatClass(DownloadTracksInputFormat.class);
        job.setOutputFormatClass(GeoMesaOutputFormat.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputKeyClass(SimpleFeature.class);
        job.setNumReduceTasks(0);

        GeoMesaOutputFormat.configureDataStore(job, datastoreConfig);

        job.submit();

        LOGGER.info("Submitted Ingestion MR-Job for enviroCar Tracks.");
        if (!job.waitForCompletion(true)) {
            throw new Exception("Job execution failed...");
        }
    }
}
