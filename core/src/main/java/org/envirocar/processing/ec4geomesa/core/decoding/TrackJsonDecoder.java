package org.envirocar.processing.ec4geomesa.core.decoding;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import static org.envirocar.processing.ec4geomesa.core.decoding.JSONConstants.EC_PROPERTIES_ID;
import static org.envirocar.processing.ec4geomesa.core.decoding.JSONConstants.KEY_PROPERTIES;
import org.envirocar.processing.ec4geomesa.core.entity.CarSensor;
import org.envirocar.processing.ec4geomesa.core.entity.Measurement;
import org.envirocar.processing.ec4geomesa.core.entity.PhenomenonType;
import org.envirocar.processing.ec4geomesa.core.entity.Track;
import org.envirocar.processing.ec4geomesa.core.entity.wrapper.AbstractFeatureWrapper;
import org.envirocar.processing.ec4geomesa.core.entity.wrapper.CarSensorWrapper;
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

    private static final Logger LOG = Logger.getLogger(TrackJsonDecoder.class);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);

    private final JSONParser jsonParser;
    private final GeometryFactory geometryFactory;
    private final Provider<Track> trackProvider;
    private final Provider<Measurement> measurmentProvider;

    /**
     * Constructor.
     *
     * @param jsonParser
     * @param geometryFactory
     * @param trackProvider
     * @param measurementProvider
     */
    @Inject
    public TrackJsonDecoder(JSONParser jsonParser,
            GeometryFactory geometryFactory,
            Provider<Track> trackProvider,
            Provider<Measurement> measurementProvider) {
        this.jsonParser = jsonParser;
        this.trackProvider = trackProvider;
        this.geometryFactory = geometryFactory;
        this.measurmentProvider = measurementProvider;
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
            CarSensor carSensor = new CarSensorWrapper(((AbstractFeatureWrapper) result).getFeature());
            carSensor = readCarSensor(carSensor, carSensorJson);
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

    private CarSensor readCarSensor(CarSensor result, JSONObject sensorJson) {
        JSONObject propertiesJson = (JSONObject) sensorJson.get(KEY_PROPERTIES);

        String manufacturer = (String) propertiesJson.get(EC_SENSOR_MANUFACTURER);
        String model = (String) propertiesJson.get(EC_SENSOR_MODEL);
        String fueltype = (String) propertiesJson.get(EC_SENSOR_FUELTYPE);
        int engineDis = readAsInt(EC_SENSOR_ENGINEDIS, propertiesJson);
        int consYear = readAsInt(EC_SENSOR_CONSTYEAR, propertiesJson);

        result.setManufacturer(manufacturer);
        result.setModel(model);
        result.setFuelType(fueltype);
        result.setEngineDisplacement(engineDis);
        result.setConstructionYear(consYear);

        return result;
    }

    private List<Measurement> readMeasurements(String trackID,
            JSONArray featureJson) {
        List<Measurement> measurements = new ArrayList<>();

        // Parse measurements
        featureJson.forEach((Object f) -> {
            JSONObject feature = (JSONObject) f;

            // Get the geometry
            JSONObject geometry = (JSONObject) feature.get(KEY_GEOMETRY);
            JSONArray coordinates = (JSONArray) geometry.get(KEY_COORDINATES);

            Double latitude = (Double) coordinates.get(1);
            Double longitude = (Double) coordinates.get(0);
            Coordinate c = new Coordinate(longitude, latitude);
            Point point = geometryFactory.createPoint(c);

            // Get the properties
            JSONObject properties = (JSONObject) feature.get(KEY_PROPERTIES);
            String id = (String) properties.get(EC_PROPERTIES_ID);
            String timeString = (String) properties.get(EC_PROPERTIES_TIME);

            // parse the date
            Date time = null;
            try {
                time = DATE_FORMAT.parse(timeString);
            } catch (java.text.ParseException ex) {
                LOG.error("Error while parsing date value.", ex);
            }

            JSONObject phenomenonsJson = (JSONObject) properties.get(EC_PHENOMENONS);
            Map<PhenomenonType, Double> phenomenons = parsePhenomenons(phenomenonsJson);

            Measurement m = measurmentProvider.get();
            m.setId(id);
            m.setTrackId(trackID);
            m.setPoint(point);
            m.setTime(time);
            m.setPhenomenons(phenomenons);
            measurements.add(m);
        });

        return measurements;
    }

    private Map<PhenomenonType, Double> parsePhenomenons(JSONObject phenomenonsJson) {
        Map<PhenomenonType, Double> result = new HashMap<>();
        Set<String> keySet = phenomenonsJson.keySet();

        for (String key : keySet) {
            PhenomenonType type = PhenomenonType.getByReadableName(key);
            if (type != null) {
                JSONObject phenomenonJson = (JSONObject) phenomenonsJson.get(key);
                double value = readAsDouble(EC_PHENOMENON_VALUE, phenomenonJson);
                result.put(type, value);
            } else {
                LOG.info("Unknown phenomenon type -> " + key);
            }
        }

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
