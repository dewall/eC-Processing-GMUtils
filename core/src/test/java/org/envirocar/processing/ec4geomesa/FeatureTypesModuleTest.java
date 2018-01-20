package org.envirocar.processing.ec4geomesa;

import com.google.inject.Guice;
import com.google.inject.Inject;
import org.envirocar.processing.ec4geomesa.core.guice.DataStoreModule;
import org.envirocar.processing.ec4geomesa.core.guice.FeatureTypesModule;
import org.envirocar.processing.ec4geomesa.core.guice.annotations.MeasurementType;
import org.envirocar.processing.ec4geomesa.core.guice.annotations.TrackType;
import org.envirocar.processing.ec4geomesa.core.feature.schema.MeasurementConstants;
import org.envirocar.processing.ec4geomesa.core.feature.schema.TrackConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 *
 * @author dewall
 */
public class FeatureTypesModuleTest {

    @Inject
    @TrackType
    private SimpleFeatureType trackType;
    
    @Inject
    @MeasurementType
    private SimpleFeatureType measurementType;

    @Before
    public void before() {
        Guice.createInjector(new FeatureTypesModule()).injectMembers(this);
    }
    
    @Test
    public void checkTypes(){
        Assert.assertEquals(TrackConstants.TABLE_NAME, trackType.getTypeName());
        Assert.assertEquals(MeasurementConstants.TABLE_NAME, measurementType.getTypeName());
    }
}
