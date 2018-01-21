package org.envirocar.processing.ec4geomesa.core.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import java.util.ServiceLoader;

/**
 *
 * @author dewall
 */
public class GeoMesaModule extends AbstractModule {

    @Override
    protected void configure() {
        for(Module m : ServiceLoader.load(Module.class)){
            install(m);
        }
    }

}
