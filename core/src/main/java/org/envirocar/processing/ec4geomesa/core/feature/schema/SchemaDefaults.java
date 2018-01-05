package org.envirocar.processing.ec4geomesa.core.feature.schema;

/**
 *
 * @author dewall
 */
public interface SchemaDefaults {

    String DEFAULT_SRID = "4326";
    String ATTRIB_GEOM = "*geom";
    String ATTRIB_SRID = "srid=" + DEFAULT_SRID;

    String TYPE_STRING = "String";
    String TYPE_DATE = "Date";
    String TYPE_INTEGER = "Integer";
    String TYPE_FLOAT = "Float";

    String TYPE_POINT = "Point";
    String TYPE_LINESTRING = "LineString";
}
