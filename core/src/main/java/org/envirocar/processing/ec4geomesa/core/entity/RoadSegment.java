package org.envirocar.processing.ec4geomesa.core.entity;

import com.vividsolutions.jts.geom.LineString;

/**
 *
 * @author dewall
 */
public interface RoadSegment {

    public String getOsmID();

    public void setOsmID(String osmid);

    public LineString getGeometry();

    public void setGeometry(LineString lineString);

    public Double getSumValue(PhenomenonType type);

    public void setSumValue(PhenomenonType type, Double value);

    public Double getNumValue(PhenomenonType type);

    public void setNumValue(PhenomenonType type, Double value);

    public Double getAvgValue(PhenomenonType type);

    public void setAvgValue(PhenomenonType type, Double value);
}
