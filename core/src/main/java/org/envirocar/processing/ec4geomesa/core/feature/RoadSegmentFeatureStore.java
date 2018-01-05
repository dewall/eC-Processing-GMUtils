package org.envirocar.processing.ec4geomesa.core.feature;

import com.beust.jcommander.internal.Lists;
import com.google.inject.Inject;
import com.vividsolutions.jts.geom.LineString;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.envirocar.processing.ec4geomesa.core.model.RoadSegment;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.data.simple.SimpleFeatureWriter;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

/**
 *
 * @author dewall
 */
public class RoadSegmentFeatureStore extends AbstractFeatureStore<RoadSegment> {

    private static final Logger LOGGER = Logger.getLogger(RoadSegmentFeatureStore.class);

    private static final List<String> FEATURE_ATTRIBUTES = Lists.newArrayList(
            "OSMID:Integer",
            "*geom:LineString:srid=4326"
    );
    private static final Map<String, String> phenomenonToAttribute = new HashMap<>();
    private static final Map<String, String> attributeToPhenomenon = new HashMap<>();

    static {
        MeasurementFeatureStore.PHENOMENONS.stream().
                forEach((phenomenon) -> {
                    String attribute = phenomenon.replace(" ", "");
                    phenomenonToAttribute.put(phenomenon, attribute);
                    attributeToPhenomenon.put(attribute, phenomenon);

                    FEATURE_ATTRIBUTES.add(sumKey(attribute) + ":Double");
                    FEATURE_ATTRIBUTES.add(avgKey(attribute) + ":Double");
                    FEATURE_ATTRIBUTES.add(numKey(attribute) + ":Integer");
                });
    }

    private static final String TABLE_NAME = "roadsegments";
    private static final String ATTRIBUTE_OSMID = "OSMID";

    /**
     * Constructor.
     *
     * @param datastore
     */
    @Inject
    public RoadSegmentFeatureStore(DataStore datastore) {
        super(datastore, TABLE_NAME, ATTRIBUTE_OSMID, FEATURE_ATTRIBUTES);
    }

    public void store(RoadSegment segment) {
        try {
            SimpleFeature roadFeature = createFeatureFromEntity(segment);
            SimpleFeatureStore store = (SimpleFeatureStore) getFeatureSource();
            store.addFeatures(DataUtilities.collection(roadFeature));
        } catch (IOException ex) {
            LOGGER.error(String.format("Error while storing segment with id=%s",
                    "" + segment.getOsmId()), ex);
        }
    }

    public void update(RoadSegment segment) {
        try {
            Filter filter = CQL.toFilter("OSMID = " + segment.getOsmId());
            try (SimpleFeatureWriter featureWriter = (SimpleFeatureWriter) datastore
                    .getFeatureWriter(TABLE_NAME, filter, Transaction.AUTO_COMMIT)) {
                while (featureWriter.hasNext()) {
                    SimpleFeature next = featureWriter.next();
                    segment.getAvgValues()
                            .entrySet()
                            .stream()
                            .forEach(c -> {
                                next.setAttribute(avgKey(c.getKey()), c.getValue());
                                next.setAttribute(sumKey(c.getKey()), segment.getSumValue(c.getKey()));
                                next.setAttribute(numKey(c.getKey()), segment.getNumValue(c.getKey()));
                            });
                    featureWriter.write();
                }
            }
        } catch (IOException e) {
            LOGGER.error(String.format("Error while updating OSM segment %s", segment.getOsmId()), e);
        } catch (CQLException ex) {
            LOGGER.error(ex);
        }
    }

    @Override
    protected SimpleFeature createFeatureFromEntity(RoadSegment t) {
        SimpleFeature sf = featureBuilder.buildFeature(String.valueOf(t.getOsmId()));
        sf.setAttribute(ATTRIBUTE_OSMID, t.getOsmId());
        sf.setDefaultGeometry(t.getSegment());

        Map<String, Double> sumValues = t.getSummedValues();
        for (Map.Entry<String, Double> phenomenon : sumValues.entrySet()) {
            sf.setAttribute(sumKey(phenomenon.getKey()), phenomenon.getValue());
        }

        Map<String, Double> avgValues = t.getAvgValues();
        for (Map.Entry<String, Double> phenomenon : avgValues.entrySet()) {
            sf.setAttribute(avgKey(phenomenon.getKey()), phenomenon.getValue());
        }

        Map<String, Integer> numValues = t.getNumValues();
        for (Map.Entry<String, Integer> phenomenon : numValues.entrySet()) {
            sf.setAttribute(numKey(phenomenon.getKey()), phenomenon.getValue());
        }

        return sf;
    }

    @Override
    public RoadSegment createEntityFromFeature(SimpleFeature sf) {
        int osmid = (int) sf.getAttribute(ATTRIBUTE_OSMID);
        LineString lineString = (LineString) sf.getDefaultGeometry();
        RoadSegment result = new RoadSegment(osmid, lineString);

        for (String phenomenon : MeasurementFeatureStore.PHENOMENONS) {
            if (sf.getAttribute(sumKey(phenomenon)) != null) {
                double sumValue = (double) sf.getAttribute(sumKey(phenomenon));
                double avgValue = (double) sf.getAttribute(avgKey(phenomenon));
                int numValue = (int) sf.getAttribute(numKey(phenomenon));
                result.setValue(phenomenon, sumValue, avgValue, numValue);
            }
        }

        return result;
    }

    private static final String avgKey(String phenomenon) {
        return formatKey("avg", phenomenon);
    }

    private static final String sumKey(String phenomenon) {
        return formatKey("sum", phenomenon);
    }

    private static final String numKey(String phenomenon) {
        return formatKey("num", phenomenon);
    }

    private static final String formatKey(String type, String phenomenon) {
        return type + phenomenon.replaceAll("\\s", "");
    }
}
