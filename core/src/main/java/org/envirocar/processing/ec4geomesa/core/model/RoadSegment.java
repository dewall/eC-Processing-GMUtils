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

    public void addValue(RoadSegment other) {
        Map<String, Double> otherSumValues = other.getSummedValues();
        Map<String, Double> otherAvgValues = other.getAvgValues();
        Map<String, Integer> otherNumValues = other.getNumValues();

        other.getAvgValues().keySet()
                .forEach(key -> this.addValue(key, otherSumValues.get(key), otherNumValues.get(key)));
    }

    public void addValue(String key, double value) {
        this.addValue(key, value, 1);
    }

    public void addValue(String key, double sum, int num) {
        if (this.sumValues.containsKey(key) && this.numValues.containsKey(key)) {
            Double sumValue = this.sumValues.get(key);
            Integer numValue = this.numValues.get(key);
            double avgValue = 0;
            
            sumValue += sum;
            numValue += num;
            avgValue = sumValue / numValue;
            
            this.sumValues.put(key, sumValue);
            this.avgValues.put(key, avgValue);
            this.numValues.put(key, numValue);

        } else {
            double avg = sum / num;
            this.setValue(key, sum, avg, num);
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
