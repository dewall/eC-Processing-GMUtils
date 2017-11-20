package org.envirocar.processing.ec4geomesa.core.model;

import com.vividsolutions.jts.geom.Point;
import java.util.Date;

/**
 *
 * @author dewall
 */
public class Measurement {

    private String id;
    private Date time;
    private double speed;
    private double rpm;
    private Point point;

    /**
     * Constructor.
     *
     * @param id
     * @param point
     * @param time
     */
    public Measurement(String id, Point point, Date time) {
        this.id = id;
        this.point = point;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return time;
    }

    public long getTime() {
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

    public void setPoint(Point point) {
        this.point = point;
    }

    public boolean isValid() {
        return this.id != null
                && this.time != null
                && this.point != null;
    }

}
