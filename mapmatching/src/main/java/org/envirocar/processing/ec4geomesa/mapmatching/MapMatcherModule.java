package org.envirocar.processing.ec4geomesa.mapmatching;

import com.bmwcarit.barefoot.matcher.Matcher;
import com.bmwcarit.barefoot.road.PostGISReader;
import com.bmwcarit.barefoot.roadmap.Loader;
import com.bmwcarit.barefoot.roadmap.Road;
import com.bmwcarit.barefoot.roadmap.RoadMap;
import com.bmwcarit.barefoot.roadmap.RoadPoint;
import com.bmwcarit.barefoot.roadmap.TimePriority;
import com.bmwcarit.barefoot.spatial.Geography;
import com.bmwcarit.barefoot.topology.Dijkstra;
import com.bmwcarit.barefoot.util.Tuple;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.envirocar.processing.ec4geomesa.core.PropertiesUtils;
import org.json.JSONException;

/**
 *
 * @author dewall
 */
public class MapMatcherModule extends AbstractModule implements BarefootConfig {

    private static final Logger LOGGER = Logger.getLogger(MapMatcherModule.class);
    private static final String PROPERTIES_FILE = "/barefoot.properties";

    private final Map<String, String> barefootConfig;

    /**
     * Constructor.
     */
    public MapMatcherModule() {
        this(new HashMap<>());
    }

    /**
     * Constructor.
     *
     * @param barefootConfig
     */
    public MapMatcherModule(Map<String, String> barefootConfig) {
        this.barefootConfig = barefootConfig;
    }

    @Override
    protected void configure() {
        if (this.barefootConfig.isEmpty()) {
            try {
                Properties p = PropertiesUtils.getProperties(PROPERTIES_FILE);

                // PostGIS Properties
                if (p.containsKey(PROPERTY_PG_HOST)) {
                    barefootConfig.put(PROPERTY_PG_HOST, p.getProperty(PROPERTY_PG_HOST));
                }
                if (p.containsKey(PROPERTY_PG_PORT)) {
                    barefootConfig.put(PROPERTY_PG_PORT, p.getProperty(PROPERTY_PG_PORT));
                }
                if (p.containsKey(PROPERTY_PG_DATABASE)) {
                    barefootConfig.put(PROPERTY_PG_DATABASE, p.getProperty(PROPERTY_PG_DATABASE));
                }
                if (p.containsKey(PROPERTY_PG_TABLE)) {
                    barefootConfig.put(PROPERTY_PG_TABLE, p.getProperty(PROPERTY_PG_TABLE));
                }
                if (p.containsKey(PROPERTY_PG_USER)) {
                    barefootConfig.put(PROPERTY_PG_USER, p.getProperty(PROPERTY_PG_USER));
                }
                if (p.containsKey(PROPERTY_PG_PW)) {
                    barefootConfig.put(PROPERTY_PG_PW, p.getProperty(PROPERTY_PG_PW));
                }

                // Matcher Parameters
                if (p.containsKey(PROPERTY_MM_SIGMA)) {
                    barefootConfig.put(PROPERTY_MM_SIGMA, p.getProperty(PROPERTY_MM_SIGMA));
                }
                if (p.containsKey(PROPERTY_MM_LAMBDA)) {
                    barefootConfig.put(PROPERTY_MM_LAMBDA, p.getProperty(PROPERTY_MM_SIGMA));
                }
                if (p.containsKey(PROPERTY_MM_MAXDISTANCE)) {
                    barefootConfig.put(PROPERTY_MM_MAXDISTANCE, p.getProperty(PROPERTY_MM_SIGMA));
                }
                if (p.containsKey(PROPERTY_MM_MAXRADIUS)) {
                    barefootConfig.put(PROPERTY_MM_MAXRADIUS, p.getProperty(PROPERTY_MM_SIGMA));
                }

            } catch (IOException ex) {
                LOGGER.error("Error while reading geomesa.properties.", ex);
            }
        }
    }

    @Provides
    @Singleton
    public PostGISReader providePostGISReader() throws JSONException, IOException {
        Map<Short, Tuple<Double, Integer>> read = Loader.read(
                "src/main/resources/road-types.json");
        return new PostGISReader(
                barefootConfig.get(PROPERTY_PG_HOST),
                Integer.parseInt(barefootConfig.get(PROPERTY_PG_PORT)),
                barefootConfig.get(PROPERTY_PG_DATABASE),
                barefootConfig.get(PROPERTY_PG_TABLE),
                barefootConfig.get(PROPERTY_PG_USER),
                barefootConfig.get(PROPERTY_PG_PW),
                read);
    }

    @Provides
    @Singleton
    public RoadMap provideRoadMap(PostGISReader reader) {
        return RoadMap.Load(reader).construct();
    }

    @Provides
    @Singleton
    public Matcher provideMapMatcher(RoadMap roadMap) {
        Matcher matcher = new Matcher(roadMap,
                new Dijkstra<>(),
                new TimePriority(),
                new Geography());

        if (barefootConfig.containsKey(PROPERTY_MM_SIGMA)) {
            matcher.setSigma(Double.parseDouble(barefootConfig.get(PROPERTY_MM_SIGMA)));
        }
        if (barefootConfig.containsKey(PROPERTY_MM_LAMBDA)) {
            matcher.setLambda(Double.parseDouble(barefootConfig.get(PROPERTY_MM_LAMBDA)));
        }
        if (barefootConfig.containsKey(PROPERTY_MM_MAXDISTANCE)) {
            matcher.setMaxDistance(Double.parseDouble(barefootConfig.get(PROPERTY_MM_MAXDISTANCE)));
        }
        if (barefootConfig.containsKey(PROPERTY_MM_MAXRADIUS)) {
            matcher.setMaxRadius(Double.parseDouble(barefootConfig.get(PROPERTY_MM_MAXRADIUS)));
        }

        return matcher;
    }

}
