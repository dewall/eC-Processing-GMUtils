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
import org.envirocar.processing.ec4geomesa.core.feature.provider.MeasurementFeatureProvider;
import org.envirocar.processing.ec4geomesa.core.feature.provider.TrackFeatureProvider;
import org.envirocar.processing.ec4geomesa.core.guice.interceptor.InitializeTableInterceptor;
import org.geotools.data.DataStore;
import org.geotools.geometry.jts.JTSFactoryFinder;

/**
 *
 * @author dewall
 */
public class DataStoreModule extends AbstractModule {

    private static final Logger LOGGER = Logger.getLogger(DataStoreModule.class);

    @Override
    protected void configure() {
        install(new PropertiesModule());

        // register interceptor for creating tables based on SimpleFeatureTypes
        InitializeTableInterceptor interceptor = new InitializeTableInterceptor();
        requestInjection(interceptor);
        bindInterceptor(Matchers.any(),
                Matchers.annotatedWith(InitializeTable.class),
                new InitializeTableInterceptor());
    }

    @Provides
    public GeoMesaDB provideGeoMesaDB(@Named(GeoMesaDB.GEOMESACONFIG) Map<String, String> config) {
        return new GeoMesaDB(config);
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
