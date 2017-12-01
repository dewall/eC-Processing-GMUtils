package org.envirocar.processing.ec4geomesa.core;

import java.util.HashMap;
import java.util.Map;

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

    private static final String DEFAULT_INSTANCE_ID = "accumulo";
    private static final String DEFAULT_ZOOKEEPERS = "zookeeper";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "GisPwd";
    private static final String DEFAULT_TABLE_NAME = "envirocar";
    private static final String DEFAULT_AUTHS = "";
    private static final String DEFAULT_VISIBILITY = "";


    public static Map<String, String> getDefaultDataStoreConf() {
        return toDataStoreConf(
                DEFAULT_INSTANCE_ID,
                DEFAULT_ZOOKEEPERS,
                DEFAULT_USER,
                DEFAULT_PASSWORD,
                DEFAULT_TABLE_NAME,
                DEFAULT_AUTHS,
                DEFAULT_VISIBILITY);
    }

    private static Map<String, String> toDataStoreConf(
            String instanceId, String zookeepers, String user, String password,
            String tableName, String auths, String visibility) {
        Map<String, String> config = new HashMap<>();
        config.put(KEY_INSTANCE_ID, instanceId);
        config.put(KEY_ZOOKEEPERS, zookeepers);
        config.put(KEY_USER, user);
        config.put(KEY_PASSWORD, password);
        config.put(KEY_TABLE_NAME, tableName);
        config.put(KEY_AUTHS, auths);
        config.put(KEY_VISIBILITY, visibility);
        return config;
    }
}
