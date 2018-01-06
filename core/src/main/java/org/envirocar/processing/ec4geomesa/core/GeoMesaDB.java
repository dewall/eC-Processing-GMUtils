package org.envirocar.processing.ec4geomesa.core;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.envirocar.processing.ec4geomesa.core.feature.store.AbstractFeatureStore;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;

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

    @Inject
    public GeoMesaDB(@Named(GEOMESACONFIG) Map<String, String> geomesaConfig,
            Set<AbstractFeatureStore<?>> featureStores) {
        try {
            this.config = geomesaConfig;
            this.datastore = DataStoreFinder.getDataStore(geomesaConfig);
            
            for(AbstractFeatureStore store : featureStores){
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public DataStore getDatastore() {
        return datastore;
    }

}
