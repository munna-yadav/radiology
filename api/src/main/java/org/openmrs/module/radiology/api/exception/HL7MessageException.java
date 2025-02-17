package org.openmrs.module.radiology.api.exception;

public class HL7MessageException extends RuntimeException {

    public HL7MessageException(String message) {
        super(message);
    }
}
