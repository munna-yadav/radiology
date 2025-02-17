package org.openmrs.module.radiology.api.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Order;
import org.openmrs.module.radiology.api.dao.RadiologyOrderQueueDao;
import org.openmrs.module.radiology.api.enums.RadiologyOrderStatus;
import org.openmrs.module.radiology.api.model.RadiologyOrder;
import org.openmrs.module.radiology.api.model.RadiologyOrderQueue;
import org.openmrs.module.radiology.api.service.RadiologyOrderQueueService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public class RadiologyOrderQueueServiceImpl implements RadiologyOrderQueueService {
    private static final Log LOG = LogFactory.getLog(RadiologyOrderQueueServiceImpl.class);

    private RadiologyOrderQueueDao radiologyOrderQueueDao;

    public void setRadiologyOrderQueueDao(RadiologyOrderQueueDao radiologyOrderQueueDao) {
        this.radiologyOrderQueueDao = radiologyOrderQueueDao;
    }

    @Override
    public Optional<RadiologyOrderQueue> findByRadiologyOrderId(Integer orderId) {
        LOG.info("Inside RadiologyOrderQueueServiceImpl > getRadiologyOrderByUuid method");
        return radiologyOrderQueueDao.findByRadiologyOrderId(orderId);
    }

    @Override
    public RadiologyOrderQueue saveOrUpdate(RadiologyOrder radiologyOrder) {
        LOG.info("Inside RadiologyOrderQueueServiceImpl > saveOrUpdate method");
        RadiologyOrderQueue queue = new RadiologyOrderQueue();
        queue.setRadiologyOrderId(radiologyOrder);
        queue.setStatus(RadiologyOrderStatus.PENDING);
        queue.setUrgency(radiologyOrder.getUrgency() == null ? Order.Urgency.ROUTINE : radiologyOrder.getUrgency());
        return radiologyOrderQueueDao.saveOrUpdate(queue);
    }

    @Override
    public RadiologyOrderQueue saveOrUpdate(RadiologyOrderQueue queueOrder) {
        LOG.info("Inside RadiologyOrderQueueServiceImpl > saveOrUpdate method");
        return radiologyOrderQueueDao.saveOrUpdate(queueOrder);
    }

    @Override
    public List<RadiologyOrderQueue> findPendingOrFailedOrders() {
        LOG.info("Inside RadiologyOrderQueueServiceImpl > findPendingOrFailedOrders method");
        return radiologyOrderQueueDao.findPendingOrFailedOrders();
    }
}
