package org.envirocar.processing.ec4geomesa.core.feature.store;

import org.envirocar.processing.ec4geomesa.core.model.Measurement;

/**
 *
 * @author dewall
 */
public interface MeasurementStore {
    Measurement create(Measurement track);

    Measurement save(Measurement track);

    void update(Measurement track);

    void delete(Measurement track);
    
    Measurement getByID(String id);
}
