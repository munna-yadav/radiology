package org.openmrs.module.radiology.api.service.impl;

import ca.uhn.hl7v2.AcknowledgmentCode;
import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v25.message.ACK;
import ca.uhn.hl7v2.model.v25.message.ORR_O02;
import ca.uhn.hl7v2.parser.PipeParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.radiology.api.exception.ModalityException;
import org.openmrs.module.radiology.api.model.Modality;
import org.openmrs.module.radiology.api.model.RadiologyOrder;
import org.openmrs.module.radiology.api.service.ModalityService;
import org.openmrs.module.radiology.api.service.OrderLogService;
import org.openmrs.module.radiology.api.service.PacsHL7Service;
import org.openmrs.module.radiology.api.service.PacsIntegrationService;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;

@Transactional
public class PacsIntegrationServiceImpl implements PacsIntegrationService {
    private static final Log LOG = LogFactory.getLog(PacsIntegrationServiceImpl.class);

    private ModalityService modalityService;
    private PacsHL7Service pacsHL7Service;
    private OrderLogService orderLogService;

    public void setModalityService(ModalityService modalityService) {
        this.modalityService = modalityService;
    }

    public void setPacsHL7Service(PacsHL7Service pacsHL7Service) {
        this.pacsHL7Service = pacsHL7Service;
    }

    public void setOrderLogService(OrderLogService orderLogService) {
        this.orderLogService = orderLogService;
    }

    public String sendMessage(AbstractMessage message, Modality modality) throws HL7Exception, LLPException, IOException {
        LOG.error("Inside sendMessage method "+ message);
        Message response = post(modality, message);
        String responseMessage = parseResponse(response);
        if (response instanceof ORR_O02) {
            ORR_O02 acknowledgement = (ORR_O02) response;
            String acknowledgmentCode = acknowledgement.getMSA().getAcknowledgmentCode().getValue();
            processAcknowledgement(modality, responseMessage, acknowledgmentCode);
        } else if (response instanceof ACK) {
            ACK acknowledgement = (ACK) response;
            String acknowledgmentCode = acknowledgement.getMSA().getAcknowledgmentCode().getValue();
            processAcknowledgement(modality, responseMessage, acknowledgmentCode);
        } else {
            throw new ModalityException(responseMessage, modality);
        }
        return responseMessage;
    }

    Message post(Modality modality, Message requestMessage) throws LLPException, IOException, HL7Exception {
        Connection newClientConnection = null;
        LOG.error("\n\n\n");
        // Create the HAPI parser
        PipeParser parser = new PipeParser();

        // Encode the message with SOH
        String encodedMessage = parser.encode(requestMessage);
        LOG.error("Sending HL7 Message: " + encodedMessage);
        try {
            HapiContext hapiContext = new DefaultHapiContext();
            newClientConnection = hapiContext.newClient(modality.getIp(), modality.getPort(), false);

            Initiator initiator = newClientConnection.getInitiator();
            Message responseMessage = initiator.sendAndReceive(requestMessage);
            LOG.error("Received HL7 Response: " + responseMessage.encode());

            return responseMessage;
        } catch (LLPException | IOException | HL7Exception e) {
            // Handle known exceptions
            LOG.error("Error while sending HL7 message: " + e.getMessage(), e);
            throw e;
        } finally {
            if (newClientConnection != null) {
                newClientConnection.close();
            }
        }
    }

    String parseResponse(Message response) throws HL7Exception {
        return new PipeParser().encode(response);
    }

    private void processAcknowledgement(Modality modality, String responseMessage, String acknowledgmentCode) {
        LOG.error("Inside processAcknowledgement method::: "+ responseMessage + " "+acknowledgmentCode);
        if (!AcknowledgmentCode.AA.toString().equals(acknowledgmentCode)) {
            throw new ModalityException(responseMessage, modality);
        }
    }

    @Override
    public void processOrder(RadiologyOrder radiologyOrder) throws HL7Exception, LLPException, IOException {
        LOG.error("Inside processOrder method ");
        Optional<Modality> modalityRecord = modalityService.getByOrderTypeId(radiologyOrder.getOrderType().getOrderTypeId());
        if (!modalityRecord.isPresent()) {
//            throw new ModalityException("No modality record found.", null);
            return;
        }
        AbstractMessage request = pacsHL7Service.createMessage(radiologyOrder);
        String response = sendMessage(request, modalityRecord.get());

        orderLogService.save(radiologyOrder, request.encode(), response, modalityRecord.get());
    }
}
