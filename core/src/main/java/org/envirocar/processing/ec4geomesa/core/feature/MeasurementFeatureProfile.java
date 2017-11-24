package org.envirocar.processing.ec4geomesa.core.feature;

import com.beust.jcommander.internal.Lists;
import com.google.common.base.Joiner;
import com.vividsolutions.jts.geom.Point;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.envirocar.processing.ec4geomesa.core.model.Measurement;
import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
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
        try {
            String spec = Joiner.on(",").join(FEATURE_ATTRIBUTES);
            this.featureType = DataUtilities.createType(TABLE_NAME, spec);
            this.featureType.getUserData().put(
                    SimpleFeatureTypes.DEFAULT_DATE_KEY, "TIME");
        } catch (SchemaException ex) {
            LOG.error(ex);
        }

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
