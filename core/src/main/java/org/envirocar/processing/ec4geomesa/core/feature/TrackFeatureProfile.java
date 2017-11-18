package org.envirocar.processing.ec4geomesa.core.feature;

import com.google.common.collect.Lists;
import java.util.List;
import org.apache.log4j.Logger;
import org.envirocar.processing.ec4geomesa.core.model.Track;
import org.geotools.feature.SchemaException;
import org.locationtech.geomesa.utils.interop.SimpleFeatureTypes;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 *
 * @author dewall
 */
public class TrackFeatureProfile extends AbstractFeatureProfile<Track> {

    private static final Logger LOG = Logger.
            getLogger(TrackFeatureProfile.class);

    private static final String TABLE_NAME = "tracks";
    private static final List<String> featureAttributes = Lists.newArrayList(
            "TrackID:String",
            "StartTime:Date",
            "EndTime:Date",
            "Length:Float",
            "CarManufacturer:String",
            "CarModel:String",
            "CarFuelType:String",
            "CarConstructionYear:Integer",
            "CarEngineDisplacement:Integer",
            "*geom:LineString:srid=4326"
    );

    public TrackFeatureProfile() {
        super(TABLE_NAME);
    }

    @Override
    public SimpleFeatureType getSimpleFeatureType() {
        try {
            SimpleFeatureType featureType = createSimpleFeatureType(
                    featureAttributes);
            featureType.getUserData().put(SimpleFeatureTypes.DEFAULT_DATE_KEY,
                    featureAttributes.get(1));
            return featureType;
        } catch (SchemaException ex) {
            LOG.error("Error while creating TrackFeature", ex);
        }
        return null;
    }

    @Override
    public SimpleFeature createSimpleFeature(Track t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
