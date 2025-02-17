package org.openmrs.module.radiology.api.service;

import org.openmrs.module.radiology.api.model.RadiologyOrder;
import org.openmrs.module.radiology.api.model.RadiologyOrderQueue;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface RadiologyOrderQueueService {


    Optional<RadiologyOrderQueue> findByRadiologyOrderId(@NotNull Integer orderId);

    RadiologyOrderQueue saveOrUpdate(@NotNull RadiologyOrder radiologyOrder);

    RadiologyOrderQueue saveOrUpdate(@NotNull RadiologyOrderQueue radiologyOrder);

    List<RadiologyOrderQueue> findPendingOrFailedOrders();
}
