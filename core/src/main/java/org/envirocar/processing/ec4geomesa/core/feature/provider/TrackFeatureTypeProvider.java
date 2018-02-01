package org.envirocar.processing.ec4geomesa.core.feature.provider;

import com.google.common.base.Joiner;
import com.google.inject.Provider;
import org.envirocar.processing.ec4geomesa.core.guice.annotations.InitializeTable;
import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.locationtech.geomesa.utils.interop.SimpleFeatureTypes;
import org.opengis.feature.simple.SimpleFeatureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.envirocar.processing.ec4geomesa.core.feature.schema.TrackSchema;
import org.envirocar.processing.ec4geomesa.core.feature.schema.MeasurementSchema;

/**
 *
 * @author dewall
 */
public class TrackFeatureTypeProvider implements Provider<SimpleFeatureType> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MeasurementFeatureTypeProvider.class);

    @Override
    @InitializeTable
    public SimpleFeatureType get() {
        try {
            SimpleFeatureType trackType = DataUtilities.createType(TrackSchema.TABLE_NAME, Joiner.on(",").join(TrackSchema.SCHEMA));
            trackType.getUserData().put(SimpleFeatureTypes.DEFAULT_DATE_KEY,
                    TrackSchema.ATTRIB_TRACK_STARTTIME);
            return trackType;
        } catch (SchemaException ex) {
            LOGGER.error("Error while creating SimpleFeatureType for type " + MeasurementSchema.TABLE_NAME, ex);
            throw new RuntimeException();
        }
    }

}
