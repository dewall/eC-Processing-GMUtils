package org.envirocar.processing.ec4geomesa.core.feature.store;

import org.envirocar.processing.ec4geomesa.core.feature.schema.TrackSchema;
import org.envirocar.processing.ec4geomesa.core.model.Track;
import org.envirocar.processing.ec4geomesa.core.model.Tracks;
import org.opengis.feature.simple.SimpleFeature;

/**
 *
 * @author hafenkran
 */
public class TrackFeatureStore extends AbstractFeatureStore<Track> 
        implements TrackStore, TrackSchema {

    @Override
    public Track create(Track track) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Track save(Track track) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(Track track) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Track track) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Tracks get() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Track featureToEntity(SimpleFeature feature) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SimpleFeature entityToFeature(Track entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
