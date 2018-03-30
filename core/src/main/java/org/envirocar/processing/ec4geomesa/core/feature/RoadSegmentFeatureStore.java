package org.envirocar.processing.ec4geomesa.core.feature;

import com.beust.jcommander.internal.Lists;
import com.google.inject.Inject;
import com.vividsolutions.jts.geom.LineString;
import java.io.IOException;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.envirocar.processing.ec4geomesa.core.model.RoadSegment;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.opengis.feature.simple.SimpleFeature;
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
        Transaction transaction = new DefaultTransaction();
        try {
            try {
                SimpleFeatureStore store = (SimpleFeatureStore) getFeatureSource();
                store.setTransaction(transaction);

                Filter filter = CQL.toFilter("OSMID = " + segment.getOsmId());

                List<String> keys = new ArrayList<>();
                List<Object> values = new ArrayList<>();

                segment.getAvgValues()
                        .entrySet()
                        .stream()
                        .forEach(c -> {
                            keys.add(avgKey(c.getKey()));
                            values.add(c.getValue());
                        });

                segment.getSummedValues()
                        .entrySet()
                        .stream()
                        .forEach(c -> {
                            keys.add(sumKey(c.getKey()));
                            values.add(c.getValue());
                        });

                segment.getNumValues()
                        .entrySet()
                        .stream()
                        .forEach(c -> {
                            keys.add(numKey(c.getKey()));
                            values.add(c.getValue());
                        });

                store.modifyFeatures(keys.toArray(new String[keys.size()]), values.toArray(), filter);
                transaction.commit();
            } catch (IOException e) {
                LOGGER.error(String.format("Error while updating OSM segment %s", segment.getOsmId()), e);
                transaction.rollback();
            } catch (CQLException ex) {
                LOGGER.error(ex);
            } finally {
                transaction.close();
            }
        } catch (IOException e) {
            LOGGER.error(String.format("Error while rolling back or closing transaction while updating OSM segment %s",
                    segment.getOsmId()), e);
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
