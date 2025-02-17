package org.openmrs.module.radiology.api.service.impl;

import org.openmrs.module.radiology.api.dao.RadiologyOrderResultDao;
import org.openmrs.module.radiology.api.model.RadiologyOrderResult;
import org.openmrs.module.radiology.api.service.RadiologyOrderResultService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class RadiologyOrderResultServiceImpl implements RadiologyOrderResultService {


    @Autowired
    private RadiologyOrderResultDao radiologyOrderResultDao;

    @Override
    public RadiologyOrderResult saveRadiologyOrderResult(RadiologyOrderResult result) {
        return radiologyOrderResultDao.save(result);
    }

    @Override
    public RadiologyOrderResult getRadiologyOrderResultById(Integer id) {
        return radiologyOrderResultDao.findById(id);
    }

    @Override
    public List<RadiologyOrderResult> getAllRadiologyOrderResults() {
        return radiologyOrderResultDao.findAll();
    }

    @Override
    public void deleteRadiologyOrderResult(Integer id) {
        radiologyOrderResultDao.deleteById(id);
    }
}
