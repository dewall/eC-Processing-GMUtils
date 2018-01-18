package org.envirocar.processing.ec4geomesa.core.entity.wrapper;

import com.beust.jcommander.internal.Maps;
import com.vividsolutions.jts.geom.Point;
import java.util.Date;
import java.util.Map;
import org.envirocar.processing.ec4geomesa.core.entity.Measurement;
import org.envirocar.processing.ec4geomesa.core.entity.PhenomenonType;
import org.opengis.feature.simple.SimpleFeature;
import org.envirocar.processing.ec4geomesa.core.schema.MeasurementConstants;

/**
 *
 * @author dewall
 */
public class MeasurementWrapper extends AbstractFeatureWrapper implements Measurement {

    /**
     * Constructor.
     *
     * @param feature
     */
    public MeasurementWrapper(SimpleFeature feature) {
        super(feature);
    }

    @Override
    public String getId() {
        return (String) this.feature.getAttribute(MeasurementConstants.ATTRIB_MEASUREMENT_ID);
    }

    @Override
    public void setId(String id) {
        this.feature.setAttribute(MeasurementConstants.ATTRIB_MEASUREMENT_ID, id);
    }

    @Override
    public String getTrackId() {
        return (String) this.feature.getAttribute(MeasurementConstants.ATTRIB_TRACK_ID);
    }

    @Override
    public void setTrackId(String trackId) {
        this.feature.setAttribute(MeasurementConstants.ATTRIB_TRACK_ID, trackId);
    }

    @Override
    public Date getTime() {
        return (Date) this.feature.getAttribute(MeasurementConstants.ATTRIB_TIME);
    }

    @Override
    public void setTime(Date time) {
        this.feature.setAttribute(MeasurementConstants.ATTRIB_TIME, time);
    }

    @Override
    public long getTimeAsLong() {
        Date time = this.getTime();
        return time == null ? null : time.getTime();
    }

    @Override
    public Point getPoint() {
        return (Point) this.feature.getDefaultGeometry();
    }

    @Override
    public void setPoint(Point point) {
        this.feature.setDefaultGeometry(point);
    }

    @Override
    public Map<PhenomenonType, Double> getPhenomenons() {
        Map<PhenomenonType, Double> result = Maps.newHashMap();
        for (PhenomenonType t : PhenomenonType.values()) {
            Object p = this.feature.getAttribute(t.name());
            if (p != null) {
                result.put(t, (double) p);
            }
        }
        return result;
    }

    @Override
    public void setPhenomenons(Map<PhenomenonType, Double> phenomenons) {
        phenomenons.entrySet().
                forEach((entry) -> {
                    feature.setAttribute(entry.getKey().name(), entry.getValue());
                });
    }

    @Override
    public boolean isValid() {
        return getId() != null
                && getTime() != null
                && getPoint() != null;
    }
}
