package org.envirocar.processing.ec4geomesa;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import java.util.Map;
import org.envirocar.processing.ec4geomesa.core.GeoMesaDB;
import org.envirocar.processing.ec4geomesa.core.guice.GeoMesaDataStoreModule;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author dewall
 */
public class GuiceTest {

    private Injector injector;

    @Before
    public void before() {
        this.injector = Guice.createInjector(new GeoMesaDataStoreModule());
    }

    @Test
    public void testDataStoreProperties() {
        Map<String, String> datastoreConfig = injector.getInstance(
                Key.get(Map.class, Names.named(GeoMesaDB.GEOMESACONFIG)));
        assertTrue(!datastoreConfig.isEmpty());
    }
}
