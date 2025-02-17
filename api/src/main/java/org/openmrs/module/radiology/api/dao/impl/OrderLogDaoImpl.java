package org.openmrs.module.radiology.api.dao.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.openmrs.module.radiology.api.dao.OrderLogDao;
import org.openmrs.module.radiology.api.model.OrderLog;

import java.util.Optional;

public class OrderLogDaoImpl implements OrderLogDao {
    private static final Log LOG = LogFactory.getLog(OrderLogDaoImpl.class);
    
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Optional<OrderLog> get(int id) {
        LOG.info("Inside get OrderLogs");
        return Optional.ofNullable(getCurrentSession().get(OrderLog.class, id));
    }

    @Override
    public OrderLog saveOrUpdate(OrderLog orderLogs) {
        LOG.info("Inside saveOrUpdate OrderLogs");
        getCurrentSession().saveOrUpdate(orderLogs);
        return orderLogs;
    }
} 