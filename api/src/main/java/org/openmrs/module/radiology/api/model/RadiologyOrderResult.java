package org.openmrs.module.radiology.api.model;

import org.openmrs.BaseOpenmrsData;
import org.openmrs.Encounter;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
public class RadiologyOrderResult extends BaseOpenmrsData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer radiologyOrderResultId;

    @ManyToOne
    private String patient;

    private String radiologyOrder;

    private String concept;

    @Enumerated(EnumType.STRING)
    private String status;

    private String radiologyFindings;
    private String radiologyImpressions;
    private String radiologyRecommendations;

    @OneToMany
    @JoinColumn(name = "radiology_order_result_id")
    private List<Encounter> encounters;


    // Default Constructor
    public RadiologyOrderResult() {}

    // Parameterized Constructor


    @Override
    public Integer getId() {
        return radiologyOrderResultId;
    }

    @Override
    public void setId(Integer id) {
        this.radiologyOrderResultId = id;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getRadiologyOrder() {
        return radiologyOrder;
    }

    public void setRadiologyOrder(String radiologyOrder) {
        this.radiologyOrder = radiologyOrder;
    }

    public UUID getConcept() {
        return UUID.fromString(concept);
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRadiologyFindings() {
        return radiologyFindings;
    }

    public void setRadiologyFindings(String radiologyFindings) {
        this.radiologyFindings = radiologyFindings;
    }

    public String getRadiologyImpressions() {
        return radiologyImpressions;
    }

    public void setRadiologyImpressions(String radiologyImpressions) {
        this.radiologyImpressions = radiologyImpressions;
    }

    public String getRadiologyRecommendations() {
        return radiologyRecommendations;
    }

    public void setRadiologyRecommendations(String radiologyRecommendations) {
        this.radiologyRecommendations = radiologyRecommendations;
    }


}
