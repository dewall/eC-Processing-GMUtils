package org.envirocar.processing.ec4geomesa.mapmatching;

import com.bmwcarit.barefoot.road.PostGISReader;
import com.bmwcarit.barefoot.util.Tuple;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;
import com.vividsolutions.jts.operation.linemerge.LineMerger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author dewall
 */
public class OSMPostGISReader extends PostGISReader implements OSMWayFetcher {

    private static final Logger LOGGER = Logger.getLogger(OSMPostGISReader.class);
    private static final String KEY_GEOM = "geom";

    private final WKBReader wkbReader;

    /**
     * Constructs {@link PostGISReader} object.
     *
     * @param host Hostname of the database server.
     * @param port Port number of the database server.
     * @param database Name of the database.
     * @param table Name of the table.
     * @param user User for accessing the database.
     * @param password Password of the user.
     * @param config Road type configuration.
     * @param wkbReader Reader for well-known binary.
     */
    public OSMPostGISReader(String host, int port, String database, String table, String user, String password,
            Map<Short, Tuple<Double, Integer>> config, WKBReader wkbReader) {
        super(host, port, database, table, user, password, config);
        this.wkbReader = wkbReader;
    }

    @Override
    public Geometry fetchOSMWayGeometry(long osmId) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT osm_id, ST_AsBinary(geom) as geom FROM bfmap_ways WHERE osm_id=");
        builder.append(osmId);
        String query = builder.toString();

        Geometry fetchedLineString = null;
        try {
            this.open();

            ResultSet resultSet = execute(query);

            LineMerger merger = new LineMerger();
            while (resultSet.next()) {
                byte[] wkb = resultSet.getBytes(KEY_GEOM);
                Geometry geom = wkbReader.read(wkb);
                merger.add(geom);
            }
            Collection<Geometry> mergedLineStrings = merger.getMergedLineStrings();
            fetchedLineString = mergedLineStrings.iterator().next();
        } catch (SQLException ex) {
            LOGGER.error("Error while fetching geometry.", ex);
        } catch (ParseException ex) {
            LOGGER.error("Error while parsing wkb for fetched geometry", ex);
        } finally {
            if (isOpen()) {
                this.close();
            }
        }

        return fetchedLineString;
    }

}
