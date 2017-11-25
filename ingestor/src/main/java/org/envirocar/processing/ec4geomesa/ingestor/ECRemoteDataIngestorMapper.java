package org.envirocar.processing.ec4geomesa.ingestor;

import com.vividsolutions.jts.geom.GeometryFactory;
import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;
import org.envirocar.processing.ec4geomesa.core.DataStoreInstanceHandler;
import org.envirocar.processing.ec4geomesa.core.decoding.EnvirocarJSONUtils;
import org.envirocar.processing.ec4geomesa.core.feature.MeasurementFeatureProfile;
import org.envirocar.processing.ec4geomesa.core.feature.TrackFeatureProfile;
import org.envirocar.processing.ec4geomesa.core.model.Track;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.json.simple.parser.ParseException;
import org.opengis.feature.simple.SimpleFeature;

/**
 *
 * @author dewall
 */
public class ECRemoteDataIngestorMapper extends Mapper<LongWritable, Text, Text, SimpleFeature>{

   private static final Logger LOG = Logger.getLogger(ECRemoteDataIngestorMapper.class);

    private DataStoreInstanceHandler datastore;
    private GeometryFactory geometryFactory;
    private TrackFeatureProfile trackProfile;
    private MeasurementFeatureProfile measurementProfile;

    @Override
    protected void setup(Context context) throws IOException,
            InterruptedException {
        super.setup(context);
        this.geometryFactory = JTSFactoryFinder.getGeometryFactory();
        this.trackProfile = new TrackFeatureProfile();
        this.measurementProfile = new MeasurementFeatureProfile();
    }

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        try {
            Track track = EnvirocarJSONUtils.parseTrack(value.toString());
            SimpleFeature trackFeature = trackProfile.createSimpleFeature(track);
            if (track != null && track.isValid() && trackFeature != null) {
                context.write(new Text(), trackFeature);

                track.getMeasurements()
                        .stream()
                        .filter(m -> m.isValid())
                        .map(m -> measurementProfile.createSimpleFeature(m))
                        .forEach(m -> {
                            try {
                                context.write(new Text(), m);
                            } catch (IOException | InterruptedException e) {
                                LOG.error("Error while creating measurement", e);
                            }
                        });
            } else {
                LOG.error(track.toString());
                LOG.error(String.format("Track %s is not valid.", track.getId()));
            }
        } catch (ParseException ex) {
            LOG.error("Error while ingesting track: " + value.toString(), ex);
        }

    }

}
