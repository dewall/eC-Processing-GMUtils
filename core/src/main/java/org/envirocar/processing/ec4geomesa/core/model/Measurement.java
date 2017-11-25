package org.envirocar.processing.ec4geomesa.core.model;

import com.vividsolutions.jts.geom.Point;
import java.util.Date;

/**
 *
 * @author dewall
 */
public class Measurement {

    private final String id;
    private final String trackId;
    private final Point point;

    private Date time;
    private double speed;
    private double rpm;

    /**
     * Constructor.
     *
     * @param id
     * @param trackId
     * @param point
     * @param time
     */
    public Measurement(String id, String trackId, Point point, Date time) {
        this.id = id;
        this.trackId = trackId;
        this.point = point;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public String getTrackId() {
        return trackId;
    }

    public Date getTime() {
        return time;
    }

    public long getTimeAsLong() {
        return time.getTime();
    }

    public void setTime(Date date) {
        this.time = date;
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

    public Point getPoint() {
        return point;
    }

    public boolean isValid() {
        return this.id != null
                && this.time != null
                && this.point != null;
    }

    @Override
    public String toString() {
        return "Measurement{" + "id=" + id 
                + ", trackId=" + trackId 
                + ", point=" + point 
                + ", time=" + time 
                + ", speed=" + speed 
                + ", rpm=" + rpm + '}';
    }

    
}
