package org.envirocar.processing.ec4geomesa.core.feature.wrapper;

import com.vividsolutions.jts.geom.LineString;
import org.envirocar.processing.ec4geomesa.core.entity.PhenomenonType;
import org.envirocar.processing.ec4geomesa.core.entity.RoadSegment;
import org.opengis.feature.simple.SimpleFeature;

/**
 *
 * @author dewall
 */
public class RoadSegmentWrapper extends AbstractFeatureWrapper implements RoadSegment {

    /**
     * Constructor.
     *
     * @param feature
     */
    public RoadSegmentWrapper(SimpleFeature feature) {
        super(feature);
    }

    @Override
    public String getOsmID() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setOsmID(String osmid) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LineString getGeometry() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setGeometry(LineString lineString) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Double getSumValue(PhenomenonType type) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setSumValue(PhenomenonType type, Double value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Double getNumValue(PhenomenonType type) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setNumValue(PhenomenonType type, Double value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Double getAvgValue(PhenomenonType type) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setAvgValue(PhenomenonType type, Double value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
