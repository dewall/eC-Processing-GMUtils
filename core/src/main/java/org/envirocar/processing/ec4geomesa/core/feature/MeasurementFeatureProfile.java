package org.envirocar.processing.ec4geomesa.core.feature;

import com.beust.jcommander.internal.Lists;
import com.vividsolutions.jts.geom.Point;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.envirocar.processing.ec4geomesa.core.model.Measurement;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.locationtech.geomesa.utils.interop.SimpleFeatureTypes;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 *
 * @author dewall
 */
public class MeasurementFeatureProfile extends AbstractFeatureProfile<Measurement> {

    private static final Logger LOG = Logger.getLogger(
            MeasurementFeatureProfile.class);

    private static final String TABLE_NAME = "measurements";
    private static final List<String> FEATURE_ATTRIBUTES = Lists.newArrayList(
            "MeasurementID:String",
            "TrackID:String",
            "Time:Date",
            "Speed:Integer",
            "*geom:Point:srid=4326"
    );

    /**
     * Constructor.
     */
    public MeasurementFeatureProfile() {
        super(TABLE_NAME);
    }

    @Override
    public SimpleFeatureType createSimpleFeatureType() {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.add("MEASUREMENT_ID", String.class);
        builder.add("TRACK_ID", String.class);
        builder.add("TIME", Date.class);
        builder.add("PHEN", Collection.class);
        builder.add("geom", Point.class, "4326");
        builder.setDefaultGeometry("geom");

        SimpleFeatureType featureType = builder.buildFeatureType();
        featureType.getUserData().put(
                SimpleFeatureTypes.DEFAULT_DATE_KEY, "TIME");
        return featureType;
//        try {
//            SimpleFeatureType featureType = createSimpleFeatureType(
//                    FEATURE_ATTRIBUTES);
//            featureType.getUserData().put(SimpleFeatureTypes.DEFAULT_DATE_KEY,
//                    FEATURE_ATTRIBUTES.get(2));
//            return featureType;
//        } catch (SchemaException ex) {
//            LOG.error("Error while creating TrackFeature", ex);
//        }
//        return null;
    }

    @Override
    public SimpleFeature createSimpleFeature(Measurement t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
