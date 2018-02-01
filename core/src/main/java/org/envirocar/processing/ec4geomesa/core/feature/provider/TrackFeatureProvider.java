package org.envirocar.processing.ec4geomesa.core.feature.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.envirocar.processing.ec4geomesa.core.entity.Track;
import org.envirocar.processing.ec4geomesa.core.feature.wrapper.TrackWrapper;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeatureType;
import org.envirocar.processing.ec4geomesa.core.guice.annotations.TrackType;
import org.envirocar.processing.ec4geomesa.core.feature.schema.TrackSchema;

/**
 *
 * @author dewall
 */
public class TrackFeatureProvider implements Provider<Track>, TrackSchema {

    private final SimpleFeatureBuilder featureBuilder;

    /**
     * Constructor.
     *
     * @param trackType
     * @throws SchemaException
     */
    @Inject
    public TrackFeatureProvider(@TrackType SimpleFeatureType trackType) throws SchemaException {
        this.featureBuilder = new SimpleFeatureBuilder(trackType);
    }

    @Override
    public Track get() {
        return new TrackWrapper(featureBuilder.buildFeature(null));
    }
}
