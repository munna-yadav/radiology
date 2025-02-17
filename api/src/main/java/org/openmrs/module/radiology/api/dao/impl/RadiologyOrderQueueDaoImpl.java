package org.openmrs.module.radiology.api.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.openmrs.module.radiology.api.dao.RadiologyOrderQueueDao;
import org.openmrs.module.radiology.api.model.RadiologyOrderQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static org.hibernate.criterion.Restrictions.eq;

public class RadiologyOrderQueueDaoImpl implements RadiologyOrderQueueDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(RadiologyOrderQueueDaoImpl.class);
	
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
    public List<RadiologyOrderQueue> findPendingOrFailedOrders() {
        LOGGER.info("Fetching pending or failed orders from the queue, ordered by priority");
		String hql = "FROM RadiologyOrderQueue q WHERE q.status IN (:pendingStatus, :failedStatus) " +
					"ORDER BY CASE q.priority " +
					"WHEN 'STAT' THEN 1 " +
					"WHEN 'ROUTINE' THEN 2 " +
					"WHEN 'SCHEDULE' THEN 3 " +
					"ELSE 4 END"; // Default case for any other priority
		Query<RadiologyOrderQueue> query = getCurrentSession().createQuery(hql, RadiologyOrderQueue.class);
		query.setParameter("pendingStatus", "PENDING");
		query.setParameter("failedStatus", "FAILED");
		return query.getResultList();
    }

	@Override
	public RadiologyOrderQueue saveOrUpdate(RadiologyOrderQueue queue) {
		LOGGER.info("Inside saveOrUpdate");
		if (getCurrentSession() == null) {
			LOGGER.error("getCurrent session is null");
		}
		if (queue == null) {
			LOGGER.error("Queue object is null");
		}

		if (queue.getRadiologyOrderId() == null) {
			LOGGER.error("Queue object > radiology order id is null");
		}
		getCurrentSession().saveOrUpdate(queue);
		return queue;
	}

	@Override
	public Optional<RadiologyOrderQueue> findByRadiologyOrderId(Integer orderId) {
		LOGGER.info("Inside findByRadiologyOrderId, orderId: {}", orderId);
		Criteria criteria = getCurrentSession().createCriteria(RadiologyOrderQueue.class);
		return Optional.ofNullable((RadiologyOrderQueue) criteria.add(eq("radiologyOrderId.orderId", orderId)).uniqueResult());
	}
}
