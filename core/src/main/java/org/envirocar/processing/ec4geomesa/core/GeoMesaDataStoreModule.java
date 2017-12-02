package org.envirocar.processing.ec4geomesa.core;

import com.google.common.io.Closeables;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;

/**
 *
 * @author dewall
 */
public class GeoMesaDataStoreModule extends AbstractModule implements GeoMesaConfig {

    private static final Logger LOGGER = Logger.getLogger(GeoMesaDataStoreModule.class);
    private static final String PROPERTIES_FILE = "/geomesa.properties";

    private final Map<String, String> geomesaConfig;

    /**
     * Default Constructor.
     */
    public GeoMesaDataStoreModule() {
        this(new HashMap<>());
    }

    /**
     * Constructor.
     *
     * @param geomesaConfig
     */
    public GeoMesaDataStoreModule(Map<String, String> geomesaConfig) {
        this.geomesaConfig = geomesaConfig;
    }

    @Provides
    @Singleton
    public DataStore provideDataStore() throws IOException {
        return DataStoreFinder.getDataStore(geomesaConfig);
    }

    @Override
    protected void configure() {
        if (this.geomesaConfig.isEmpty()) {
            try {
                Properties p = getGeoMesaProperties();
                if (p.contains(PROPERTY_INSTANCE_ID)) {
                    geomesaConfig.put(PROPERTY_INSTANCE_ID, p.getProperty(PROPERTY_INSTANCE_ID));
                }
                if (p.contains(PROPERTY_ZOOKEEPERS)) {
                    geomesaConfig.put(PROPERTY_ZOOKEEPERS, p.getProperty(PROPERTY_ZOOKEEPERS));
                }
                if (p.contains(PROPERTY_USER)) {
                    geomesaConfig.put(PROPERTY_USER, p.getProperty(PROPERTY_USER));
                }
                if (p.contains(PROPERTY_PASSWORD)) {
                    geomesaConfig.put(PROPERTY_PASSWORD, p.getProperty(PROPERTY_PASSWORD));
                }
                if (p.contains(PROPERTY_AUTHS)) {
                    geomesaConfig.put(PROPERTY_AUTHS, p.getProperty(PROPERTY_AUTHS));
                }
                if (p.contains(PROPERTY_VISIBILITY)) {
                    geomesaConfig.put(PROPERTY_VISIBILITY, p.getProperty(PROPERTY_VISIBILITY));
                }
                if (p.contains(PROPERTY_TABLE_NAME)) {
                    geomesaConfig.put(PROPERTY_TABLE_NAME, p.getProperty(PROPERTY_TABLE_NAME));
                }
            } catch (IOException ex) {
                LOGGER.error("Error while reading geomesa.properties.", ex);
            }
        }

        bind(Map.class)
                .annotatedWith(Names.named(GEOMESACONFIG))
                .toInstance(geomesaConfig);
    }

    private Properties getGeoMesaProperties() throws IOException {
        Properties result = new Properties();
        InputStream inputStream = GeoMesaDataStoreModule.class.getResourceAsStream(PROPERTIES_FILE);

        if (inputStream != null) {
            try {
                result.load(inputStream);
            } finally {
                Closeables.closeQuietly(inputStream);
            }
        }

        return result;
    }

}
