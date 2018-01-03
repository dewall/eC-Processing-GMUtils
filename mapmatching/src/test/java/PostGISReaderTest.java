
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.vividsolutions.jts.geom.Geometry;
import org.envirocar.processing.ec4geomesa.mapmatching.MapMatcherModule;
import org.envirocar.processing.ec4geomesa.mapmatching.OSMPostGISReader;
import org.junit.Assert;
import org.junit.Before;

/**
 *
 * @author dewall
 */
public class PostGISReaderTest {

    @Inject
    private OSMPostGISReader reader;

    @Before
    public void before() {
        Injector injector = Guice.createInjector(new MapMatcherModule());
        injector.injectMembers(this);

    }

//    @Test
    public void testReader() {
        Geometry way = reader.fetchOSMWayGeometry(10275099L);
        Assert.assertTrue(way.getLength() > 0.0);
    }

}
