package org.envirocar.processing.ec4geomesa.core.decoding;

/**
 *
 * @author dewall
 */
interface JSONConstants {

    String KEY_PROPERTIES = "properties";
    String KEY_FEATURES = "features";
    String KEY_COORDINATES = "coordinates";
    String KEY_GEOMETRY = "geometry";

    String EC_PROPERTIES_ID = "id";
    String EC_PROPERTIES_LENGTH = "length";
    String EC_PROPERTIES_SENSOR = "sensor";
    String EC_PROPERTIES_TIME = "time";

    String EC_SENSOR_MODEL = "model";
    String EC_SENSOR_FUELTYPE = "fuelType";
    String EC_SENSOR_ENGINEDIS = "engineDisplacement";
    String EC_SENSOR_CONSTYEAR = "constructionYear";
    String EC_SENSOR_MANUFACTURER = "manufacturer";

    String EC_PHENOMENONS = "phenomenons";
    String EC_PHENOMENON_VALUE = "value";
    String EC_PHENOMENON_UNIT = "unit";
}
