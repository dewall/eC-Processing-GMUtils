package org.envirocar.processing.ec4geomesa.core.feature.store;

import org.geotools.data.DataStore;
import org.opengis.feature.simple.SimpleFeature;

/**
 *
 * @author dewall
 */
public abstract class AbstractFeatureStore<T> {

    protected DataStore datastore;

    /**
     * Constructor.
     */
    public AbstractFeatureStore() {
    }

    public abstract T featureToEntity(SimpleFeature feature);

    public abstract SimpleFeature entityToFeature(T entity);
}
