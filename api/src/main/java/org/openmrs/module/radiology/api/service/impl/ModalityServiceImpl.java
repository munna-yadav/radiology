package org.openmrs.module.radiology.api.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.radiology.api.dao.ModalityDao;
import org.openmrs.module.radiology.api.model.Modality;
import org.openmrs.module.radiology.api.service.ModalityService;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public class ModalityServiceImpl implements ModalityService {
    private static final Log LOG = LogFactory.getLog(ModalityServiceImpl.class);

    private ModalityDao modalityDao;

    public void setModalityDao(ModalityDao modalityDao) {
        this.modalityDao = modalityDao;
    }

    @Override
    public Optional<Modality> getByOrderTypeId(int orderTypeId) {
        LOG.info("Fetching Modality by order_type_id: " + orderTypeId);
        return modalityDao.getByOrderTypeId(orderTypeId);
    }
}