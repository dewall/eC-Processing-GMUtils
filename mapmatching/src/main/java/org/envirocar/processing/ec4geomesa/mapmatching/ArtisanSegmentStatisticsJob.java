package org.envirocar.processing.ec4geomesa.mapmatching;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.envirocar.processing.ec4geomesa.core.GeoMesaDataStoreModule;
import org.envirocar.processing.ec4geomesa.core.feature.MeasurementFeatureStore;
import org.envirocar.processing.ec4geomesa.core.feature.RoadSegmentFeatureStore;
import org.envirocar.processing.ec4geomesa.core.feature.TrackFeatureStore;

/**
 *
 * @author dewall
 */
public class ArtisanSegmentStatisticsJob {

    private final TrackFeatureStore trackStore;
    private final MeasurementFeatureStore measurementStore;
    private final RoadSegmentFeatureStore roadsegmentStore;

    @Inject
    public ArtisanSegmentStatisticsJob(
            TrackFeatureStore trackStore,
            MeasurementFeatureStore measurementStore,
            RoadSegmentFeatureStore roadsegmentStore) {
        this.trackStore = trackStore;
        this.measurementStore = measurementStore;
        this.roadsegmentStore = roadsegmentStore;
    }

    private void runJob() {

    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new GeoMesaDataStoreModule());
        ArtisanSegmentStatisticsJob instance = injector.getInstance(ArtisanSegmentStatisticsJob.class);
        instance.runJob();
    }
}
