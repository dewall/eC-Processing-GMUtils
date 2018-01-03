package org.envirocar.processing.ec4geomesa.mapmatching;

import com.bmwcarit.barefoot.matcher.Matcher;
import com.bmwcarit.barefoot.matcher.MatcherCandidate;
import com.bmwcarit.barefoot.matcher.MatcherKState;
import com.bmwcarit.barefoot.matcher.MatcherSample;
import com.bmwcarit.barefoot.road.BaseRoad;
import com.google.inject.Inject;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.envirocar.processing.ec4geomesa.core.model.Measurement;
import org.envirocar.processing.ec4geomesa.core.model.RoadSegment;
import org.envirocar.processing.ec4geomesa.core.model.Track;

/**
 *
 * @author dewall
 */
public class TrackMapMatcher {

    private static final Logger LOGGER = Logger.getLogger(TrackMapMatcher.class);

    private final Matcher matcher;
    private final GeometryFactory factory;
    private final OSMWayFetcher wayFetcher;

    /**
     * Constructor.
     *
     * @param matcher the barefoot matcher which is handling the map matching process.
     * @param factory the geometry factory.
     * @param wayFetcher
     */
    @Inject
    public TrackMapMatcher(Matcher matcher, GeometryFactory factory, OSMWayFetcher wayFetcher) {
        this.matcher = matcher;
        this.factory = factory;
        this.wayFetcher = wayFetcher;
    }
    
    /**
     *
     * @param track
     * @return
     */
    public Map<Long, RoadSegment> computeSegmentStatistics(Track track) {
        Map<Long, RoadSegment> result = new HashMap<>();

        // setup map of measurements
        Map<String, Measurement> measurementMap = new HashMap<>();
        track.getMeasurements()
                .stream()
                .forEach((t) -> measurementMap.put(t.getId(), t));

        // first create the matching samples
        List<MatcherSample> samples = track.getMeasurements()
                .stream()
                .map(m -> new MatcherSample(m.getId(), m.getTimeAsLong(), convertFromJTSPoint(m.getPoint())))
                .collect(Collectors.toList());

        // map match
        MatcherKState state = matcher.mmatch(samples, 15, 4000);
        if (state.isEmpty()) {
            return null;
        }

        try {
            List<MatcherSample> matcherSamples = state.samples();
            List<MatcherCandidate> matcherSequence = state.sequence();

            if (matcherSamples != null && matcherSequence != null) {
                LOGGER.info("TrackID -> " + track.getId()
                        + ", Measurements -> " + measurementMap.size()
                        + ", MatcherSamples -> " + matcherSamples.size());
                for (int i = 0, size = matcherSamples.size(); i < size; i++) {
                    MatcherSample matcherSample = matcherSamples.get(i);
                    MatcherCandidate matcherCandidate = matcherSequence.get(i);
                    Measurement measurement = measurementMap.get(matcherSample.id());

                    BaseRoad edge = matcherCandidate.point().edge().base();
                    Long osmid = edge.refid();

                    RoadSegment roadSegment = result.get(osmid);
                    if (roadSegment == null) {
                        LineString waygeometry = (LineString) wayFetcher.fetchOSMWayGeometry(osmid);
                        roadSegment = new RoadSegment(osmid.intValue(), waygeometry);
                        result.put(osmid, roadSegment);
                    }
                    roadSegment.addPhenomenons(measurement.getPhenomenons());
                }
            }
        } catch (NoSuchElementException e) {
            LOGGER.error("unable to match track " + track.getId());
        }

        return result;
    }

    private com.esri.core.geometry.Point convertFromJTSPoint(Point point) {
        return new com.esri.core.geometry.Point(point.getX(), point.getY());
    }

}
