package org.envirocar.processing.ec4geomesa.core.decoding;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.envirocar.processing.ec4geomesa.core.model.CarSensor;
import org.envirocar.processing.ec4geomesa.core.model.Measurement;
import org.envirocar.processing.ec4geomesa.core.model.Track;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author dewall
 */
public class EnvirocarJSONUtils implements GeoJSONConstants {

    private static final Logger LOG = Logger.getLogger(EnvirocarJSONUtils.class);

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
    private static final JSONParser JSON_PARSER = new JSONParser();
    private static final GeometryFactory GEOMETRY_FACOTRY = JTSFactoryFinder.
            getGeometryFactory();

    static {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static final Track parseTrack(String trackText) throws ParseException {
        JSONObject track = (JSONObject) JSON_PARSER.parse(trackText);
        return parseTrack(track);
    }

    public static final Track parseTrack(JSONObject track) {

        JSONObject properties = (JSONObject) track.get(KEY_PROPERTIES);
        JSONArray features = (JSONArray) track.get(KEY_FEATURES);

        // parse properties
        String trackID = (String) properties.get(EC_PROPERTIES_ID);
        double length = readAsDouble(EC_PROPERTIES_LENGTH, properties);

        // parse carsensor
        JSONObject carSensorJson = (JSONObject) properties.get(
                EC_PROPERTIES_SENSOR);
        CarSensor carSensor = readCarSensor(carSensorJson);

        // parse features        
        List<Measurement> measurements = readMeasurements(features);

        LineString lineString = measurements.stream()
                .map(t -> t.getPoint().getCoordinate())
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        list -> {
                            Coordinate[] coords = list.toArray(
                                    new Coordinate[list.size()]);
                            return GEOMETRY_FACOTRY.createLineString(coords);
                        }));

        Track result = new Track(trackID);
        result.setCarSensor(carSensor);
        result.setMeasurements(measurements);
        result.setLength(length);
        result.setLineString(lineString);

        return result;
    }

    public static CarSensor readCarSensor(JSONObject sensorJson) {
        CarSensor result = new CarSensor();
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

    private static List<Measurement> readMeasurements(
            JSONArray featureJson) {
        List<Measurement> measurements = new ArrayList<>();

        // Parse measurements
        featureJson.forEach((f) -> {
            JSONObject feature = (JSONObject) f;

            // Get the geometry
            JSONObject geometry = (JSONObject) feature.get(KEY_GEOMETRY);
            JSONArray coordinates = (JSONArray) geometry.get(KEY_COORDINATES);

            Double latitude = (Double) coordinates.get(1);
            Double longitude = (Double) coordinates.get(0);
            Coordinate c = new Coordinate(longitude, latitude);
            Point point = GEOMETRY_FACOTRY.createPoint(c);

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

            Measurement m = new Measurement(id, point, time);
            measurements.add(m);
        });

        return measurements;
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
