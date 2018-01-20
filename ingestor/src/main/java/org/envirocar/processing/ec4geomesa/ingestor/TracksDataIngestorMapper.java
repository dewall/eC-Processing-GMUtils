package org.envirocar.processing.ec4geomesa.ingestor;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.vividsolutions.jts.geom.GeometryFactory;
import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;
import org.envirocar.processing.ec4geomesa.core.decoding.TrackJsonDecoder;
import org.envirocar.processing.ec4geomesa.core.entity.Track;
import org.envirocar.processing.ec4geomesa.core.entity.wrapper.AbstractFeatureWrapper;
import org.envirocar.processing.ec4geomesa.core.guice.DataStoreModule;
import org.json.simple.parser.ParseException;
import org.opengis.feature.simple.SimpleFeature;

/**
 *
 * @author dewall
 */
public class TracksDataIngestorMapper extends Mapper<LongWritable, Text, Text, SimpleFeature> {

    private static final Logger LOG = Logger.getLogger(TracksDataIngestorMapper.class);

    @Inject
    private GeometryFactory geometryFactory;
    @Inject
    private TrackJsonDecoder trackDecoder;

    @Override
    protected void setup(Context context) throws IOException,
            InterruptedException {
        super.setup(context);
        Guice.createInjector(new DataStoreModule())
                .injectMembers(this);
    }

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        try {
            Track track = trackDecoder.parseJson(value.toString());
            if (track != null && track.isValid()) {
                context.write(new Text(), ((AbstractFeatureWrapper) track).getFeature());

                track.getMeasurements() 
                        .stream()
                        .filter(m -> m.isValid())
                        .forEach(m -> {
                            try {
                                context.write(new Text(), ((AbstractFeatureWrapper) m).getFeature());
                            } catch (IOException | InterruptedException e) {
                                LOG.error("Error while creating measurement", e);
                            }
                        });
            } else {
                LOG.error(String.format("Track %s is not valid.", track.getId()));
            }
        } catch (ParseException ex) {
            LOG.error("Error while ingesting track: " + value.toString(), ex);
        } catch (Exception ex) {
            LOG.error("Error while ingesting track: " + value.toString(), ex);
        }

    }

}
