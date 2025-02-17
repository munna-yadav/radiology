package org.openmrs.module.radiology.api.model;

import org.openmrs.BaseChangeableOpenmrsData;
import org.openmrs.Order;
import org.openmrs.module.radiology.api.enums.RadiologyOrderStatus;

import javax.persistence.*;

@Entity
@Table(name = "radiology_order_queue")
public class RadiologyOrderQueue extends BaseChangeableOpenmrsData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
	@JoinColumn(name = "radiology_order_id")
    private RadiologyOrder radiologyOrderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "varchar(20) default 'PENDING'")
    private RadiologyOrderStatus status; // PENDING, FAILED, COMPLETED, etc.

    @Enumerated(EnumType.STRING)
    @Column(name = "urgency", nullable = false)
    private Order.Urgency urgency; // e.g., "STAT", "ROUTINE", "SCHEDULE"

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RadiologyOrder getRadiologyOrderId() {
        return radiologyOrderId;
    }

    public void setRadiologyOrderId(RadiologyOrder radiologyOrderId) {
        this.radiologyOrderId = radiologyOrderId;
    }

    public RadiologyOrderStatus getStatus() {
        return status;
    }

    public void setStatus(RadiologyOrderStatus status) {
        this.status = status;
    }

    public Order.Urgency getUrgency() {
        return urgency;
    }

    public void setUrgency(Order.Urgency urgency) {
        this.urgency = urgency;
    }
}