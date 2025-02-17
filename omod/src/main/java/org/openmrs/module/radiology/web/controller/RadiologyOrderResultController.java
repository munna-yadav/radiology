package org.openmrs.module.radiology.web.controller;

import org.openmrs.module.radiology.api.model.RadiologyOrderResult;
import org.openmrs.module.radiology.api.service.RadiologyOrderResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/radiology/results")
public class RadiologyOrderResultController {

    @Autowired
    private RadiologyOrderResultService radiologyOrderResultService;

    // Create a new Radiology Order Result
    @PostMapping
    public RadiologyOrderResult createRadiologyOrderResult(@RequestBody RadiologyOrderResult result) {
        return radiologyOrderResultService.saveRadiologyOrderResult(result);
    }

    // Get a single Radiology Order Result by ID
    @GetMapping("/{id}")
    public RadiologyOrderResult getRadiologyOrderResult(@PathVariable Integer id) {
        return radiologyOrderResultService.getRadiologyOrderResultById(id);
    }

    // Get all Radiology Order Results
    @GetMapping
    public List<RadiologyOrderResult> getAllRadiologyOrderResults() {
        return radiologyOrderResultService.getAllRadiologyOrderResults();
    }

    // Delete a Radiology Order Result by ID
    @DeleteMapping("/{id}")
    public void deleteRadiologyOrderResult(@PathVariable Integer id) {
        radiologyOrderResultService.deleteRadiologyOrderResult(id);
    }
}
