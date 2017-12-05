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
    private final Map<String, Double> sumValues;
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

        this.sumValues = new HashMap<>();
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
        return sumValues;
    }

    public Map<String, Double> getAvgValues() {
        return avgValues;
    }

    public Map<String, Integer> getNumValues() {
        return numValues;
    }

    public Double getSumValue(String key) {
        return (Double) getValue(this.sumValues, key);
    }

    public Double getAvgValue(String key) {
        return (Double) getValue(this.avgValues, key);
    }

    public Integer getNumValue(String key) {
        return (Integer) getValue(this.numValues, key);
    }

    public void addPhenomenons(Map<String, Double> phenomenons) {
        phenomenons.entrySet()
                .stream()
                .forEach(e -> addValue(e.getKey(), e.getValue()));
    }

    public void addValue(String key, double value) {
        if (this.sumValues.containsKey(key) && this.avgValues.containsKey(key) && this.numValues.containsKey(key)) {
            Double sumValue = this.sumValues.get(key);
            this.sumValues.put(key, sumValue + value);

            Integer numValue = this.numValues.get(key);
            numValue++;
            this.numValues.put(key, numValue);

            Double avgValue = this.avgValues.get(key);
            this.avgValues.put(key, sumValue / numValue);
        } else {
            this.setValue(key, value, value, 1);
        }
    }

    public void setValue(String key, double sumValue, double avgValue, int numValue) {
        this.sumValues.put(key, sumValue);
        this.avgValues.put(key, avgValue);
        this.numValues.put(key, numValue);
    }

    private Number getValue(Map<String, ? extends Number> map, String key) {
        Number number = map.get(key);
        return number == null ? number : 0;
    }
}
