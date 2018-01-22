package org.envirocar.processing.ec4geomesa.core.feature2;

//package org.envirocar.processing.ec4geomesa.core.feature;
//
//import org.envirocar.processing.ec4geomesa.core.schema.MeasurementSchema;
//import com.google.inject.Inject;
//import com.vividsolutions.jts.geom.Point;
//import java.io.IOException;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import org.apache.log4j.Logger;
//import static org.envirocar.processing.ec4geomesa.core.schema.MeasurementSchema.ATTRIB_TIME;
//import static org.envirocar.processing.ec4geomesa.core.schema.MeasurementSchema.TABLE_NAME;
//import org.envirocar.processing.ec4geomesa.core.model.Measurement;
//import org.envirocar.processing.ec4geomesa.core.model.Track;
//import org.geotools.data.DataStore;
//import org.geotools.data.simple.SimpleFeatureCollection;
//import org.geotools.filter.text.cql2.CQL;
//import org.geotools.filter.text.cql2.CQLException;
//import org.opengis.feature.simple.SimpleFeature;
//
///**
// *
// * @author dewall
// */
//public class MeasurementFeatureStore extends AbstractFeatureStore<Measurement> implements MeasurementSchema{
//
//    private static final Logger LOG = Logger.getLogger(MeasurementFeatureStore.class);
//    /**
//     * Constructor.
//     *
//     * @param datastore
//     */
//    @Inject
//    public MeasurementFeatureStore(DataStore datastore) {
//        super(datastore, TABLE_NAME, ATTRIB_MEASUREMENT_ID, ATTRIB_TIME, SCHEMA);
//    }
//
//    @Override
//    public SimpleFeature createFeatureFromEntity(Measurement t) {
//        if (!t.isValid()) {
//            return null;
//        }
//
//        SimpleFeature sf = featureBuilder.buildFeature(t.getId());
//        sf.setAttribute(ATTRIB_MEASUREMENT_ID, t.getId());
//        sf.setAttribute(ATTRIB_TRACK_ID, t.getTrackId());
//        sf.setAttribute(ATTRIB_TIME, t.getTime());
//        sf.setDefaultGeometry(t.getPoint());
//
//        // setting phenomenons
//        Map<String, Double> phenomenons = t.getPhenomenons();
//        phenomenons.entrySet().
//                forEach((phenomenon) -> {
//                    sf.setAttribute(phenomenon.getKey(), phenomenon.getValue());
//                });
//        return sf;
//    }
//
//    @Override
//    protected Measurement createEntityFromFeature(SimpleFeature sf) {
//        if (sf == null) {
//            return null;
//        }
//
//        String mID = (String) sf.getAttribute(ATTRIB_MID);
//        String tID = (String) sf.getAttribute(ATTRIB_TRACKID);
//        Date time = (Date) sf.getAttribute(ATTRIB_TIME);
//        Point point = (Point) sf.getDefaultGeometry();
//        Measurement m = new Measurement(mID, tID, point, time);
//
//        Map<String, Double> phenomenons = new HashMap<>();
//        PHENOMENONS.forEach(p -> {
//            Object attr = sf.getAttribute(key(p));
//            if (attr != null) {
//                double attribute = (double) attr;
//                phenomenons.put(p, attribute);
//            }
//
//        });
//        m.setPhenomenons(phenomenons);
//
//        return m;
//    }
//
//    public Track fetchTrack(Track track) {
//        try {
//            SimpleFeatureCollection sfc = fetch(CQL.toFilter("TrackID = '" + track.getId() + "'"));
//            List<Measurement> measurements = createEntitiesFromFeatures(sfc);
//            track.setMeasurements(measurements);
//        } catch (CQLException ex) {
//            LOG.error("Error creating Filter", ex);
//        } catch (IOException ex) {
//            LOG.error(String.format("Error while getting measurements for track=%s", track.getId()), ex);
//        }
//        return track;
//    }
//    
//    private static String key(String attribute){
//        return attribute.replaceAll("\\s", "");
//    }
//
//}
