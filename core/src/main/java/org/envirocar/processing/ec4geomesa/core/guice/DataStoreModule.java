package org.envirocar.processing.ec4geomesa.core.guice;

import org.envirocar.processing.ec4geomesa.core.guice.annotations.InitializeTable;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.vividsolutions.jts.geom.GeometryFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.envirocar.processing.ec4geomesa.core.GeoMesaDB;

import static org.envirocar.processing.ec4geomesa.core.GeoMesaDB.GEOMESACONFIG;
import static org.envirocar.processing.ec4geomesa.core.GeoMesaDB.PROPERTY_AUTHS;
import static org.envirocar.processing.ec4geomesa.core.GeoMesaDB.PROPERTY_INSTANCE_ID;
import static org.envirocar.processing.ec4geomesa.core.GeoMesaDB.PROPERTY_PASSWORD;
import static org.envirocar.processing.ec4geomesa.core.GeoMesaDB.PROPERTY_TABLE_NAME;
import static org.envirocar.processing.ec4geomesa.core.GeoMesaDB.PROPERTY_USER;
import static org.envirocar.processing.ec4geomesa.core.GeoMesaDB.PROPERTY_VISIBILITY;
import static org.envirocar.processing.ec4geomesa.core.GeoMesaDB.PROPERTY_ZOOKEEPERS;
import org.envirocar.processing.ec4geomesa.core.decoding.TrackJsonDecoder;
import org.envirocar.processing.ec4geomesa.core.entity.Measurement;
import org.envirocar.processing.ec4geomesa.core.entity.Track;
import org.envirocar.processing.ec4geomesa.core.feature.factory.MeasurementFeatureFactory;
import org.envirocar.processing.ec4geomesa.core.feature.factory.TrackFeatureFactory;
import org.envirocar.processing.ec4geomesa.core.feature.provider.InitializeTableInterceptor;
import org.geotools.data.DataStore;
import org.geotools.geometry.jts.JTSFactoryFinder;

/**
 *
 * @author dewall
 */
public class DataStoreModule extends AbstractModule {

    private static final Logger LOGGER = Logger.getLogger(DataStoreModule.class);

    private final Map<String, String> geomesaConfig;

    /**
     * Constructor.
     */
    public DataStoreModule() {
        this(new HashMap<>());
    }

    /**
     * Constructor.
     *
     * @param config GeoMesa Configuration
     */
    public DataStoreModule(Map<String, String> config) {
        this.geomesaConfig = config != null ? config : new HashMap<>();
    }

    @Override
    protected void configure() {
//        bind(TrackJsonDecoder.class);


//        Multibinder<AbstractFeatureStore> multibinder = Multibinder.newSetBinder(binder(), AbstractFeatureStore.class);
//        multibinder.addBinding().to(TrackFeatureStore.class);
//        multibinder.addBinding().to(MeasurementFeatureStore.class);
        install(new PropertiesModule());
        install(new FeatureTypesModule());

        // register interceptor for creating tables based on SimpleFeatureTypes
        InitializeTableInterceptor interceptor = new InitializeTableInterceptor();
        requestInjection(interceptor);
        bindInterceptor(Matchers.any(),
                Matchers.annotatedWith(InitializeTable.class),
                new InitializeTableInterceptor());
    }

    @Provides
    public GeoMesaDB provideGeoMesaDB() {
        return new GeoMesaDB(geomesaConfig);
    }

    @Provides
    public DataStore provideDataStore(GeoMesaDB geomesa) {
        return geomesa.getDatastore();
    }

    @Provides
    @Singleton
    public GeometryFactory provideGeometryFactory() {
        return JTSFactoryFinder.getGeometryFactory();
    }

}
