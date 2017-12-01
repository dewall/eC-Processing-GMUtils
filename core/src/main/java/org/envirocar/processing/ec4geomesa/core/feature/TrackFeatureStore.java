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

import com.google.common.collect.Lists;
import java.util.List;
import org.apache.log4j.Logger;
import org.envirocar.processing.ec4geomesa.core.model.CarSensor;
import org.envirocar.processing.ec4geomesa.core.model.Track;
import org.geotools.data.DataStore;
import org.geotools.feature.SchemaException;
import org.locationtech.geomesa.utils.interop.SimpleFeatureTypes;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 *
 * @author dewall
 */
public class TrackFeatureStore extends AbstractFeatureStore<Track> {

    private static final Logger LOG = Logger.
            getLogger(TrackFeatureStore.class);

    private static final String TABLE_NAME = "tracks";

    private static final String ATTRIB_TRACKID = "TrackID";
    private static final String ATTRIB_STARTTIME = "StartTime";
    private static final String ATTRIB_ENDTIME = "EndTime";
    private static final String ATTRIB_LENGTH = "Length";
    private static final String ATTRIB_MANUFACTURER = "CarManufacturer";
    private static final String ATTRIB_MODEL = "CarModel";
    private static final String ATTRIB_FUELTYPE = "CarFuelType";
    private static final String ATTRIB_CONSTRUCTIONYEAR = "CarConstructionYear";
    private static final String ATTRIB_ENGINEDISPLACEMENT = "CarEngineDisplacement";

    private static final List<String> featureAttributes = Lists.newArrayList(
            "TrackID:String",
            "StartTime:Date",
            "EndTime:Date",
            "Length:Float",
            "CarManufacturer:String",
            "CarModel:String",
            "CarFuelType:String",
            "CarConstructionYear:Integer",
            "CarEngineDisplacement:Integer",
            "*geom:LineString:srid=4326"
    );

    /**
     * Constructor.
     */
    public TrackFeatureStore() {
        super(TABLE_NAME);
    }

    @Override
    public SimpleFeatureType createSimpleFeatureType() {
        try {
            SimpleFeatureType featureType = createSimpleFeatureType(featureAttributes);
            featureType.getUserData().put(SimpleFeatureTypes.DEFAULT_DATE_KEY, "StartTime");
            return featureType;
        } catch (SchemaException ex) {
            LOG.error("Error while creating TrackFeature", ex);
        }
        return null;
    }

    @Override
    public SimpleFeature createSimpleFeature(Track t) {
        if (!t.isValid()) {
            return null;
        }

        SimpleFeature sf = featureBuilder.buildFeature(t.getId());
        sf.setDefaultGeometry(t.getLineString());
        sf.setAttribute(ATTRIB_TRACKID, t.getId());
        sf.setAttribute(ATTRIB_STARTTIME, t.getStartingTime());
        sf.setAttribute(ATTRIB_ENDTIME, t.getEndingTime());
        sf.setAttribute(ATTRIB_LENGTH, t.getLength());

        CarSensor s = t.getCarSensor();
        sf.setAttribute(ATTRIB_MANUFACTURER, s.getManufacturer());
        sf.setAttribute(ATTRIB_MODEL, s.getModel());
        sf.setAttribute(ATTRIB_FUELTYPE, s.getFuelType());
        sf.setAttribute(ATTRIB_CONSTRUCTIONYEAR, s.getConstructionYear());
        sf.setAttribute(ATTRIB_ENGINEDISPLACEMENT, s.getEngineDisplacement());

        return sf;
    }

    @Override
    public Track getById(DataStore ds, String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}