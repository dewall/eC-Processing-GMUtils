package org.envirocar.processing.ec4geomesa.core.entity;

/**
 *
 * @author dewall
 */
public interface CarSensor {

    public String getManufacturer();

    public void setManufacturer(String manufacturer);

    public String getModel();

    public void setModel(String model);

    public String getFuelType();

    public void setFuelType(String fuelType);

    public int getConstructionYear();

    public void setConstructionYear(int constructionYear);

    public int getEngineDisplacement();

    public void setEngineDisplacement(int engineDisplacement); 
}
