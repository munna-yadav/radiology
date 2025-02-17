package org.openmrs.module.radiology.api.service;

import javax.validation.constraints.NotNull;
import java.util.Optional;

import org.openmrs.module.radiology.api.model.Modality;
import org.openmrs.module.radiology.api.model.OrderLog;
import org.openmrs.module.radiology.api.model.RadiologyOrder;

public interface OrderLogService {
    
    Optional<OrderLog> get(@NotNull int id);

    OrderLog saveOrUpdate(@NotNull OrderLog orderLogs);

    OrderLog save(RadiologyOrder order, String hl7Request, String hl7Response, Modality modality);
}