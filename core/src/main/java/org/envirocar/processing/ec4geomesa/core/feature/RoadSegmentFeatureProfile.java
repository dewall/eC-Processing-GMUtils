package org.envirocar.processing.ec4geomesa.core.feature;

import com.beust.jcommander.internal.Lists;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.envirocar.processing.ec4geomesa.core.model.Measurement;
import org.envirocar.processing.ec4geomesa.core.model.RoadSegment;
import org.geotools.feature.SchemaException;
import org.locationtech.geomesa.utils.interop.SimpleFeatureTypes;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 *
 * @author dewall
 */
public class RoadSegmentFeatureProfile extends AbstractFeatureProfile<RoadSegment> {

    private static final Logger LOGGER = Logger.getLogger(RoadSegmentFeatureProfile.class);
    private static final String TABLE_NAME = "roadsegments";

    private static final List<String> FEATURE_ATTRIBUTES = Lists.newArrayList(
            "OSMID:Integer",
            "*geom:LineString:srid=4326"
    );

    /**
     *
     * @param tableName
     */
    public RoadSegmentFeatureProfile(String tableName) {
        super(tableName);
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

        // setting phenomenons
//        Map<String, Double> phenomenons = t.getSummedValues();
//        for (Map.Entry<String, Double> phenomenon : phenomenons.entrySet()) {
//            sf.setAttribute(phenomenon.getKey(), phenomenon.getValue());
//        }
//        return sf;
    }

}
