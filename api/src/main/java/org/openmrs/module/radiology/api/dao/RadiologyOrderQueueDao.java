package org.openmrs.module.radiology.api.dao;

import org.openmrs.module.radiology.api.model.RadiologyOrderQueue;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface RadiologyOrderQueueDao {

    List<RadiologyOrderQueue> findPendingOrFailedOrders();

    RadiologyOrderQueue saveOrUpdate(RadiologyOrderQueue queue);

    Optional<RadiologyOrderQueue> findByRadiologyOrderId(@NotNull Integer orderId);
}
