package org.envirocar.processing.ec4geomesa.core.feature.wrapper;

import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.LineString;
import java.util.Date;
import java.util.List;
import org.envirocar.processing.ec4geomesa.core.entity.CarSensor;
import org.envirocar.processing.ec4geomesa.core.entity.Track;
import org.envirocar.processing.ec4geomesa.core.entity.Measurement;
import org.opengis.feature.simple.SimpleFeature;
import org.envirocar.processing.ec4geomesa.core.feature.schema.TrackSchema;

/**
 *
 * @author dewall
 */
public class TrackWrapper extends AbstractFeatureWrapper implements Track {

    private CarSensorWrapper carSensor;
    private List<Measurement> measurements;

    /**
     * Constructor.
     *
     * @param feature
     */
    public TrackWrapper(SimpleFeature feature) {
        super(feature);
        this.measurements = Lists.newArrayList();
    }

    @Override
    public LineString getLineString() {
        return (LineString) this.feature.getDefaultGeometry();
    }

    @Override
    public void setLineString(LineString lineString) {
        if (lineString == null || lineString.getGeometryType() != LineString.class.getSimpleName()) {
            throw new IllegalArgumentException("Invalid geometry type.");
        }
        this.feature.setDefaultGeometry(lineString);
    }

    @Override
    public String getId() {
        return (String) this.feature.getAttribute(TrackSchema.ATTRIB_TRACK_ID);
    }

    @Override
    public double getLength() {
        return (double) this.feature.getAttribute(TrackSchema.ATTRIB_TRACK_LENGTH);
    }

    @Override
    public void setLength(double length) {
        this.feature.setAttribute(TrackSchema.ATTRIB_TRACK_LENGTH, length);
    }

    @Override
    public CarSensor getCarSensor() {
        return this.carSensor;
    }

    @Override
    public void setCarSensor(CarSensor carSensor) {
        this.carSensor = (CarSensorWrapper) carSensor;
    }

    @Override
    public List<Measurement> getMeasurements() {
        return this.measurements;
    }

    @Override
    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }

    @Override
    public void addMeasurements(Measurement measurement) {
        this.measurements.add(measurement);
    }

    @Override
    public Date getStartingTime() {
        return (Date) this.feature.getAttribute(TrackSchema.ATTRIB_TRACK_STARTTIME);
    }

    @Override
    public void setStartingTime(Date startingTime) {
        this.feature.setAttribute(TrackSchema.ATTRIB_TRACK_STARTTIME, startingTime);
    }

    @Override
    public Date getEndingTime() {
        return (Date) this.feature.getAttribute(TrackSchema.ATTRIB_TRACK_ENDTIME);
    }

    @Override
    public void setEndingTime(Date endingTime) {
        this.feature.setAttribute(TrackSchema.ATTRIB_TRACK_ENDTIME, endingTime);
    }

    @Override
    public boolean isValid() {
        return getId() != null
                && getLineString() != null
                && getLineString().getNumPoints() > 1
                && getStartingTime() != null
                && getEndingTime() != null
                && getCarSensor() != null;
    }
}
