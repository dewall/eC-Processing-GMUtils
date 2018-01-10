package org.envirocar.processing.ec4geomesa.core.feature.store;

import org.envirocar.processing.ec4geomesa.core.model.Track;
import org.envirocar.processing.ec4geomesa.core.model.Tracks;

/**
 *
 * @author dewall
 */
public interface TrackStore {

    Track create(Track track);

    Track save(Track track);

    void update(Track track);

    void delete(Track track);
    
    Tracks get();
}
