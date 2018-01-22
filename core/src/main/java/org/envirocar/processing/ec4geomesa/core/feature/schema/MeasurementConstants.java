package org.envirocar.processing.ec4geomesa.core.feature.schema;

import java.util.ArrayList;
import org.envirocar.processing.ec4geomesa.core.entity.PhenomenonType;

/**
 *
 * @author dewall
 */
public interface MeasurementConstants extends SchemaDefaults {

    String TABLE_NAME = "measurements";

    String ATTRIB_MEASUREMENT_ID = "MEASUREMENT_ID";
    String ATTRIB_TRACK_ID = "TRACK_ID";
    String ATTRIB_TIME = "TIME";

    ArrayList<String> SCHEMA = new ArrayList<String>() {
        {
            add(ATTRIB_MEASUREMENT_ID + ":" + TYPE_STRING);
            add(ATTRIB_TRACK_ID + ":" + TYPE_STRING);
            add(ATTRIB_TIME + ":" + TYPE_DATE);

            for (PhenomenonType p : PhenomenonType.values()) {
                add(p.name() + ":" + TYPE_DOUBLE);
            }

            add(ATTRIB_GEOM + ":" + TYPE_POINT + ":" + ATTRIB_SRID);
        }
    };

}