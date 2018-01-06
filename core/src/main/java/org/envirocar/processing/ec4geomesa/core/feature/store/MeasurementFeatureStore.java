package org.envirocar.processing.ec4geomesa.core.feature.store;

import java.util.Map;
import org.envirocar.processing.ec4geomesa.core.feature.schema.MeasurementSchema;
import org.envirocar.processing.ec4geomesa.core.model.Measurement;
import org.opengis.feature.simple.SimpleFeature;

/**
 *
 * @author dewall
 */
public class MeasurementFeatureStore extends AbstractFeatureStore<Measurement> implements MeasurementStore, MeasurementSchema {

    @Override
    public Measurement featureToEntity(SimpleFeature feature) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SimpleFeature entityToFeature(Measurement m) {
        if (!t.isValid()) {
            return null;
        }

        SimpleFeature sf = featureBuilder.buildFeature(m.getId());
        sf.setAttribute(ATTRIB_MEASUREMENT_ID, m.getId());
        sf.setAttribute(ATTRIB_TRACK_ID, m.getTrackId());
        sf.setAttribute(ATTRIB_TIME, m.getTime());
        sf.setDefaultGeometry(m.getPoint());

        // setting phenomenons
        Map<String, Double> phenomenons = m.getPhenomenons();
        phenomenons.entrySet().
                forEach((phenomenon) -> {
                    sf.setAttribute(phenomenon.getKey(), phenomenon.getValue());
                });
        return sf;
    }

    @Override
    public Measurement create(Measurement track) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Measurement save(Measurement track) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(Measurement track) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Measurement track) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Measurement getByID(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
