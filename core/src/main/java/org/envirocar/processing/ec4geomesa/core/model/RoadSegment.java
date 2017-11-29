package org.envirocar.processing.ec4geomesa.core.model;

import com.vividsolutions.jts.geom.LineString;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dewall
 */
public class RoadSegment {

    private final int osmId;
    private final LineString segment;

    // different statistics
    private final Map<String, Double> summedValues;
    private final Map<String, Double> avgValues;
    private final Map<String, Integer> numValues;

    /**
     * Constructor.
     *
     * @param osmId
     * @param segment
     */
    public RoadSegment(final int osmId, final LineString segment) {
        this.osmId = osmId;
        this.segment = segment;

        this.summedValues = new HashMap<>();
        this.avgValues = new HashMap<>();
        this.numValues = new HashMap<>();
    }

    public int getOsmId() {
        return osmId;
    }

    public LineString getSegment() {
        return segment;
    }

    public Map<String, Double> getSummedValues() {
        return summedValues;
    }

    public Map<String, Double> getAvgValues() {
        return avgValues;
    }

    public Map<String, Integer> getNumValues() {
        return numValues;
    }

    public Double getSumValue(String key) {
        return (Double) getValue(this.summedValues, key);
    }

    public Double getAvgValue(String key) {
        return (Double) getValue(this.avgValues, key);
    }

    public Integer getNumValue(String key) {
        return (Integer) getValue(this.numValues, key);
    }

    private Number getValue(Map<String, ? extends Number> map, String key) {
        Number number = map.get(key);
        return number == null ? number : 0;
    }
}
