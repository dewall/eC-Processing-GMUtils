package org.envirocar.processing.ec4geomesa.core.feature.provider;

import com.google.common.base.Joiner;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.envirocar.processing.ec4geomesa.core.entity.Track;
import org.envirocar.processing.ec4geomesa.core.feature.wrapper.TrackWrapper;
import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.locationtech.geomesa.utils.interop.SimpleFeatureTypes;
import org.opengis.feature.simple.SimpleFeatureType;
import org.envirocar.processing.ec4geomesa.core.feature.schema.TrackConstants;
import org.envirocar.processing.ec4geomesa.core.guice.annotations.TrackType;

/**
 *
 * @author dewall
 */
public class TrackFeatureProvider implements Provider<Track>, TrackConstants {

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
