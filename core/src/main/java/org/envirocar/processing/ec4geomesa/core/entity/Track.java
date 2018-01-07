package org.envirocar.processing.ec4geomesa.core.entity;

import com.vividsolutions.jts.geom.LineString;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.envirocar.processing.ec4geomesa.core.model.Measurement;

/**
 *
 * @author dewall
 */
public interface Track {

    public LineString getLineString();

    public void setLineString(LineString lineString);

    public String getId();

    public double getLength();

    public void setLength(double length);

    public CarSensor getCarSensor();

    public void setCarSensor(CarSensor carSensor);

    public List<Measurement> getMeasurements();

    public void setMeasurements(List<Measurement> measurements);

    public void addMeasurements(Measurement measurement);

    public Date getStartingTime();

    public void setStartingTime(Date startingTime);

    public Date getEndingTime();

    public void setEndingTime(Date endingTime);

    public boolean isValid();

}
