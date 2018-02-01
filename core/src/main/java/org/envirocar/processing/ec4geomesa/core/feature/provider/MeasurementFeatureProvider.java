package org.envirocar.processing.ec4geomesa.core.feature.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.envirocar.processing.ec4geomesa.core.entity.Measurement;
import org.envirocar.processing.ec4geomesa.core.feature.wrapper.MeasurementWrapper;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeatureType;
import org.envirocar.processing.ec4geomesa.core.guice.annotations.MeasurementType;
import org.envirocar.processing.ec4geomesa.core.feature.schema.MeasurementSchema;

/**
 *
 * @author dewall
 */
public class MeasurementFeatureProvider implements Provider<Measurement>, MeasurementSchema {

    private final SimpleFeatureBuilder featureBuilder;

    /**
     * Constructor.
     *
     * @param measurementType
     * @throws SchemaException
     */
    @Inject
    public MeasurementFeatureProvider(@MeasurementType SimpleFeatureType measurementType) throws SchemaException {
        this.featureBuilder = new SimpleFeatureBuilder(measurementType);
    }

    @Override
    public Measurement get() {
        return new MeasurementWrapper(featureBuilder.buildFeature(null));
    }

}
