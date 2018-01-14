package org.envirocar.processing.ec4geomesa.core.entity.wrapper.factory;

import com.google.common.base.Joiner;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.envirocar.processing.ec4geomesa.core.entity.Track;
import org.envirocar.processing.ec4geomesa.core.entity.wrapper.TrackWrapper;
import org.envirocar.processing.ec4geomesa.core.schema.TrackSchema;
import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.locationtech.geomesa.utils.interop.SimpleFeatureTypes;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 *
 * @author dewall
 */
public class TrackFeatureFactory implements Provider<Track>, TrackSchema {

    private final SimpleFeatureType trackType;
    private final SimpleFeatureBuilder featureBuilder;

    /**
     * Constructor.
     *
     * @throws SchemaException
     */
    @Inject
    public TrackFeatureFactory() throws SchemaException {
        this.trackType = DataUtilities.createType(TABLE_NAME,
                Joiner.on(",").join(SCHEMA));
        this.trackType.getUserData()
                .put(SimpleFeatureTypes.DEFAULT_DATE_KEY, ATTRIB_TRACK_STARTTIME);

        this.featureBuilder = new SimpleFeatureBuilder(trackType);
    }

    @Override
    public Track get() {
        return new TrackWrapper(featureBuilder.buildFeature(null));
    }
}
