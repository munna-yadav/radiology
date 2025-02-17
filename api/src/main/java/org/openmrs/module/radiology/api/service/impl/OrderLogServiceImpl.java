package org.openmrs.module.radiology.api.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.radiology.api.dao.OrderLogDao;
import org.openmrs.module.radiology.api.model.Modality;
import org.openmrs.module.radiology.api.model.OrderLog;
import org.openmrs.module.radiology.api.model.RadiologyOrder;
import org.openmrs.module.radiology.api.service.OrderLogService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public class OrderLogServiceImpl implements OrderLogService {
    private static final Log LOG = LogFactory.getLog(OrderLogServiceImpl.class);

    private OrderLogDao orderLogDao;

    public void setOrderLogDao(OrderLogDao orderLogDao) {
        this.orderLogDao = orderLogDao;
    }

    @Override
    public Optional<OrderLog> get(int id) {
        LOG.info("Inside get OrderLogs");
        return orderLogDao.get(id);
    }

    @Override
    public OrderLog saveOrUpdate(OrderLog orderLogs) {
        LOG.info("Inside saveOrUpdate OrderLogs");
        return orderLogDao.saveOrUpdate(orderLogs);
    }

    @Override
    public OrderLog save(RadiologyOrder order, String hl7Request, String hl7Response, Modality modality) {
        LOG.info("Inside saveOrUpdate OrderLogs");
        OrderLog orderLog = new OrderLog();
        orderLog.setOrder(order);
        orderLog.setHl7Request(hl7Request);
        orderLog.setHl7Response(hl7Response);
        orderLog.setModality(modality);
        return orderLogDao.saveOrUpdate(orderLog);
    }
} 