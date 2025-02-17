package org.openmrs.module.radiology.api.service;

import org.openmrs.module.radiology.api.model.RadiologyOrderResult;

import java.util.List;

public interface RadiologyOrderResultService {

        RadiologyOrderResult saveRadiologyOrderResult(RadiologyOrderResult result);
        RadiologyOrderResult getRadiologyOrderResultById(Integer id);
        List<RadiologyOrderResult> getAllRadiologyOrderResults();
        void deleteRadiologyOrderResult(Integer id);

}
