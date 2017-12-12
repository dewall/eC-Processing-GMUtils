/*
 * Copyright (C) 2017 the enviroCar community
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.envirocar.processing.ec4geomesa.core.feature;

import com.beust.jcommander.internal.Lists;
import com.google.inject.Inject;
import com.vividsolutions.jts.geom.Point;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.envirocar.processing.ec4geomesa.core.model.Measurement;
import org.envirocar.processing.ec4geomesa.core.model.Track;
import org.geotools.data.DataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.opengis.feature.simple.SimpleFeature;

/**
 *
 * @author dewall
 */
public class MeasurementFeatureStore extends AbstractFeatureStore<Measurement> {

    private static final Logger LOG = Logger.getLogger(MeasurementFeatureStore.class);

    private static final String TABLE_NAME = "measurements";
    private static final String ATTRIB_MID = "MeasurementID";
    private static final String ATTRIB_TRACKID = "TrackID";
    private static final String ATTRIB_TIME = "Time";

    protected static final List<String> PHENOMENONS = Arrays.asList(
            "CO2",
            "Calculated MAF",
            "Consumption",
            "Engine Load",
            "Fuel System Loop",
            "Fuel System Status Code",
            "GPS Accuracy",
            "GPS Altitude",
            "GPS Bearing",
            "GPS HDOP",
            "GPS PDOP",
            "GPS Speed",
            "GPS VDOP",
            "Intake Pressure",
            "Intake Temperature",
            "Long-Term Fuel Trim 1",
            "MAF",
            "O2 Lambda Current",
            "O2 Lambda Current ER",
            "O2 Lambda Voltage",
            "O2 Lambda Voltage ER",
            "Rpm",
            "Short-Term Fuel Trim 1",
            "Speed",
            "Throttle Position");

    private static final List<String> FEATURE_ATTRIBUTES = Lists.newArrayList(
            "MeasurementID:String",
            "TrackID:String",
            "Time:Date"
    );

    static {
        PHENOMENONS.forEach(p -> {
            FEATURE_ATTRIBUTES.add(p + ":Double");
        });
        FEATURE_ATTRIBUTES.add("*geom:Point:srid=4326");
    }

    /**
     * Constructor.
     *
     * @param datastore
     */
    @Inject
    public MeasurementFeatureStore(DataStore datastore) {
        super(datastore, TABLE_NAME, ATTRIB_MID, ATTRIB_TIME, FEATURE_ATTRIBUTES);
    }

    @Override
    public SimpleFeature createFeatureFromEntity(Measurement t) {
        if (!t.isValid()) {
            return null;
        }

        SimpleFeature sf = featureBuilder.buildFeature(t.getId());
        sf.setAttribute(ATTRIB_MID, t.getId());
        sf.setAttribute(ATTRIB_TRACKID, t.getTrackId());
        sf.setAttribute(ATTRIB_TIME, t.getTime());
        sf.setDefaultGeometry(t.getPoint());

        // setting phenomenons
        Map<String, Double> phenomenons = t.getPhenomenons();
        phenomenons.entrySet().
                forEach((phenomenon) -> {
                    sf.setAttribute(phenomenon.getKey(), phenomenon.getValue());
                });
        return sf;
    }

    @Override
    protected Measurement createEntityFromFeature(SimpleFeature sf) {
        if (sf == null) {
            return null;
        }

        String mID = (String) sf.getAttribute(ATTRIB_MID);
        String tID = (String) sf.getAttribute(ATTRIB_TRACKID);
        Date time = (Date) sf.getAttribute(ATTRIB_TIME);
        Point point = (Point) sf.getDefaultGeometry();
        Measurement m = new Measurement(mID, tID, point, time);

        Map<String, Double> phenomenons = new HashMap<>();
        PHENOMENONS.forEach(p -> {
            Object attr = sf.getAttribute(p);
            if (attr != null) {
                double attribute = (double) attr;
                phenomenons.put(p, attribute);
            }

        });
        m.setPhenomenons(phenomenons);

        return m;
    }

    public Track fetchTrack(Track track) {
        try {
            SimpleFeatureCollection sfc = fetch(CQL.toFilter("TrackID = '" + track.getId() + "'"));
            List<Measurement> measurements = createEntitiesFromFeatures(sfc);
            track.setMeasurements(measurements);
        } catch (CQLException ex) {
            LOG.error("Error creating Filter", ex);
        } catch (IOException ex) {
            LOG.error(String.format("Error while getting measurements for track=%s", track.getId()), ex);
        }
        return track;
    }

}
