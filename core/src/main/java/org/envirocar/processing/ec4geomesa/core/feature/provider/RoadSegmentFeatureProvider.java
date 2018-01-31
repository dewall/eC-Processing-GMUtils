package org.envirocar.processing.ec4geomesa.core.feature.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.envirocar.processing.ec4geomesa.core.entity.RoadSegment;
import org.envirocar.processing.ec4geomesa.core.feature.wrapper.RoadSegmentWrapper;
import org.envirocar.processing.ec4geomesa.core.guice.annotations.RoadSegmentType;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 *
 * @author dewall
 */
public class RoadSegmentFeatureProvider implements Provider<RoadSegment> {

    private final SimpleFeatureBuilder featureBuilder;

    @Inject
    public RoadSegmentFeatureProvider(@RoadSegmentType SimpleFeatureType roadSegmentType) {
        this.featureBuilder = new SimpleFeatureBuilder(roadSegmentType);
    }

    @Override
    public RoadSegment get() {
        return new RoadSegmentWrapper(featureBuilder.buildFeature(null));
    }

}
 