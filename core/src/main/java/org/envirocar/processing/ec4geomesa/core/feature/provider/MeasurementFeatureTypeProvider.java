package org.envirocar.processing.ec4geomesa.core.feature.provider;

import com.google.common.base.Joiner;
import com.google.inject.Provider;
import org.envirocar.processing.ec4geomesa.core.guice.annotations.InitializeTable;
import static org.envirocar.processing.ec4geomesa.core.feature.schema.MeasurementSchema.ATTRIB_TIME;
import static org.envirocar.processing.ec4geomesa.core.feature.schema.MeasurementSchema.SCHEMA;
import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.locationtech.geomesa.utils.interop.SimpleFeatureTypes;
import org.opengis.feature.simple.SimpleFeatureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.envirocar.processing.ec4geomesa.core.feature.schema.MeasurementSchema;

/**
 *
 * @author dewall
 */
public class MeasurementFeatureTypeProvider implements Provider<SimpleFeatureType> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MeasurementFeatureTypeProvider.class);

    @Override
    @InitializeTable
    public SimpleFeatureType get() {
        try {
            SimpleFeatureType measurementType;
            measurementType = DataUtilities.createType(MeasurementSchema.TABLE_NAME, Joiner.on(",").join(SCHEMA));
            measurementType.getUserData()
                    .put(SimpleFeatureTypes.DEFAULT_DATE_KEY, ATTRIB_TIME);
            return measurementType;
        } catch (SchemaException ex) {
            LOGGER.error("Error while creating SimpleFeatureType for type " + MeasurementSchema.TABLE_NAME, ex);
            throw new RuntimeException();
        }
    }

}
