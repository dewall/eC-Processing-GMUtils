package org.envirocar.processing.ec4geomesa.core.guice;

import com.google.common.base.Joiner;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import org.envirocar.processing.ec4geomesa.core.entity.Measurement;
import org.envirocar.processing.ec4geomesa.core.schema.MeasurementConstants;
import static org.envirocar.processing.ec4geomesa.core.schema.MeasurementConstants.ATTRIB_TIME;
import static org.envirocar.processing.ec4geomesa.core.schema.MeasurementConstants.SCHEMA;
import static org.envirocar.processing.ec4geomesa.core.schema.MeasurementConstants.TABLE_NAME;
import org.envirocar.processing.ec4geomesa.core.schema.TrackConstants;
import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.locationtech.geomesa.utils.interop.SimpleFeatureTypes;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 *
 * @author dewall
 */
public class FeatureTypesModule extends AbstractModule {

    @Override
    protected void configure() {

    }

    @Provides
    @Singleton
    @Named(TrackConstants.TABLE_NAME)
    public SimpleFeatureType provideTrackType() throws SchemaException {
        SimpleFeatureType trackType = DataUtilities.createType(
                TrackConstants.TABLE_NAME, Joiner.on(",").join(TrackConstants.SCHEMA));
        trackType.getUserData().put(SimpleFeatureTypes.DEFAULT_DATE_KEY,
                TrackConstants.ATTRIB_TRACK_STARTTIME);
        return trackType;
    }

    @Provides
    @Singleton
    @Named(MeasurementConstants.TABLE_NAME)
    public SimpleFeatureType provideMeasurementType() throws SchemaException {
        SimpleFeatureType measurementType = DataUtilities.createType(
                MeasurementConstants.TABLE_NAME, Joiner.on(",").join(SCHEMA));
        measurementType.getUserData()
                .put(SimpleFeatureTypes.DEFAULT_DATE_KEY, ATTRIB_TIME);
        return measurementType;
    }

}
