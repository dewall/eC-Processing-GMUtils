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
import java.util.List;
import org.apache.log4j.Logger;
import org.geotools.feature.SchemaException;
import org.locationtech.geomesa.utils.interop.SimpleFeatureTypes;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 *
 * @author dewall
 */
public class MeasurementFeatureProfile extends AbstractFeatureProfile {

    private static final Logger LOG = Logger.getLogger(
            MeasurementFeatureProfile.class);

    private static final String TABLE_NAME = "measurements";
    private static final List<String> FEATURE_ATTRIBUTES = Lists.newArrayList(
            "MeasurementID:String",
            "TrackID:String",
            "Time:Date",
            "Speed:Integer",
            "*geom:Point:srid=4326"
    );

    /**
     * Constructor.
     */
    public MeasurementFeatureProfile() {
        super(TABLE_NAME);
    }

    @Override
    public SimpleFeatureType getSimpleFeatureType() {
        try {
            SimpleFeatureType featureType = createSimpleFeatureType(
                    FEATURE_ATTRIBUTES);
            featureType.getUserData().put(SimpleFeatureTypes.DEFAULT_DATE_KEY,
                    FEATURE_ATTRIBUTES.get(2));
            return featureType;
        } catch (SchemaException ex) {
            LOG.error("Error while creating TrackFeature", ex);
        }
        return null;
    }

}
