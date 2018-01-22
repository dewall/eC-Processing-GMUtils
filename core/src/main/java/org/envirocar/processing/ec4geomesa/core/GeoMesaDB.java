package org.envirocar.processing.ec4geomesa.core;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.io.IOException;
import java.util.Map;
import org.apache.log4j.Logger;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 *
 * @author dewall
 */
@Singleton
public class GeoMesaDB {

    private static final Logger LOGGER = Logger.getLogger(GeoMesaDB.class);

    public static final String GEOMESACONFIG = "GeoMesaConfig";
    public static final String PROPERTY_INSTANCE_ID = "instanceId";
    public static final String PROPERTY_ZOOKEEPERS = "zookeepers";
    public static final String PROPERTY_USER = "user";
    public static final String PROPERTY_PASSWORD = "password";
    public static final String PROPERTY_AUTHS = "auths";
    public static final String PROPERTY_VISIBILITY = "visibilities";
    public static final String PROPERTY_TABLE_NAME = "tableName";

    private final Map<String, String> config;
    private final DataStore datastore;

    /**
     * Constructor.
     * @param geomesaConfig 
     */
//    null;
    public GeoMesaDB(Map<String, String> geomesaConfig) {
//        try {
            this.config = geomesaConfig;
            this.datastore = null;
//            DataStoreFinder.getDataStore(geomesaConfig);

//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }
    }

    public DataStore getDatastore() {
        return datastore;
    }

    public void createTable(SimpleFeatureType featureType) throws IOException {
        if (datastore.getSchema(featureType.getTypeName()) == null) {
            this.datastore.createSchema(featureType);
        }
    }

}
