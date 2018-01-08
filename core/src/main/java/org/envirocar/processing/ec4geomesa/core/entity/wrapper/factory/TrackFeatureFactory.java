package org.envirocar.processing.ec4geomesa.core.entity.wrapper.factory;

import com.google.inject.Inject;
import org.envirocar.processing.ec4geomesa.core.entity.Track;

/**
 *
 * @author dewall
 */
public class TrackFeatureFactory implements FeatureFactory<Track> {

    @Inject
    public TrackFeatureFactory() {

    }

    @Override
    public Track create() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
