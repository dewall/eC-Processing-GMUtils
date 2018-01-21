package org.envirocar.processing.ec4geomesa.core.feature.wrapper;

import org.envirocar.processing.ec4geomesa.core.entity.CarSensor;
import org.opengis.feature.simple.SimpleFeature;
import org.envirocar.processing.ec4geomesa.core.feature.schema.TrackConstants;

/**
 *
 * @author dewall
 */
public class CarSensorWrapper extends AbstractFeatureWrapper implements CarSensor {

    /**
     * Constructor.
     *
     * @param feature
     */
    public CarSensorWrapper(SimpleFeature feature) {
        super(feature);
    }

    @Override
    public String getManufacturer() {
        return (String) this.feature.getAttribute(TrackConstants.ATTRIB_CAR_MANUFACTURER);
    }

    @Override
    public void setManufacturer(String manufacturer) {
        this.feature.setAttribute(TrackConstants.ATTRIB_CAR_MANUFACTURER, manufacturer);
    }

    @Override
    public String getModel() {
        return (String) this.feature.getAttribute(TrackConstants.ATTRIB_CAR_MODEL);
    }

    @Override
    public void setModel(String model) {
        this.feature.setAttribute(TrackConstants.ATTRIB_CAR_MODEL, model);
    }

    @Override
    public String getFuelType() {
        return (String) this.feature.getAttribute(TrackConstants.ATTRIB_CAR_FUELTYPE);
    }

    @Override
    public void setFuelType(String fuelType) {
        this.feature.setAttribute(TrackConstants.ATTRIB_CAR_FUELTYPE, fuelType);
    }

    @Override
    public int getConstructionYear() {
        return (int) this.feature.getAttribute(TrackConstants.ATTRIB_CAR_CONSTRUCTIONYEAR);
    }

    @Override
    public void setConstructionYear(int constructionYear) {
        this.feature.setAttribute(TrackConstants.ATTRIB_CAR_CONSTRUCTIONYEAR, constructionYear);
    }

    @Override
    public int getEngineDisplacement() {
        return (int) this.feature.getAttribute(TrackConstants.ATTRIB_CAR_ENGINEDISPLACEMENT);
    }

    @Override
    public void setEngineDisplacement(int engineDisplacement) {
        this.feature.setAttribute(TrackConstants.ATTRIB_CAR_ENGINEDISPLACEMENT, engineDisplacement);
    }

}
