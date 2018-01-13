package org.envirocar.processing.ec4geomesa;

import com.google.inject.Guice;
import com.google.inject.Inject;
import org.envirocar.processing.ec4geomesa.core.decoding.TrackJsonDecoder;
import org.envirocar.processing.ec4geomesa.core.guice.GeoMesaDataStoreModule;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author dewall
 */
public class TrackDecodingTest {

    @Inject
    private TrackJsonDecoder decoder;
    
    @Before
    public void before() {
        Guice.createInjector(new GeoMesaDataStoreModule()).injectMembers(this);
    }
    
    @Test
    public void testTrackDecoding(){
        
    }
}
