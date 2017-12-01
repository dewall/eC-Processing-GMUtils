package org.envirocar.processing.ec4geomesa.core.feature;

import com.beust.jcommander.internal.Lists;
import com.vividsolutions.jts.geom.Point;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.log4j.Logger;
import org.envirocar.processing.ec4geomesa.core.model.Measurement;
import org.opengis.feature.simple.SimpleFeature;

/**
 *
 * @author dewall
 */
public class MeasurementFeatureStore extends AbstractFeatureStore<Measurement> {

    private static final Logger LOG = Logger.getLogger(MeasurementFeatureStore.class);

    private static final String TABLE_NAME = "measurements";
    private static final String ATTRIB_MID = "MeasurementID";
    private static final String ATTRIB_TRACKID = "TrackID";
    private static final String ATTRIB_TIME = "Time";

    protected static final List<String> PHENOMENONS = Arrays.asList(
            "CO2",
            "Calculated MAF",
            "Consumption",
            "Engine Load",
            "Fuel System Loop",
            "Fuel System Status Code",
            "GPS Accuracy",
            "GPS Altitude",
            "GPS Bearing",
            "GPS HDOP",
            "GPS PDOP",
            "GPS Speed",
            "GPS VDOP",
            "Intake Pressure",
            "Intake Temperature",
            "Long-Term Fuel Trim 1",
            "MAF",
            "O2 Lambda Current",
            "O2 Lambda Current ER",
            "O2 Lambda Voltage",
            "O2 Lambda Voltage ER",
            "Rpm",
            "Short-Term Fuel Trim 1",
            "Speed",
            "Throttle Position");

    private static final List<String> FEATURE_ATTRIBUTES = Lists.newArrayList(
            "MeasurementID:String",
            "TrackID:String",
            "Time:Date",
            "*geom:Point:srid=4326",
            "CO2:Double",
            "Calculated MAF:Double",
            "Consumption:Double",
            "Engine Load:Double",
            "Fuel System Loop:Double",
            "Fuel System Status Code:Double",
            "GPS Accuracy:Double",
            "GPS Altitude:Double",
            "GPS Bearing:Double",
            "GPS HDOP:Double",
            "GPS PDOP:Double",
            "GPS Speed:Double",
            "GPS VDOP:Double",
            "Intake Pressure:Double",
            "Intake Temperature:Double",
            "Long-Term Fuel Trim 1:Double",
            "MAF:Double",
            "O2 Lambda Current:Double",
            "O2 Lambda Current ER:Double",
            "O2 Lambda Voltage:Double",
            "O2 Lambda Voltage ER:Double",
            "Rpm:Double",
            "Short-Term Fuel Trim 1:Double",
            "Speed:Double",
            "Throttle Position:Double"
    );

    /**
     * Constructor.
     */
    public MeasurementFeatureStore() {
        super(TABLE_NAME, ATTRIB_MID, ATTRIB_TIME, PHENOMENONS);
    }

    @Override
    public SimpleFeature createFeatureFromEntity(Measurement t) {
        if (!t.isValid()) {
            return null;
        }

        SimpleFeature sf = featureBuilder.buildFeature(t.getId());
        sf.setAttribute(ATTRIB_MID, t.getId());
        sf.setAttribute(ATTRIB_TRACKID, t.getTrackId());
        sf.setAttribute(ATTRIB_TIME, t.getTime());
        sf.setDefaultGeometry(t.getPoint());

        // setting phenomenons
        Map<String, Double> phenomenons = t.getPhenomenons();
        phenomenons.entrySet().
                forEach((phenomenon) -> {
                    sf.setAttribute(phenomenon.getKey(), phenomenon.getValue());
                });
        
        return sf;
    }

    @Override
    protected Measurement createEntityFromFeature(SimpleFeature sf) {
        if (sf == null) {
            return null;
        }

        String mID = (String) sf.getAttribute(ATTRIB_MID);
        String tID = (String) sf.getAttribute(ATTRIB_TRACKID);
        Date time = (Date) sf.getAttribute(ATTRIB_TIME);
        Point point = (Point) sf.getDefaultGeometry();
        Measurement m = new Measurement(mID, tID, point, time);

        return m;
    }

}
