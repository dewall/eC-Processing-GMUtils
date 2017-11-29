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
import com.google.common.base.Joiner;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.log4j.Logger;
import org.envirocar.processing.ec4geomesa.core.model.Measurement;
import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.locationtech.geomesa.utils.interop.SimpleFeatureTypes;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 *
 * @author dewall
 */
public class MeasurementFeatureProfile extends AbstractFeatureProfile<Measurement> {

    private static final Logger LOG = Logger.getLogger(
            MeasurementFeatureProfile.class);

    private static final String TABLE_NAME = "measurements";

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
            "Time:Date",
            "*geom:Point:srid=4326",
            "CO2:Double",
            "Calculated MAF:Double",
            "Consumption:Double",
            "Engine Load:Double",
            "Fuel System Loop:Double",
            "Fuel System Status Code:Double",
            "GPS Accuracy:Double",
            "GPS Altitude:Double",
            "GPS Bearing:Double",
            "GPS HDOP:Double",
            "GPS PDOP:Double",
            "GPS Speed:Double",
            "GPS VDOP:Double",
            "Intake Pressure:Double",
            "Intake Temperature:Double",
            "Long-Term Fuel Trim 1:Double",
            "MAF:Double",
            "O2 Lambda Current:Double",
            "O2 Lambda Current ER:Double",
            "O2 Lambda Voltage:Double",
            "O2 Lambda Voltage ER:Double",
            "Rpm:Double",
            "Short-Term Fuel Trim 1:Double",
            "Speed:Double",
            "Throttle Position:Double"
    );

    /**
     * Constructor.
     */
    public MeasurementFeatureProfile() {
        super(TABLE_NAME);
    }

    @Override
    public SimpleFeatureType createSimpleFeatureType() {
        try {
            String spec = Joiner.on(",").join(FEATURE_ATTRIBUTES);
            this.featureType = DataUtilities.createType(TABLE_NAME, spec);
            this.featureType.getUserData().put(
                    SimpleFeatureTypes.DEFAULT_DATE_KEY, "Time");
        } catch (SchemaException ex) {
            LOG.error(ex);
        }

        return featureType;

    }

    @Override
    public SimpleFeature createSimpleFeature(Measurement t) {
        if (!t.isValid()) {
            return null;
        }

        SimpleFeature sf = featureBuilder.buildFeature(t.getId());
        sf.setAttribute("MeasurementID", t.getId());
        sf.setAttribute("TrackID", t.getTrackId());
        sf.setAttribute("Time", t.getTime());
        sf.setDefaultGeometry(t.getPoint());

        // setting phenomenons
        Map<String, Double> phenomenons = t.getPhenomenons();
        for (Entry<String, Double> phenomenon : phenomenons.entrySet()) {
            sf.setAttribute(phenomenon.getKey(), phenomenon.getValue());
        }
        return sf;
    }

}
