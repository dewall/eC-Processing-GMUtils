package org.envirocar.processing.ec4geomesa.core.entity;

import com.vividsolutions.jts.geom.Point;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author dewall
 */
public interface Measurement {

    public String getId();

    public void setId(String id);

    public String getTrackId();

    public void setTrackId(String trackId);

    public Date getTime();

    public void setTime(Date time);

    public long getTimeAsLong();

    public Point getPoint();

    public void setPoint(Point point);

    public boolean isValid();

    public Map<PhenomenonType, Double> getPhenomenons();

    public void setPhenomenons(Map<PhenomenonType, Double> phenomenons);

}
