package org.envirocar.processing.ec4geomesa.core;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;
import java.io.IOException;
import java.util.Map;
import org.apache.log4j.Logger;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;

/**
 *
 * @author dewall
 */
public class GeoMesaStoreModule extends AbstractModule {

    private static final Logger LOGGER = Logger.getLogger(GeoMesaStoreModule.class);

    private final Map<String, String> geomesaConfig;
    private final DataStore geomesaDatastore;

    /**
     * Constructor.
     *
     * @param geomesaConfig
     */
    public GeoMesaStoreModule(Map<String, String> geomesaConfig) {
        try {
            this.geomesaConfig = geomesaConfig;
            this.geomesaDatastore = DataStoreFinder.getDataStore(geomesaConfig);
        } catch (IOException ex) {
            LOGGER.error("Unable to find GeoMesa datastore", ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected void configure() {
        MapBinder<String, String> newMapBinder = MapBinder.newMapBinder(binder(), String.class, String.class);
        geomesaConfig.entrySet().stream()
                .forEach(e -> newMapBinder.addBinding(e.getKey()).toInstance(e.getValue()));

        bind(DataStore.class).toInstance(this.geomesaDatastore);
    }

}
