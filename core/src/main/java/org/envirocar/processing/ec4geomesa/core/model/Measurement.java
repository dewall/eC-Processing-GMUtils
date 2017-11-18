package org.envirocar.processing.ec4geomesa.core.model;

import java.util.Date;

/**
 *
 * @author dewall
 */
public class Measurement {

    private String id;
    private Date date;

    private double latitude;
    private double longitude;

    private double speed;
    private double rpm;

    /**
     * Measurement.
     *
     * @param id
     * @param latitude
     * @param longitude
     */
    public Measurement(String id, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getRpm() {
        return rpm;
    }

    public void setRpm(double rpm) {
        this.rpm = rpm;
    }

    public boolean isValid() {
        return this.id != null
                && this.date != null
                && this.longitude != 0.0
                && this.latitude != 0.0;
    }

}
