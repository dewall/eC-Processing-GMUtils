package org.envirocar.processing.ec4geomesa.core.guice;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.envirocar.processing.ec4geomesa.core.guice.DataStoreModule;
import static org.envirocar.processing.ec4geomesa.core.GeoMesaDB.GEOMESACONFIG;
import static org.envirocar.processing.ec4geomesa.core.GeoMesaDB.PROPERTY_AUTHS;
import static org.envirocar.processing.ec4geomesa.core.GeoMesaDB.PROPERTY_INSTANCE_ID;
import static org.envirocar.processing.ec4geomesa.core.GeoMesaDB.PROPERTY_PASSWORD;
import static org.envirocar.processing.ec4geomesa.core.GeoMesaDB.PROPERTY_TABLE_NAME;
import static org.envirocar.processing.ec4geomesa.core.GeoMesaDB.PROPERTY_USER;
import static org.envirocar.processing.ec4geomesa.core.GeoMesaDB.PROPERTY_VISIBILITY;
import static org.envirocar.processing.ec4geomesa.core.GeoMesaDB.PROPERTY_ZOOKEEPERS;

/**
 *
 * @author dewall
 */
public class PropertiesModule extends AbstractModule {

    private static final Logger LOGGER = Logger.getLogger(DataStoreModule.class);
    private static final String PROPERTIES_FILE = "/geomesa.properties";

    private final Map<String, String> geomesaConfig;

    /**
     * Constructor.
     */
    public PropertiesModule() {
        this(new HashMap<>());
    }

    /**
     * Constructor.
     *
     * @param config GeoMesa Configuration
     */
    public PropertiesModule(Map<String, String> config) {
        this.geomesaConfig = config != null ? config : new HashMap<>();
    }

    @Override
    protected void configure() {
        if (geomesaConfig.isEmpty()) {
            try {
                Properties p = getProperties(PROPERTIES_FILE);
                if (p.containsKey(PROPERTY_INSTANCE_ID)) {
                    geomesaConfig.put(PROPERTY_INSTANCE_ID, p.getProperty(PROPERTY_INSTANCE_ID));
                }
                if (p.containsKey(PROPERTY_ZOOKEEPERS)) {
                    geomesaConfig.put(PROPERTY_ZOOKEEPERS, p.getProperty(PROPERTY_ZOOKEEPERS));
                }
                if (p.containsKey(PROPERTY_USER)) {
                    geomesaConfig.put(PROPERTY_USER, p.getProperty(PROPERTY_USER));
                }
                if (p.containsKey(PROPERTY_PASSWORD)) {
                    geomesaConfig.put(PROPERTY_PASSWORD, p.getProperty(PROPERTY_PASSWORD));
                }
                if (p.containsKey(PROPERTY_AUTHS)) {
                    geomesaConfig.put(PROPERTY_AUTHS, p.getProperty(PROPERTY_AUTHS));
                }
                if (p.containsKey(PROPERTY_VISIBILITY)) {
                    geomesaConfig.put(PROPERTY_VISIBILITY, p.getProperty(PROPERTY_VISIBILITY));
                }
                if (p.containsKey(PROPERTY_TABLE_NAME)) {
                    geomesaConfig.put(PROPERTY_TABLE_NAME, p.getProperty(PROPERTY_TABLE_NAME));
                }
            } catch (IOException ex) {
                LOGGER.error("Error while reading geomesa.properties.", ex);
            }
        }

        bind(Map.class)
                .annotatedWith(Names.named(GEOMESACONFIG))
                .toInstance(geomesaConfig);
        bind(String.class)
                .annotatedWith(Names.named(PROPERTY_INSTANCE_ID))
                .toInstance(this.geomesaConfig.get(PROPERTY_INSTANCE_ID));
        bind(String.class)
                .annotatedWith(Names.named(PROPERTY_ZOOKEEPERS))
                .toInstance(this.geomesaConfig.get(PROPERTY_ZOOKEEPERS));
        bind(String.class)
                .annotatedWith(Names.named(PROPERTY_USER))
                .toInstance(this.geomesaConfig.get(PROPERTY_USER));
        bind(String.class)
                .annotatedWith(Names.named(PROPERTY_AUTHS))
                .toInstance(this.geomesaConfig.get(PROPERTY_AUTHS));
        bind(String.class)
                .annotatedWith(Names.named(PROPERTY_VISIBILITY))
                .toInstance(this.geomesaConfig.get(PROPERTY_VISIBILITY));
        bind(String.class)
                .annotatedWith(Names.named(PROPERTY_TABLE_NAME))
                .toInstance(this.geomesaConfig.get(PROPERTY_TABLE_NAME));

    }

    private Properties getProperties(String propertiesFile) throws IOException {
        Properties result = new Properties();
        InputStream inputStream = DataStoreModule.class.getResourceAsStream(propertiesFile);

        if (inputStream != null) {
            try {
                result.load(inputStream);
            } finally {
                inputStream.close();
            }
        }

        return result;
    }

}