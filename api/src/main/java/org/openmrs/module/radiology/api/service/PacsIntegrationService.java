package org.openmrs.module.radiology.api.service;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.llp.LLPException;
import org.openmrs.module.radiology.api.model.RadiologyOrder;

import java.io.IOException;

public interface PacsIntegrationService {

    void processOrder(RadiologyOrder radiologyOrder) throws HL7Exception, LLPException, IOException;
} 