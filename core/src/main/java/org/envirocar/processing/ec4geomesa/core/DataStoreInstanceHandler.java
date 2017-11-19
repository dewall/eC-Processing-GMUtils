package org.envirocar.processing.ec4geomesa.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;

/**
 *
 * @author dewall
 */
public class DataStoreInstanceHandler {

    private static final String KEY_INSTANCE_ID = "instanceId";
    private static final String KEY_ZOOKEEPERS = "zookeepers";
    private static final String KEY_USER = "user";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_AUTHS = "auths";
    private static final String KEY_VISIBILITY = "visibilities";
    private static final String KEY_TABLE_NAME = "tableName";

    public static Map<String, String> getAccumuloDataStoreConfig() {
        Map<String, String> config = new HashMap<>();
        config.put(KEY_INSTANCE_ID, "accumulo");
        config.put(KEY_ZOOKEEPERS, "localhost");
        config.put(KEY_USER, "root");
        config.put(KEY_PASSWORD, "GisPwd");
        config.put(KEY_TABLE_NAME, "envirocar");
        config.put(KEY_AUTHS, "");
        config.put(KEY_VISIBILITY, "");
        return config;
    }

    private DataStore dataStore;

    /**
     * Constructor.
     *
     * @throws IOException
     */
    public DataStoreInstanceHandler() throws IOException {
        Map<String, String> dataStoreConfig = new HashMap<>();
        this.dataStore = DataStoreFinder.getDataStore(dataStoreConfig);
    }

}
