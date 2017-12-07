package org.envirocar.processing.ec4geomesa.core.feature;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.vividsolutions.jts.geom.LineString;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.envirocar.processing.ec4geomesa.core.model.CarSensor;
import org.envirocar.processing.ec4geomesa.core.model.Track;
import org.geotools.data.DataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.opengis.feature.simple.SimpleFeature;

/**
 *
 * @author dewall
 */
public class TrackFeatureStore extends AbstractFeatureStore<Track> {

    private static final Logger LOGGER = Logger.
            getLogger(TrackFeatureStore.class);

    private static final String TABLE_NAME = "tracks";

    private static final String ATTRIB_TRACKID = "TrackID";
    private static final String ATTRIB_STARTTIME = "StartTime";
    private static final String ATTRIB_ENDTIME = "EndTime";
    private static final String ATTRIB_LENGTH = "Length";
    private static final String ATTRIB_MANUFACTURER = "CarManufacturer";
    private static final String ATTRIB_MODEL = "CarModel";
    private static final String ATTRIB_FUELTYPE = "CarFuelType";
    private static final String ATTRIB_CONSTRUCTIONYEAR = "CarConstructionYear";
    private static final String ATTRIB_ENGINEDISPLACEMENT = "CarEngineDisplacement";

    private static final List<String> FEATURE_ATTRIBUTES = Lists.newArrayList(
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

    private final MeasurementFeatureStore measurementStore;

    /**
     * Constructor.
     *
     * @param datastore
     * @param measurementStore
     */
    @Inject
    public TrackFeatureStore(DataStore datastore, MeasurementFeatureStore measurementStore) {
        super(datastore, TABLE_NAME, ATTRIB_TRACKID, ATTRIB_STARTTIME, FEATURE_ATTRIBUTES);
        this.measurementStore = measurementStore;
    }

    @Override
    public SimpleFeature createFeatureFromEntity(Track t) {
        if (!t.isValid()) {
            return null;
        }

        SimpleFeature sf = featureBuilder.buildFeature(t.getId());
        sf.setDefaultGeometry(t.getLineString());
        sf.setAttribute(ATTRIB_TRACKID, t.getId());
        sf.setAttribute(ATTRIB_STARTTIME, t.getStartingTime());
        sf.setAttribute(ATTRIB_ENDTIME, t.getEndingTime());
        sf.setAttribute(ATTRIB_LENGTH, t.getLength());

        CarSensor s = t.getCarSensor();
        sf.setAttribute(ATTRIB_MANUFACTURER, s.getManufacturer());
        sf.setAttribute(ATTRIB_MODEL, s.getModel());
        sf.setAttribute(ATTRIB_FUELTYPE, s.getFuelType());
        sf.setAttribute(ATTRIB_CONSTRUCTIONYEAR, s.getConstructionYear());
        sf.setAttribute(ATTRIB_ENGINEDISPLACEMENT, s.getEngineDisplacement());

        return sf;
    }

    @Override
    protected Track createEntityFromFeature(SimpleFeature sf) {
        String trackId = (String) sf.getAttribute(ATTRIB_TRACKID);
        Track track = new Track(trackId);
        track.setLength((float) sf.getAttribute(ATTRIB_LENGTH));
        track.setStartingTime((Date) sf.getAttribute(ATTRIB_STARTTIME));
        track.setEndingTime((Date) sf.getAttribute(ATTRIB_ENDTIME));
        track.setLineString((LineString) sf.getDefaultGeometry());

        // create car type
        CarSensor s = new CarSensor();
        s.setManufacturer((String) sf.getAttribute(ATTRIB_MANUFACTURER));
        s.setModel((String) sf.getAttribute(ATTRIB_MODEL));
        s.setFuelType((String) sf.getAttribute(ATTRIB_FUELTYPE));
        s.setConstructionYear((int) sf.getAttribute(ATTRIB_CONSTRUCTIONYEAR));
        s.setEngineDisplacement((int) sf.getAttribute(ATTRIB_ENGINEDISPLACEMENT));
        track.setCarSensor(s);

        return track;
    }

    public Track getByID(String tackId, boolean fetchMeasurements) {
        Track track = getByID(tackId);
        if (fetchMeasurements) {
            return measurementStore.fetchTrack(track);
        }
        return track;
    }

    public SimpleFeatureCollection getAllTracks() {
        try {
            return this.datastore.getFeatureSource(getTableName()).getFeatures();
        } catch (IOException ex) {
            LOGGER.error("Error while querying all Tracks", ex);
        }
        return null;
    }

}
