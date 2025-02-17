package org.openmrs.module.radiology.api.exception;

import org.openmrs.module.radiology.api.model.Modality;

public class ModalityException extends RuntimeException {

    private String responseMessage;
    private Modality modality;

    public ModalityException(String responseMessage, Modality modality) {
        super();
        this.responseMessage = responseMessage;
        this.modality = modality;
    }

    public String getMessage() {
        if (this.modality == null) {
            return "Unable to send the message to the modality \n" + responseMessage;
        } else {
            return "Unable to send the message to the modality \n" + modality.toString() + "\n" + responseMessage;
        }
    }
}
