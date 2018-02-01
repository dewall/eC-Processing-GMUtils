package org.envirocar.processing.ec4geomesa.core.feature.schema;

import com.google.common.collect.Lists;
import java.util.List;

/**
 *
 * @author dewall
 */
public interface TrackSchema extends SchemaDefaults {

    String TABLE_NAME = "tracks";

    String ATTRIB_TRACK_ID = "TRACK_ID";
    String ATTRIB_TRACK_STARTTIME = "START_TIME";
    String ATTRIB_TRACK_ENDTIME = "END_TIME";
    String ATTRIB_TRACK_LENGTH = "LENGTH";

    String ATTRIB_CAR_MANUFACTURER = "CAR_MANUFACTURER";
    String ATTRIB_CAR_MODEL = "CAR_MODEL";
    String ATTRIB_CAR_FUELTYPE = "CAR_FUELTYPE";
    String ATTRIB_CAR_CONSTRUCTIONYEAR = "CAR_CONSTRUCTION_YEAR";
    String ATTRIB_CAR_ENGINEDISPLACEMENT = "CAR_ENGINE_DISPLACEMENT";

    List<String> SCHEMA = Lists.newArrayList(
            ATTRIB_TRACK_ID + ":" + TYPE_STRING,
            ATTRIB_TRACK_STARTTIME + ":" + TYPE_DATE,
            ATTRIB_TRACK_ENDTIME + ":" + TYPE_DATE,
            ATTRIB_TRACK_LENGTH + ":" + TYPE_FLOAT,
            ATTRIB_CAR_MANUFACTURER + ":" + TYPE_STRING,
            ATTRIB_CAR_MODEL + ":" + TYPE_STRING,
            ATTRIB_CAR_FUELTYPE + ":" + TYPE_STRING,
            ATTRIB_CAR_CONSTRUCTIONYEAR + ":" + TYPE_INTEGER,
            ATTRIB_CAR_ENGINEDISPLACEMENT + ":" + TYPE_INTEGER,
            ATTRIB_GEOM + ":" + TYPE_LINESTRING + ":" + ATTRIB_SRID
    );
    
}
