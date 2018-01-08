package org.envirocar.processing.ec4geomesa.core.entity.wrapper.factory;

import com.google.inject.Inject;
import org.envirocar.processing.ec4geomesa.core.model.Measurement;

/**
 *
 * @author dewall
 */
public class MeasurementFeatureFactory implements FeatureFactory<Measurement>{

    @Inject
    public MeasurementFeatureFactory(){
        
    }
    
    @Override
    public Measurement create() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
