package org.envirocar.processing.ec4geomesa.mapmatching;

import com.vividsolutions.jts.geom.Geometry;

/**
 *
 * @author dewall
 */
public interface OSMWayFetcher {

    public Geometry fetchOSMWayGeometry(long osmId);

}
