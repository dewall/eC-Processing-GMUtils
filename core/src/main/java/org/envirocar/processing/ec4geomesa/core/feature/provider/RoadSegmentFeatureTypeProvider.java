package org.envirocar.processing.ec4geomesa.core.feature.provider;

import com.google.common.base.Joiner;
import com.google.inject.Provider;
import org.envirocar.processing.ec4geomesa.core.feature.schema.RoadSegmentSchema;
import org.envirocar.processing.ec4geomesa.core.guice.annotations.InitializeTable;
import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.opengis.feature.simple.SimpleFeatureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.envirocar.processing.ec4geomesa.core.feature.schema.MeasurementSchema;

/**
 *
 * @author dewall
 */
public class RoadSegmentFeatureTypeProvider implements Provider<SimpleFeatureType> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoadSegmentFeatureTypeProvider.class);

    @Override
    @InitializeTable
    public SimpleFeatureType get() {
        try {
            SimpleFeatureType roadsegmentType = DataUtilities.createType(
                    RoadSegmentSchema.TABLE_NAME, Joiner.on(",").join(RoadSegmentSchema.SCHEMA));
            return roadsegmentType;
        } catch (SchemaException ex) {
            LOGGER.error("Error while creating SimpleFeatureType for type " + MeasurementSchema.TABLE_NAME, ex);
            throw new RuntimeException();
        }
    }
}
