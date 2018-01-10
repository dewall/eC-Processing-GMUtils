package org.envirocar.processing.ec4geomesa.core.decoding;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static org.envirocar.processing.ec4geomesa.core.decoding.EnvirocarJSONUtils.readCarSensor;
import static org.envirocar.processing.ec4geomesa.core.decoding.JSONConstants.EC_PROPERTIES_ID;
import static org.envirocar.processing.ec4geomesa.core.decoding.JSONConstants.KEY_PROPERTIES;
import org.envirocar.processing.ec4geomesa.core.entity.CarSensor;
import org.envirocar.processing.ec4geomesa.core.entity.Measurement;
import org.envirocar.processing.ec4geomesa.core.entity.Track;
import org.envirocar.processing.ec4geomesa.core.exception.InvalidMeasurementNumberException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author dewall
 */
public class TrackJsonDecoder implements JSONConstants {

    private final JSONParser jsonParser;
    private final GeometryFactory geometryFactory;
    private final Provider<Track> trackProvider;

    /**
     * Constructor.
     *
     * @param jsonParser
     * @param geometryFactory
     * @param trackProvider
     */
    @Inject
    public TrackJsonDecoder(JSONParser jsonParser, GeometryFactory geometryFactory, Provider<Track> trackProvider) {
        this.jsonParser = jsonParser;
        this.trackProvider = trackProvider;
        this.geometryFactory = geometryFactory;
    }

    public Track parseJson(JSONObject track) throws Exception {
        try {
            JSONObject properties = (JSONObject) track.get(KEY_PROPERTIES);
            // parse properties
            String trackID = (String) properties.get(EC_PROPERTIES_ID);
            double length = readAsDouble(EC_PROPERTIES_LENGTH, properties);

            JSONArray features = (JSONArray) track.get(KEY_FEATURES);
            if (features.size() <= 1) {
                throw new InvalidMeasurementNumberException(String.format(
                        "Track [%s] has an invalid number of Measurements", trackID));
            }

            // parse carsensor
            JSONObject carSensorJson = (JSONObject) properties.get(
                    EC_PROPERTIES_SENSOR);
            CarSensor carSensor = readCarSensor(carSensorJson);

            // parse features        
            List<Measurement> measurements = readMeasurements(trackID, features);

            LineString lineString = measurements.stream()
                    .map(t -> t.getPoint().getCoordinate())
                    .collect(Collectors.collectingAndThen(
                            Collectors.toList(),
                            list -> {
                                Coordinate[] coords = list.toArray(
                                        new Coordinate[list.size()]);
                                return geometryFactory.createLineString(coords);
                            }));

            Track result = trackProvider.get();
            result.setCarSensor(carSensor);
            result.setMeasurements(measurements);
            result.setLength(length);
            result.setLineString(lineString);
            if (measurements.size() >= 2) {
                result.setStartingTime(measurements.get(0).getTime());
                result.setEndingTime(measurements.get(measurements.size() - 1).getTime());
            }

            return result;
        } catch (IllegalArgumentException e) {
            throw new Exception("Unable to parse track", e);
        }
    }
    

    public List<String> parseTrackIDs(String json) throws ParseException {
        List<String> result = new ArrayList<>();
        JSONObject parsedDocument = (JSONObject) jsonParser.parse(json);
        JSONArray tracks = (JSONArray) parsedDocument.get("tracks");

        tracks.forEach((t) -> {
            JSONObject track = (JSONObject) t;
            String trackId = (String) track.get(EC_PROPERTIES_ID);
            result.add(trackId);
        });

        return result;
    }

    private static final double readAsDouble(String key, JSONObject json) {
        double result = 0.0;

        try {
            Object object = json.get(key);
            if (object instanceof Number) {
                result = (double) object;
            } else if (object instanceof String) {
                String string = (String) object;
                result = Double.parseDouble(string);
            }
        } catch (RuntimeException e) {
            LOG.error("Error while parsing double value.", e);
        }

        return result;
    }

    private static final int readAsInt(String key, JSONObject json) {
        int result = 0;

        try {
            Object object = json.get(key);
            if (object instanceof Number) {
                result = ((Long) object).intValue();
            } else if (object instanceof String) {
                result = Integer.valueOf(object.toString());
            }
        } catch (RuntimeException e) {
            LOG.error("Error while parsing int value.", e);
        }

        return result;
    }
}
