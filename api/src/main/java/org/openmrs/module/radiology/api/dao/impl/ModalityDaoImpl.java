package org.openmrs.module.radiology.api.dao.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.openmrs.module.radiology.api.dao.ModalityDao;
import org.openmrs.module.radiology.api.model.Modality;

import java.util.Optional;

public class ModalityDaoImpl implements ModalityDao {
    private static final Log LOG = LogFactory.getLog(ModalityDaoImpl.class);
    
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Optional<Modality> getByOrderTypeId(int orderTypeId) {
        LOG.info("Fetching Modality by order_type_id: " + orderTypeId);
        return Optional.ofNullable(getCurrentSession()
            .createQuery("FROM Modality m WHERE m.orderType.id = :orderTypeId", Modality.class)
            .setParameter("orderTypeId", orderTypeId)
            .setMaxResults(1) // Fetch the first record
            .uniqueResult());
    }
} 