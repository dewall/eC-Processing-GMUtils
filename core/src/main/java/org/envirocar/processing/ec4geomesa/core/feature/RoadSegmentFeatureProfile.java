package org.envirocar.processing.ec4geomesa.core.feature;

import com.beust.jcommander.internal.Lists;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.envirocar.processing.ec4geomesa.core.model.RoadSegment;
import org.geotools.feature.SchemaException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 *
 * @author dewall
 */
public class RoadSegmentFeatureProfile extends AbstractFeatureProfile<RoadSegment> {

    private static final Logger LOGGER = Logger.getLogger(RoadSegmentFeatureProfile.class);
    private static final String TABLE_NAME = "roadsegments";

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
            FEATURE_ATTRIBUTES.add("sum" + phenomenon);
            FEATURE_ATTRIBUTES.add("avg" + phenomenon);
            FEATURE_ATTRIBUTES.add("num" + phenomenon);
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

}
