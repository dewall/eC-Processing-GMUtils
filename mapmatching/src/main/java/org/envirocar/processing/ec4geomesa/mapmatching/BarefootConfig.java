package org.envirocar.processing.ec4geomesa.mapmatching;

/**
 *
 * @author dewall
 */
public interface BarefootConfig {
    
    String BAREFOOTCONFIG = "BarefootConfig";

    String PROPERTY_PG_HOST = "host";
    String PROPERTY_PG_PORT = "port";
    String PROPERTY_PG_DATABASE = "database";
    String PROPERTY_PG_TABLE = "table";
    String PROPERTY_PG_USER = "user";
    String PROPERTY_PG_PW = "password";

    String PROPERTY_MM_SIGMA = "sigma";
    String PROPERTY_MM_LAMBDA = "lambda";
    String PROPERTY_MM_MAXDISTANCE = "maxdistance";
    String PROPERTY_MM_MAXRADIUS = "maxradius";
    
}
