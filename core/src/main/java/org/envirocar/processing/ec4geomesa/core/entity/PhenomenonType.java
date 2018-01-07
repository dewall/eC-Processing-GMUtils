package org.envirocar.processing.ec4geomesa.core.entity;

/**
 *
 * @author dewall
 */
public enum PhenomenonType {
    CO2("CO2"),
    CALCULATED_MAF("Calculated MAF"),
    CONSUMPTION("Consumption"),
    ENGINE_LOAD("Engine Load"),
    FUEL_SYSTEM_LOOP("Fuel System Loop"),
    FUEL_SYSTEM_STATUS_CODE("Fuel System Status Code"),
    GPS_ACCURACY("GPS Accuracy"),
    GPS_ALTITUDE("GPS Altitude"),
    GPS_BEARING("GPS Bearing"),
    GPS_HDOP("GPS HDOP"),
    GPS_PDOP("GPS PDOP"),
    GPS_VDOP("GPS VDOP"),
    GPS_SPEED("GPS SPEED"),
    INTAKE_PRESSURE("Intake Pressure"),
    INTAKE_TEMPERATURE("Intake Temperature"),
    LONG_TERM_FUEL_TRIM_1("Long-Term Fuel Trim 1"),
    MAF("MAF"),
    O2_LAMBDA_CURRENT("O2 Lambda Current"),
    O2_LAMBDA_CURRENT_ER("O2 Lambda Current ER"),
    O2_LAMBDA_VOLTAGE("O2 Lambda Voltage"),
    O2_LAMBDA_VOLTAGE_ER("O2 Lambda Voltage ER"),
    RPM("Rpm"),
    SHORT_TERM_FUEL_TRIM_1("Short-Term Fuel Trim 1"),
    SPEED("Speed"),
    THROTTLE_POSITION("Throttle Position");

    private final String readableName;

    PhenomenonType(String readableName) {
        this.readableName = readableName;
    }

    public String getReadableName() {
        return this.readableName;
    }

    @Override
    public String toString() {
        return readableName;
    }

    public static PhenomenonType getByReadableName(String toFind) {
        for (PhenomenonType p : values()) {
            if (p.toString().equalsIgnoreCase(toFind)) {
                return p;
            }
        }
        throw new IllegalArgumentException();
    }

}
