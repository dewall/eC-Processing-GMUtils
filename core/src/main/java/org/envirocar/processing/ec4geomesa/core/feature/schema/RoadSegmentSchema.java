package org.envirocar.processing.ec4geomesa.core.feature.schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.envirocar.processing.ec4geomesa.core.entity.PhenomenonType;

/**
 *
 * @author dewall
 */
public interface RoadSegmentSchema extends SchemaDefaults {

    String TABLE_NAME = "roadsegments";
    String ATTRIBUTE_OSMID = "OSMID";

    ArrayList<String> SCHEMA = new ArrayList<String>() {
        {
            add("OSMID:Integer");
            add("*geom:LineString:srid=4326");

            for (PhenomenonType type : PhenomenonType.values()) {
                add(type.toAvgKey() + ":" + TYPE_DOUBLE);
                add(type.toSumKey() + ":" + TYPE_DOUBLE);
                add(type.toNumKey() + ":" + TYPE_INTEGER);
            }
        }
    };

}
