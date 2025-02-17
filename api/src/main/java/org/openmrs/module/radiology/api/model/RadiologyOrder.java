package org.openmrs.module.radiology.api.model;

import org.openmrs.Concept;
import org.openmrs.ServiceOrder;
import org.openmrs.module.radiology.api.enums.RadiologyOrderStatus;

import javax.persistence.PrePersist;

public class RadiologyOrder extends ServiceOrder {

	private static final long serialVersionUID = 1L;

	private Concept specimenType;

	private Concept bodySite;

	private Concept modality;

	private RadiologyOrder relatedRadiologyOrder;

	private RadiologyOrderStatus radiologyStatus;

	public RadiologyOrder() {
	}

	@PrePersist
	public void setDefaults() {
		if (this.radiologyStatus == null) {
			this.radiologyStatus = RadiologyOrderStatus.PENDING;
		}
		// Add more default value settings if required
	}

	@Override
	public RadiologyOrder copy() {
		RadiologyOrder newOrder = new RadiologyOrder();
		super.copyHelper(newOrder);
		return newOrder;
	}

	/**
	 * Creates a discontinuation order for this.
	 *
	 * @return the newly created order
	 * @see org.openmrs.ServiceOrder#cloneForDiscontinuing()
	 */
	@Override
	public RadiologyOrder cloneForDiscontinuing() {
		RadiologyOrder newOrder = new RadiologyOrder();
		super.cloneForDiscontinuingHelper(newOrder);
		return newOrder;
	}

	/**
	 * Creates a ReferralOrder for revision from this order, sets the previousOrder, action field and
	 * other test order fields.
	 *
	 * @return the newly created order
	 */
	@Override
	public RadiologyOrder cloneForRevision() {
		RadiologyOrder newOrder = new RadiologyOrder();
		super.cloneForRevisionHelper(newOrder);
		return newOrder;
	}

	public Concept getSpecimenType() {
		return specimenType;
	}

	public void setSpecimenType(Concept specimenType) {
		this.specimenType = specimenType;
	}

	public Concept getBodySite() {
		return bodySite;
	}

	public void setBodySite(Concept bodySite) {
		this.bodySite = bodySite;
	}

	public RadiologyOrder getRelatedRadiologyOrder() {
		return relatedRadiologyOrder;
	}

	public void setRelatedRadiologyOrder(RadiologyOrder relatedRadiologyOrder) {
		this.relatedRadiologyOrder = relatedRadiologyOrder;
	}

	public RadiologyOrderStatus getRadiologyStatus() {
		return radiologyStatus;
	}

	public void setRadiologyStatus(RadiologyOrderStatus radiologyStatus) {
		this.radiologyStatus = radiologyStatus;
	}

    public Concept getModality() {
        return modality;
    }

    public void setModality(Concept modality) {
        this.modality = modality;
    }
}