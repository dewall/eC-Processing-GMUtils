package org.envirocar.processing.ec4geomesa.core.feature;

import com.beust.jcommander.internal.Lists;
import com.vividsolutions.jts.geom.LineString;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.envirocar.processing.ec4geomesa.core.model.RoadSegment;
import org.geotools.data.DataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.SchemaException;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

/**
 *
 * @author dewall
 */
public class RoadSegmentFeatureProfile extends AbstractFeatureProfile<RoadSegment> {

    private static final Logger LOGGER = Logger.getLogger(RoadSegmentFeatureProfile.class);
    private static final String TABLE_NAME = "roadsegments";

    private static final String ATTRIBUTE_OSMID = "OSMID";
    private static final String ATTRIBUTE_GEOM = "*geom";

    private final List<String> FEATURE_ATTRIBUTES = Lists.newArrayList(
            "OSMID:Integer",
            "*geom:LineString:srid=4326"
    );

    /**
     *
     * @param tableName
     */
    public RoadSegmentFeatureProfile(String tableName) {
        super(tableName);
        for (String phenomenon : MeasurementFeatureProfile.PHENOMENONS) {
            FEATURE_ATTRIBUTES.add("sum" + phenomenon + ":Double");
            FEATURE_ATTRIBUTES.add("avg" + phenomenon + ":Double");
            FEATURE_ATTRIBUTES.add("num" + phenomenon + ":Integer");
        }
    }

    @Override
    protected SimpleFeatureType createSimpleFeatureType() {
        try {
            SimpleFeatureType featureType = createSimpleFeatureType(FEATURE_ATTRIBUTES);
            return featureType;
        } catch (SchemaException ex) {
            LOGGER.error("Error while creating FeatureType for RoadSegment.", ex);
        }
        return null;
    }

    @Override
    public SimpleFeature createSimpleFeature(RoadSegment t) {
        SimpleFeature sf = featureBuilder.buildFeature(String.valueOf(t.getOsmId()));
        sf.setAttribute("OSMID", t.getOsmId());
        sf.setDefaultGeometry(t.getSegment());

        Map<String, Double> sumValues = t.getSummedValues();
        for (Map.Entry<String, Double> phenomenon : sumValues.entrySet()) {
            sf.setAttribute("sum" + phenomenon.getKey(), phenomenon.getValue());
        }

        Map<String, Double> avgValues = t.getAvgValues();
        for (Map.Entry<String, Double> phenomenon : avgValues.entrySet()) {
            sf.setAttribute("avg" + phenomenon.getKey(), phenomenon.getValue());
        }

        Map<String, Integer> numValues = t.getNumValues();
        for (Map.Entry<String, Integer> phenomenon : numValues.entrySet()) {
            sf.setAttribute("num" + phenomenon.getKey(), phenomenon.getValue());
        }

        return sf;
    }

    public RoadSegment createEntityFromFeature(SimpleFeature sf) {
        int osmid = (int) sf.getAttribute(ATTRIBUTE_OSMID);
        LineString lineString = (LineString) sf.getDefaultGeometry();
        RoadSegment result = new RoadSegment(osmid, lineString);

        for (String phenomenon : MeasurementFeatureProfile.PHENOMENONS) {
            double sumValue = (double) sf.getAttribute("sum" + phenomenon);
            double avgValue = (double) sf.getAttribute("avg" + phenomenon);
            int numValue = (int) sf.getAttribute("num" + phenomenon);
            result.addValue(phenomenon, sumValue, avgValue, numValue);
        }
        
        return result;
    }

    @Override
    public RoadSegment getById(DataStore ds, String osmid) {
        try {
            SimpleFeatureSource featureSource = ds.getFeatureSource(TABLE_NAME);
            Filter filter = CQL.toFilter("OSMID = " + osmid);
            SimpleFeatureCollection features = featureSource.getFeatures(filter);

            if (!features.isEmpty()) {
                SimpleFeature next = features.features().next();
                return createEntityFromFeature(next);
            }
        } catch (IOException ex) {
            LOGGER.error(String.format("Error while fetching FeatureSource for table [%s]", TABLE_NAME), ex);
        } catch (CQLException ex) {
            LOGGER.error("Error while creating OSM ID filter.", ex);
        }
        return null;
    }

}
