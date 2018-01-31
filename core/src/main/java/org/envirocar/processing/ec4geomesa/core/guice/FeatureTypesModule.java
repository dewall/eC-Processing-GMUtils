package org.envirocar.processing.ec4geomesa.core.guice;

import org.envirocar.processing.ec4geomesa.core.guice.annotations.MeasurementType;
import org.envirocar.processing.ec4geomesa.core.guice.annotations.TrackType;
import com.google.inject.AbstractModule;
import org.envirocar.processing.ec4geomesa.core.entity.Measurement;
import org.envirocar.processing.ec4geomesa.core.entity.RoadSegment;
import org.envirocar.processing.ec4geomesa.core.entity.Track;
import org.envirocar.processing.ec4geomesa.core.feature.provider.MeasurementFeatureProvider;
import org.envirocar.processing.ec4geomesa.core.feature.provider.TrackFeatureProvider;
import org.envirocar.processing.ec4geomesa.core.feature.provider.MeasurementFeatureTypeProvider;
import org.envirocar.processing.ec4geomesa.core.feature.provider.RoadSegmentFeatureProvider;
import org.envirocar.processing.ec4geomesa.core.feature.provider.TrackFeatureTypeProvider;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 *
 * @author dewall
 */
public class FeatureTypesModule extends AbstractModule {

    @Override
    protected void configure() {
        // bindings for simplefeature types
        bind(SimpleFeatureType.class)
                .annotatedWith(TrackType.class)
                .toProvider(TrackFeatureTypeProvider.class);
        bind(SimpleFeatureType.class)
                .annotatedWith(MeasurementType.class)
                .toProvider(MeasurementFeatureTypeProvider.class);

        // factories for simplefeature wrapper
        bind(Track.class).toProvider(TrackFeatureProvider.class);
        bind(Measurement.class).toProvider(MeasurementFeatureProvider.class);
        bind(RoadSegment.class).toProvider(RoadSegmentFeatureProvider.class);
    }

}
