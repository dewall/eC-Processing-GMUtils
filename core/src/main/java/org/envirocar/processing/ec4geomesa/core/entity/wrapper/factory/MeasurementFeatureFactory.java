package org.envirocar.processing.ec4geomesa.core.entity.wrapper.factory;

import com.google.common.base.Joiner;
import com.google.inject.Inject;
import org.envirocar.processing.ec4geomesa.core.entity.Measurement;
import org.envirocar.processing.ec4geomesa.core.entity.wrapper.MeasurementWrapper;
import org.envirocar.processing.ec4geomesa.core.schema.MeasurementSchema;
import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.locationtech.geomesa.utils.interop.SimpleFeatureTypes;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 *
 * @author dewall
 */
public class MeasurementFeatureFactory implements FeatureFactory<Measurement>, MeasurementSchema {

    private final SimpleFeatureType measurementType;
    private final SimpleFeatureBuilder featureBuilder;

    /**
     * Constructor.
     *
     * @throws SchemaException
     */
    @Inject
    public MeasurementFeatureFactory() throws SchemaException {
        this.measurementType = DataUtilities.createType(TABLE_NAME,
                Joiner.on(",").join(SCHEMA));
        this.measurementType.getUserData()
                .put(SimpleFeatureTypes.DEFAULT_DATE_KEY, ATTRIB_TIME);

        this.featureBuilder = new SimpleFeatureBuilder(measurementType);
    }

    @Override
    public Measurement create(String id) {
        return new MeasurementWrapper(featureBuilder.buildFeature(id));
    }

}
