package org.openmrs.module.radiology.api.model;

import javax.persistence.*;

@Entity
@Table(name = "order_log")
public class OrderLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "radiology_order_id")
    private RadiologyOrder order;

    @ManyToOne
    @JoinColumn(name = "modality_id")
    private Modality modality;

    @Column(name = "hl7_request")
    private String hl7Request;

    @Column(name = "hl7_response")
    private String hl7Response;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RadiologyOrder getOrder() {
        return order;
    }

    public void setOrder(RadiologyOrder order) {
        this.order = order;
    }

    public Modality getModality() {
        return modality;
    }

    public void setModality(Modality modality) {
        this.modality = modality;
    }

    public String getHl7Request() {
        return hl7Request;
    }

    public void setHl7Request(String hl7Request) {
        this.hl7Request = hl7Request;
    }

    public String getHl7Response() {
        return hl7Response;
    }

    public void setHl7Response(String hl7Response) {
        this.hl7Response = hl7Response;
    }
}