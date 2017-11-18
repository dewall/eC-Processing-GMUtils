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
import org.geotools.feature.SchemaException;
import org.locationtech.geomesa.utils.interop.SimpleFeatureTypes;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 *
 * @author dewall
 */
public class TrackFeatureProfile extends AbstractFeatureProfile {

    private static final Logger LOG = Logger.
            getLogger(TrackFeatureProfile.class);

    private static final String TABLE_NAME = "tracks";
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

    public TrackFeatureProfile() {
        super(TABLE_NAME);
    }

    @Override
    public SimpleFeatureType getSimpleFeatureType() {
        try {
            SimpleFeatureType featureType = createSimpleFeatureType(
                    featureAttributes);
            featureType.getUserData().put(SimpleFeatureTypes.DEFAULT_DATE_KEY,
                    featureAttributes.get(1));
            return featureType;
        } catch (SchemaException ex) {
            LOG.error("Error while creating TrackFeature", ex);
        }
        return null;
    }

}
