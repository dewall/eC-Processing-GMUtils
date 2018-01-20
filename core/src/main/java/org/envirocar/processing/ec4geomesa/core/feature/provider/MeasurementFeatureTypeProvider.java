package org.envirocar.processing.ec4geomesa.core.feature.provider;

import com.google.common.base.Joiner;
import com.google.inject.Provider;
import org.envirocar.processing.ec4geomesa.core.guice.annotations.InitializeTable;
import org.envirocar.processing.ec4geomesa.core.feature.schema.MeasurementConstants;
import static org.envirocar.processing.ec4geomesa.core.feature.schema.MeasurementConstants.ATTRIB_TIME;
import static org.envirocar.processing.ec4geomesa.core.feature.schema.MeasurementConstants.SCHEMA;
import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.locationtech.geomesa.utils.interop.SimpleFeatureTypes;
import org.opengis.feature.simple.SimpleFeatureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            measurementType = DataUtilities.createType(
                    MeasurementConstants.TABLE_NAME, Joiner.on(",").join(SCHEMA));
            measurementType.getUserData()
                    .put(SimpleFeatureTypes.DEFAULT_DATE_KEY, ATTRIB_TIME);
            return measurementType;
        } catch (SchemaException ex) {
            LOGGER.error("Error while creating SimpleFeatureType for type " + MeasurementConstants.TABLE_NAME, ex);
            throw new RuntimeException();
        }
    }

}
