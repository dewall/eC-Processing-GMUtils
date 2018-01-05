package org.envirocar.processing.ec4geomesa.core.feature.schema;

import com.google.common.collect.Lists;
import java.util.List;

/**
 *
 * @author dewall
 */
public interface TrackSchema extends SchemaDefaults {

    String TABLE_NAME = "tracks";

    String ATTRIB_TRACK_ID = "TrackID";
    String ATTRIB_TRACK_STARTTIME = "StartTime";
    String ATTRIB_TRACK_ENDTIME = "EndTime";
    String ATTRIB_TRACK_LENGTH = "Length";

    String ATTRIB_CAR_MANUFACTURER = "CarManufacturer";
    String ATTRIB_CAR_MODEL = "CarModel";
    String ATTRIB_CAR_FUELTYPE = "CarFuelType";
    String ATTRIB_CAR_CONSTRUCTIONYEAR = "CarConstructionYear";
    String ATTRIB_CAR_ENGINEDISPLACEMENT = "CarEngineDisplacement";

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
