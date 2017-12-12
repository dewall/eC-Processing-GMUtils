package org.envirocar.processing.ec4geomesa.mapmatching;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import java.util.Map;
import org.envirocar.processing.ec4geomesa.core.GeoMesaDataStoreModule;
import org.envirocar.processing.ec4geomesa.core.feature.MeasurementFeatureStore;
import org.envirocar.processing.ec4geomesa.core.feature.RoadSegmentFeatureStore;
import org.envirocar.processing.ec4geomesa.core.feature.TrackFeatureStore;
import org.envirocar.processing.ec4geomesa.core.model.RoadSegment;
import org.envirocar.processing.ec4geomesa.core.model.Track;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.opengis.feature.simple.SimpleFeature;

/**
 *
 * @author dewall
 */
public class ArtisanSegmentStatisticsJob {

    private final TrackFeatureStore trackStore;
    private final MeasurementFeatureStore measurementStore;
    private final RoadSegmentFeatureStore roadsegmentStore;
    private final TrackMapMatcher mapMatcher;

    @Inject
    public ArtisanSegmentStatisticsJob(
            TrackFeatureStore trackStore,
            MeasurementFeatureStore measurementStore,
            RoadSegmentFeatureStore roadsegmentStore,
            TrackMapMatcher mapMatcher) {
        this.trackStore = trackStore;
        this.measurementStore = measurementStore;
        this.roadsegmentStore = roadsegmentStore;
        this.mapMatcher = mapMatcher;

        // Create Roadsegment table when it does not already exist
        roadsegmentStore.createTable();
    }

    private void runJob() {
        SimpleFeatureIterator features = this.trackStore.getAllTracks().features();
        while (features.hasNext()) {
            SimpleFeature next = features.next();
            String trackId = (String) next.getAttribute("TrackID");
            Track track = this.trackStore.getByID(trackId, true);

            Map<Long, RoadSegment> segmentStatistics = mapMatcher.computeSegmentStatistics(track);
            segmentStatistics.entrySet()
                    .stream()
                    .forEach(s -> {
                        RoadSegment existingSegment = roadsegmentStore.getByID("" + s.getKey());
                        if (existingSegment != null) {
                            existingSegment.addValue(s.getValue());
                            roadsegmentStore.update(existingSegment);
                        } else {
                            roadsegmentStore.store(s.getValue());
                        }
                    });
        }
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(
                new GeoMesaDataStoreModule(),
                new MapMatcherModule());
        ArtisanSegmentStatisticsJob instance = injector.getInstance(ArtisanSegmentStatisticsJob.class);
        instance.runJob();
    }
}
