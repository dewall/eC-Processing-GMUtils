package org.envirocar.processing.ec4geomesa.core.model;

import com.vividsolutions.jts.geom.LineString;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author dewall
 */
public class Track {

    private final String id;
    private double length;

    private LineString lineString;
    private CarSensor carSensor;
    private List<Measurement> measurements;

    private Date startingTime;
    private Date endingTime;

    /**
     * Constructor.
     *
     * @param id
     */
    public Track(String id) {
        this.id = id;
        this.measurements = new ArrayList<>();
    }

    public LineString getLineString() {
        return lineString;
    }

    public void setLineString(LineString lineString) {
        this.lineString = lineString;
    }

    public String getId() {
        return id;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public CarSensor getCarSensor() {
        return carSensor;
    }

    public void setCarSensor(CarSensor carSensor) {
        this.carSensor = carSensor;
    }

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }

    public void addMeasurements(Measurement measurement) {
        this.measurements.add(measurement);
    }

    public Date getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(Date startingTime) {
        this.startingTime = startingTime;
    }

    public Date getEndingTime() {
        return endingTime;
    }

    public void setEndingTime(Date endingTime) {
        this.endingTime = endingTime;
    }

    public boolean isValid() {
        return this.id != null
                && this.lineString.getNumPoints() > 1
                && this.endingTime != null
                && this.startingTime != null
                && this.carSensor != null
                && this.lineString != null;
    }
}
