package org.envirocar.processing.ec4geomesa.ingestor;

import com.google.inject.Guice;
import com.google.inject.Injector;
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
import org.envirocar.processing.ec4geomesa.core.GeoMesaStoreModule;
import org.envirocar.processing.ec4geomesa.core.feature.MeasurementFeatureStore;
import org.envirocar.processing.ec4geomesa.core.feature.TrackFeatureStore;
import org.envirocar.processing.ec4geomesa.ingestor.input.DownloadTracksInputFormat;
import org.geotools.data.DataStore;
import org.locationtech.geomesa.jobs.interop.mapreduce.GeoMesaOutputFormat;
import org.opengis.feature.simple.SimpleFeature;

/**
 *
 * @author dewall
 */
public class DownloadTracksDataIngestorMR {

    public static final String OPTION_LIMIT = "limit";
    public static final int OPTION_LIMIT_DEFAULT = 100;

    public static void main(String[] args) throws Exception {
        Options options = getCLOptions();
        CommandLineParser parser = new BasicParser();
        CommandLine cmd = parser.parse(options, args);
        int limit = getLimitOptionValue(cmd);

        Map<String, String> datastoreConfig = DataStoreInstanceHandler.getDefaultDataStoreConf();
        System.out.println("started");
        Injector injector = Guice.createInjector(new GeoMesaStoreModule(datastoreConfig));
        System.out.println("started");
        DataStore instance = injector.getInstance(DataStore.class);
        System.out.println("started");
        injector.getInstance(MeasurementFeatureStore.class);
        System.out.println("started");
        injector.getInstance(TrackFeatureStore.class);

        runIngestor(limit, datastoreConfig);
    }

    private static Options getCLOptions() {
        return new Options()
                .addOption(OptionBuilder.withArgName(OPTION_LIMIT)
                        .hasArg()
                        .withDescription("number of tracks to ingest")
                        .create("limit"));
    }

    private static int getLimitOptionValue(CommandLine cmd) {
        String limitValue = cmd.getOptionValue(OPTION_LIMIT);
        return limitValue != null ? Integer.parseInt(limitValue) : OPTION_LIMIT_DEFAULT;
    }

    private static void runIngestor(int limit, Map<String, String> datastoreConfig) throws IOException,
            InterruptedException, Exception {

        Configuration config = new Configuration();
        config.setInt(OPTION_LIMIT, limit);

        Job job = Job.getInstance(config);
        job.setJobName("GeoMesa enviroCar Ingestion");
        job.setJarByClass(DownloadTracksDataIngestorMR.class);

        job.setMapperClass(TracksDataIngestorMapper.class);
        job.setInputFormatClass(DownloadTracksInputFormat.class);
        job.setOutputFormatClass(GeoMesaOutputFormat.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputKeyClass(SimpleFeature.class);
        job.setNumReduceTasks(0);

        GeoMesaOutputFormat.configureDataStore(job, datastoreConfig);

        job.submit();
        System.out.println("submitted");
        if (!job.waitForCompletion(true)) {
            throw new Exception("Job execution failed...");
        }
    }
}
