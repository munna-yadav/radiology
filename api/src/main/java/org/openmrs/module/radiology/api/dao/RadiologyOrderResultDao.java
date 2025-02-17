package org.openmrs.module.radiology.api.dao;

import org.openmrs.module.radiology.api.model.RadiologyOrderResult;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class RadiologyOrderResultDao {

    @PersistenceContext
    private EntityManager entityManager;

    public RadiologyOrderResult save(RadiologyOrderResult result) {
        if (result.getId() == null) {
            entityManager.persist(result);
            return result;
        } else {
            return entityManager.merge(result);
        }
    }

    public RadiologyOrderResult findById(Integer id) {
        return entityManager.find(RadiologyOrderResult.class, id);
    }

    public List<RadiologyOrderResult> findAll() {
        return entityManager.createQuery("FROM RadiologyOrderResult", RadiologyOrderResult.class).getResultList();
    }

    public void deleteById(Integer id) {
        RadiologyOrderResult result = findById(id);
        if (result != null) {
            entityManager.remove(result);
        }
    }
}
