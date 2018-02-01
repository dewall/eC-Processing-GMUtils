package org.envirocar.processing.ec4geomesa;

import com.google.inject.Guice;
import com.google.inject.Inject;
import org.envirocar.processing.ec4geomesa.core.guice.DataStoreModule;
import org.envirocar.processing.ec4geomesa.core.guice.FeatureTypesModule;
import org.envirocar.processing.ec4geomesa.core.guice.annotations.MeasurementType;
import org.envirocar.processing.ec4geomesa.core.guice.annotations.TrackType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;
import org.envirocar.processing.ec4geomesa.core.feature.schema.TrackSchema;
import org.envirocar.processing.ec4geomesa.core.feature.schema.MeasurementSchema;

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
        Assert.assertEquals(TrackSchema.TABLE_NAME, trackType.getTypeName());
        Assert.assertEquals(MeasurementSchema.TABLE_NAME, measurementType.getTypeName());
    }
}
