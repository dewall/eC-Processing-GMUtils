package org.envirocar.processing.ec4geomesa.core.entity.wrapper;

import org.opengis.feature.simple.SimpleFeature;

/**
 *
 * @author dewall
 */
public abstract class AbstractFeatureWrapper {

    protected SimpleFeature feature;

    /**
     * Constructor.
     *
     * @param feature
     */
    public AbstractFeatureWrapper(SimpleFeature feature) {
        this.feature = feature;
    }

    public String getFeatureID() {
        return feature.getID();
    }

    public SimpleFeature getFeature() {
        return feature;
    }

    protected void setFeature(SimpleFeature feature) {
        this.feature = feature;
    }
}
