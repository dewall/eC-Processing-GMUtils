package org.envirocar.processing.ec4geomesa.ingestor;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.vividsolutions.jts.geom.GeometryFactory;
import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;
import org.envirocar.processing.ec4geomesa.core.guice.GeoMesaDataStoreModule;
import org.envirocar.processing.ec4geomesa.core.decoding.EnvirocarJSONUtils;
import org.envirocar.processing.ec4geomesa.core.feature.MeasurementFeatureStore;
import org.envirocar.processing.ec4geomesa.core.feature.TrackFeatureStore;
import org.envirocar.processing.ec4geomesa.core.model.Track;
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
    private TrackFeatureStore trackProfile;
    @Inject
    private MeasurementFeatureStore measurementProfile;

    @Override
    protected void setup(Context context) throws IOException,
            InterruptedException {
        super.setup(context);
        Guice.createInjector(new GeoMesaDataStoreModule())
                .injectMembers(this);
    }

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        try {
            Track track = EnvirocarJSONUtils.parseTrack(value.toString());
            SimpleFeature trackFeature = trackProfile.createFeatureFromEntity(track);
            if (track != null && track.isValid() && trackFeature != null) {
                context.write(new Text(), trackFeature);

                track.getMeasurements()
                        .stream()
                        .filter(m -> m.isValid())
                        .map(m -> measurementProfile.createFeatureFromEntity(m))
                        .forEach(m -> {
                            try {
                                context.write(new Text(), m);
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
