package org.envirocar.processing.ec4geomesa.ingestor;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import java.util.Map;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.envirocar.processing.ec4geomesa.core.GeoMesaDB;
import org.envirocar.processing.ec4geomesa.core.guice.DataStoreModule;
import org.envirocar.processing.ec4geomesa.core.guice.annotations.TrackType;
import org.envirocar.processing.ec4geomesa.ingestor.input.MRJsonInputFormat;
import org.locationtech.geomesa.jobs.interop.mapreduce.GeoMesaOutputFormat;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 *
 * @author dewall
 */
public class MRFileBasedDataIngestor {

    public static void main(String[] args) throws Exception {
        Options options = getCLOptions();
        CommandLineParser parser = new BasicParser();
        CommandLine cmd = parser.parse(options, args);
        String inputDir = cmd.getOptionValue("inputDir");

        Injector injector = Guice.createInjector(new DataStoreModule());
        Map<String, String> datastoreConfig = injector.getInstance(
                Key.get(Map.class, Names.named(GeoMesaDB.GEOMESACONFIG)));

        initializeTables(injector);

        runIngestor(inputDir, datastoreConfig);
    }

    private static void initializeTables(Injector injector) {
        // transitively initialize table over aop
        SimpleFeatureType trackFeature = injector.getInstance(
                Key.get(SimpleFeatureType.class, TrackType.class));
        SimpleFeatureType measurementFeature = injector.getInstance(
                Key.get(SimpleFeatureType.class, TrackType.class));
    }

    private static Options getCLOptions() {
        return new Options()
                .addOption(OptionBuilder.withArgName("inputDir")
                        .hasArg()
                        .isRequired()
                        .withDescription("track file on hdfs for ingestion")
                        .create("inputDir"));
    }

    private static void runIngestor(String inputDir, Map<String, String> datastoreConfig) throws Exception {
        Configuration config = new Configuration();

        Job job = Job.getInstance(config);
        job.setJobName("GeoMesa enviroCar Ingest");
        job.setJarByClass(MRFileBasedDataIngestor.class);

        job.setMapperClass(TracksDataIngestorMapper.class);
        job.setInputFormatClass(MRJsonInputFormat.class);
        job.setOutputFormatClass(GeoMesaOutputFormat.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputKeyClass(SimpleFeature.class);
        job.setNumReduceTasks(0);

        Path input = new Path(inputDir);
        FileInputFormat.setInputPaths(job, input);
        GeoMesaOutputFormat.configureDataStore(job, datastoreConfig);

        job.submit();
        if (!job.waitForCompletion(true)) {
            throw new Exception("Job execution failed...");
        }
    }
}
