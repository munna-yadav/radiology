package org.openmrs.module.radiology.api.service;

import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.DataTypeException;
import org.openmrs.module.radiology.api.model.RadiologyOrder;

public interface PacsHL7Service {

    AbstractMessage createMessage(RadiologyOrder order) throws DataTypeException;
}
