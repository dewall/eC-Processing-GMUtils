package org.envirocar.processing.ec4geomesa.core.exception;

/**
 *
 * @author dewall
 */
public class InvalidMeasurementNumberException extends Exception {

    public InvalidMeasurementNumberException(String message) {
        super(message);
    }

    /**
     *
     * @param message
     * @param throwable
     */
    public InvalidMeasurementNumberException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
