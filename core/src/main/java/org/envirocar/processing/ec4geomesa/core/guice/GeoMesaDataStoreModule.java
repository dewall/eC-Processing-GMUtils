package org.envirocar.processing.ec4geomesa.core.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.apache.log4j.Logger;
import org.envirocar.processing.ec4geomesa.core.GeoMesaDB;
import org.envirocar.processing.ec4geomesa.core.feature.store.MeasurementFeatureStore;
import org.envirocar.processing.ec4geomesa.core.feature.store.TrackFeatureStore;
import org.envirocar.processing.ec4geomesa.core.feature.store.AbstractFeatureStore;
import org.geotools.data.DataStore;
import org.geotools.geometry.jts.JTSFactoryFinder;

/**
 *
 * @author dewall
 */
public class GeoMesaDataStoreModule extends AbstractModule {

    private static final Logger LOGGER = Logger.getLogger(GeoMesaDataStoreModule.class);

    @Override
    protected void configure() {
        bind(GeoMesaDB.class);
        
        Multibinder<AbstractFeatureStore> multibinder = Multibinder.newSetBinder(binder(), AbstractFeatureStore.class);
        multibinder.addBinding().to(TrackFeatureStore.class);
        multibinder.addBinding().to(MeasurementFeatureStore.class);
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
