package org.openmrs.module.radiology.api.service.impl;

import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v25.group.ORM_O01_PATIENT;
import ca.uhn.hl7v2.model.v25.message.ORM_O01;
import ca.uhn.hl7v2.model.v25.segment.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.ConceptMap;
import org.openmrs.Order;
import org.openmrs.Patient;
import org.openmrs.Provider;
import org.openmrs.api.context.Context;
import org.openmrs.module.radiology.api.constants.Constants;
import org.openmrs.module.radiology.api.exception.HL7MessageException;
import org.openmrs.module.radiology.api.model.RadiologyOrder;
import org.openmrs.module.radiology.api.service.PacsHL7Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PacsHL7ServiceImpl implements PacsHL7Service {
    private static final Log LOG = LogFactory.getLog(PacsHL7ServiceImpl.class);

    @Override
    public AbstractMessage createMessage(RadiologyOrder order) throws DataTypeException {
        LOG.error("Inside createMessage method ");
        if (Constants.ACTION_DISCONTINUE.equals(order.getAction().name())) {
            return createCancelOrderMessage(order);
        } else {
            return createOrderMessage(order);
        }
    }

    private AbstractMessage createOrderMessage(RadiologyOrder order) throws DataTypeException {
        LOG.error("Inside createOrderMessage method ");
        ORM_O01 message = createBaseMessage(order);
        ORC orc = message.getORDER().getORC();
        String orderNumber = order.getOrderNumber();
        validateOrderNumberSize(orderNumber);
        populateOrcFields(orc, order, orderNumber, Constants.NEW_ORDER);
//        populateOrcFields(orc, order, orderNumber, Constants.SCHEDULED);
        addOBRComponent(order, message);
        return message;
    }

    private AbstractMessage createCancelOrderMessage(RadiologyOrder order) throws DataTypeException {
        Order previousOrder = order.getPreviousOrder();
        if (previousOrder == null) {
            throw new HL7MessageException("Unable to Cancel the Order. Previous order is not found: " + order.getOrderNumber());
        }
        ORM_O01 message = createBaseMessage(order);
        ORC orc = message.getORDER().getORC();
        String orderNumber = previousOrder.getOrderNumber();
        validateOrderNumberSize(orderNumber);
        populateOrcFields(orc, order, orderNumber, Constants.CANCEL_ORDER);
        addOBRComponent(order, message);
        return message;
    }

    private ORM_O01 createBaseMessage(RadiologyOrder order) throws DataTypeException {
        ORM_O01 message = new ORM_O01();
        addMessageHeader(order, message);
        addPatientDetails(message, order.getPatient());
        addProviderDetails(order.getOrderer(), message);
        return message;
    }

    private void validateOrderNumberSize(String orderNumber) {
        if (isSizeExceedingLimit(orderNumber)) {
            throw new HL7MessageException("Unable to create HL7 message. Order Number size exceeds limit: " + orderNumber);
        }
    }

    private void populateOrcFields(ORC orc, RadiologyOrder order, String orderNumber, String orderControl) throws DataTypeException {
        orc.getQuantityTiming(0).getPriority().setValue(order.getUrgency().name());
        orc.getPlacerOrderNumber().getEntityIdentifier().setValue(orderNumber);
        orc.getFillerOrderNumber().getEntityIdentifier().setValue(orderNumber);
        orc.getEnteredBy(0).getGivenName().setValue(Constants.SENDER);
        orc.getOrderStatus().setValue("IP");
//        orc.getOrderStatus().setValue("A");
        orc.getOrderingProvider(0).getGivenName().setValue(order.getOrderer().getName());
        orc.getOrderControl().setValue(orderControl);
    }

    private boolean isSizeExceedingLimit(String orderNumber) {
        return orderNumber.getBytes().length > 16;
    }

    private void addMessageHeader(RadiologyOrder order, ORM_O01 message) throws DataTypeException {
        MSH msh = message.getMSH();
        msh.getMessageControlID().setValue(generateMessageControlID(order.getOrderNumber()));
        populateMessageHeader(msh, new Date(), "ORM", "O01", Constants.SENDER);
    }

    private void addOBRComponent(RadiologyOrder order, ORM_O01 message) throws DataTypeException {
        OBR obr = message.getORDER().getORDER_DETAIL().getOBR();

        //Set
//        obr.getSetIDOBR().setValue("1");

        //Set modality
        obr.getDiagnosticServSectID().setValue(order.getModality().getShortNameInLocale(Context.getLocale()).getName());

        // Retrieve the PACS concept source for the radiology order
        ConceptMap pacsConceptSource = order.getConcept().getConceptMappings().stream()
                .filter(map -> Constants.PACS_CONCEPT_SOURCE_NAME.equals(map.getConceptReferenceTerm().getConceptSource().getName()))
                .findFirst()
                .orElseThrow(() -> new HL7MessageException("Unable to create HL7 message. Missing concept source for order: " + order.getUuid()));

        // Set the universal service identifier with the concept code and name
        obr.getUniversalServiceIdentifier().getIdentifier().setValue(pacsConceptSource.getConceptReferenceTerm().getCode());
//        obr.getUniversalServiceIdentifier().getIdentifier().setValue("SHLDR-AP");
        obr.getUniversalServiceIdentifier().getText().setValue(pacsConceptSource.getConceptReferenceTerm().getName());
//        obr.getUniversalServiceIdentifier().getText().setValue("SHOULDER CLAVICL-AP");

        // Set the reason for study (comment intended for the fulfiller)
        if (order.getCommentToFulfiller() != null) {
            obr.getReasonForStudy(0).getText().setValue(order.getCommentToFulfiller());
        }

        // Set the collector's comment to include the concept name of the radiology order
        if (order.getConcept().getName() != null) {
            obr.getCollectorSComment(0).getText().setValue(order.getConcept().getDisplayString());
        }

        obr.getOrderingProvider(0).getGivenName().setValue(order.getOrderer().getName());
    }


    private void addProviderDetails(Provider provider, ORM_O01 message) throws DataTypeException {
        ORC orc = message.getORDER().getORC();

        // Set provider ID (UUID)
        orc.getOrderingProvider(0).getIDNumber().setValue(provider.getUuid());

        // Set provider name
        if (provider.getPerson().getGivenName() != null) {
            orc.getOrderingProvider(0).getGivenName().setValue(provider.getPerson().getGivenName());
        }
        if (provider.getPerson().getFamilyName() != null) {
            orc.getOrderingProvider(0).getFamilyName().getSurname().setValue(provider.getPerson().getFamilyName());
        }
    }

    private void addPatientDetails(ORM_O01 message, Patient patientData) throws DataTypeException {
        ORM_O01_PATIENT patient = message.getPATIENT();
        PID pid = patient.getPID();
        pid.getPatientIdentifierList(0).getIDNumber().setValue(patientData.getPatientIdentifier().getIdentifier());
        pid.getPatientName(0).getGivenName().setValue(patientData.getGivenName());
        pid.getPatientName(0).getFamilyName().getSurname().setValue(patientData.getFamilyName());
        pid.getDateTimeOfBirth().getTime().setValue(new SimpleDateFormat("yyyyMMdd").format(patientData.getBirthdate()));
        pid.getAdministrativeSex().setValue(patientData.getGender());
        message.getORDER().getORDER_DETAIL().getOBR().getPlannedPatientTransportComment(0).getText().setValue(patientData.getGivenName() + "," + patientData.getFamilyName());
    }

    private static DateFormat getHl7DateFormat() {
        return new SimpleDateFormat("yyyyMMddHH");
    }

//    private PV1 populatePatientVisit() {
//        // Populate PV1 (Patient Visit) segment
//        PV1 pv1 = message.getPATIENT().getVISIT().getPV1();
//        pv1.getPatientClass().setValue("O");
//        pv1.getAssignedPatientLocation().getPointOfCare().setValue("RADIOLOGY");
//    }

    private MSH populateMessageHeader(MSH msh, Date dateTime, String messageType, String triggerEvent, String sendingFacility) throws DataTypeException {
        msh.getFieldSeparator().setValue("|");
        msh.getEncodingCharacters().setValue("^~\\&");
//        msh.getSendingFacility().getHd1_NamespaceID().setValue(sendingFacility);
//        msh.getSendingFacility().getUniversalID().setValue(sendingFacility);
//        msh.getSendingFacility().getNamespaceID().setValue(sendingFacility);
        msh.getSendingApplication().getNamespaceID().setValue("OpenMRS"); // Set the Sending Application
        msh.getSendingFacility().getNamespaceID().setValue("Hospital"); // Set the Sending Facility
        msh.getReceivingApplication().getNamespaceID().setValue("PACS"); // Set the Receiving Application
        msh.getReceivingFacility().getNamespaceID().setValue("IMAGING"); // Set the Receiving Facility
        msh.getDateTimeOfMessage().getTs1_Time().setValue(getHl7DateFormat().format(dateTime));
        msh.getMessageType().getMessageCode().setValue(messageType);
        msh.getMessageType().getTriggerEvent().setValue(triggerEvent);
        msh.getProcessingID().getProcessingID().setValue("P");
        msh.getVersionID().getVersionID().setValue("2.5");
        return msh;
    }

    String generateMessageControlID(String orderNumber) {
        int endAt = Math.min(orderNumber.length(), 9);
        return (new Date().getTime() + orderNumber.substring(4, endAt));
    }
}
