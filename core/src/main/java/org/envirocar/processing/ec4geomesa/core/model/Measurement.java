package org.envirocar.processing.ec4geomesa.core.model;

import com.vividsolutions.jts.geom.Point;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dewall
 */
public class Measurement {

    private final String id;
    private final String trackId;
    private final Point point;
    private final Date time;

    private Map<String, Double> phenomenons;

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
        this.phenomenons = new HashMap<>();
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

    public Point getPoint() {
        return point;
    }

    public boolean isValid() {
        return this.id != null
                && this.time != null
                && this.point != null;
    }

    public Map<String, Double> getPhenomenons() {
        return phenomenons;
    }

    public void setPhenomenons(Map<String, Double> phenomenons) {
        this.phenomenons = phenomenons;
    }

    @Override
    public String toString() {
        return "Measurement{"
                + "id=" + id
                + ", trackId=" + trackId
                + ", point=" + point
                + ", time=" + time + '}';
    }

}
