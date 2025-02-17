package org.openmrs.module.radiology.web.resources;

import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.IntegerProperty;
import io.swagger.models.properties.RefProperty;
import io.swagger.models.properties.StringProperty;
import org.openmrs.Order;
import org.openmrs.api.context.Context;
import org.openmrs.module.radiology.api.enums.RadiologyOrderStatus;
import org.openmrs.module.radiology.api.model.RadiologyOrder;
import org.openmrs.module.webservices.docs.swagger.core.property.EnumProperty;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.annotation.PropertyGetter;
import org.openmrs.module.webservices.rest.web.annotation.SubClassHandler;
import org.openmrs.module.webservices.rest.web.api.RestService;
import org.openmrs.module.webservices.rest.web.representation.CustomRepresentation;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.BaseDelegatingSubclassHandler;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingSubclassHandler;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SubClassHandler(supportedClass = RadiologyOrder.class, supportedOpenmrsVersions = { "2.6.* - 9.*" })
public class RadiologyOrderSubclassHandler extends BaseDelegatingSubclassHandler<Order, RadiologyOrder> implements DelegatingSubclassHandler<Order, RadiologyOrder> {

	private static final Logger LOGGER = LoggerFactory.getLogger(RadiologyOrderSubclassHandler.class);
	
	@Override
	public String getTypeName() {
		return "radiologyOrder";
	}
	
	@Override
	public PageableResult getAllByType(RequestContext requestContext) throws ResourceDoesNotSupportOperationException {
		LOGGER.info("Inside getAllByType");
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public RadiologyOrder newDelegate() {
		return new RadiologyOrder();
	}
	
	@Override
	public DelegatingResourceDescription getUpdatableProperties() throws ResourceDoesNotSupportOperationException {
		LOGGER.info("Inside getUpdatableProperties");
		OrderResource2_5 orderResource = (OrderResource2_5) Context.getService(RestService.class)
		        .getResourceBySupportedClass(Order.class);
		return orderResource.getUpdatableProperties();
	}
	
	@PropertyGetter("display")
	public static String getDisplay(RadiologyOrder delegate) {
		LOGGER.info("Inside getDisplay");
		OrderResource2_5 orderResource = (OrderResource2_5) Context.getService(RestService.class)
		        .getResourceBySupportedClass(Order.class);
		return orderResource.getDisplayString(delegate);
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		LOGGER.info("Inside getRepresentationDescription");
		if (rep instanceof DefaultRepresentation) {
			OrderResource2_5 orderResource = (OrderResource2_5) Context.getService(RestService.class)
			        .getResourceBySupportedClass(Order.class);
			DelegatingResourceDescription d = orderResource.getRepresentationDescription(rep);
			d.addProperty("specimenSource", Representation.REF);
			d.addProperty("laterality");
			d.addProperty("clinicalHistory");
			d.addProperty("frequency", Representation.REF);
			d.addProperty("numberOfRepeats");
			d.addProperty("specimenType", Representation.REF);
			d.addProperty("bodySite", Representation.REF);
			d.addProperty("modality", Representation.REF);
			d.addProperty("radiologyStatus");
			d.addProperty("relatedRadiologyOrder", Representation.REF);
			return d;
		} else if (rep instanceof FullRepresentation) {
			OrderResource2_5 orderResource = (OrderResource2_5) Context.getService(RestService.class)
			        .getResourceBySupportedClass(Order.class);
			DelegatingResourceDescription d = orderResource.getRepresentationDescription(rep);
			d.addProperty("specimenSource", Representation.FULL);
			d.addProperty("laterality");
			d.addProperty("clinicalHistory");
			d.addProperty("frequency", Representation.DEFAULT);
			d.addProperty("numberOfRepeats");
			d.addProperty("specimenType", Representation.FULL);
			d.addProperty("bodySite", Representation.FULL);
			d.addProperty("modality", Representation.FULL);
			d.addProperty("radiologyStatus");
			d.addProperty("relatedRadiologyOrder", Representation.FULL);
			return d;
		} else if (rep instanceof CustomRepresentation) { // custom rep
			return null;
		}
		return null;
	}
	
	@Override
	public DelegatingResourceDescription getCreatableProperties() {
		LOGGER.info("Inside getCreatableProperties");
		OrderResource2_5 orderResource = (OrderResource2_5) Context.getService(RestService.class)
		        .getResourceBySupportedClass(Order.class);
		DelegatingResourceDescription d = orderResource.getCreatableProperties();
		d.addProperty("specimenSource");
		d.addProperty("laterality");
		d.addProperty("clinicalHistory");
		d.addProperty("frequency");
		d.addProperty("numberOfRepeats");
		d.addProperty("orderType");
		d.addProperty("bodySite");
		d.addProperty("modality");
		d.addProperty("specimenType");
		d.addProperty("radiologyStatus");
		d.addProperty("commentToFulfiller");
		d.addProperty("scheduledDate");
		d.addProperty("relatedRadiology");
		return d;
	}
	
	@Override
	public Model getGETModel(Representation rep) {
		LOGGER.info("Inside getGETModel");
		OrderResource2_5 orderResource = (OrderResource2_5) Context.getService(RestService.class)
		        .getResourceBySupportedClass(Order.class);
		ModelImpl orderModel = (ModelImpl) orderResource.getGETModel(rep);
		orderModel.property("laterality", new EnumProperty(RadiologyOrder.Laterality.class))
		        .property("clinicalHistory", new StringProperty())
				.property("radiologyStatus", new EnumProperty(RadiologyOrderStatus.PENDING.getClass()))
				.property("numberOfRepeats", new IntegerProperty());
		
		if (rep instanceof DefaultRepresentation) {
			orderModel.property("specimenSource", new RefProperty("#/definitions/ConceptGetRef")).property("frequency",
			    new RefProperty("#/definitions/OrderfrequencyGetRef"));
		} else if (rep instanceof FullRepresentation) {
			orderModel.property("specimenSource", new RefProperty("#/definitions/ConceptGet")).property("frequency",
			    new RefProperty("#/definitions/OrderfrequencyGet"));
		}
		return orderModel;
	}
	
	@Override
	public Model getCREATEModel(Representation rep) {
		LOGGER.info("Inside getCREATEModel");
		OrderResource2_5 orderResource = (OrderResource2_5) Context.getService(RestService.class)
		        .getResourceBySupportedClass(Order.class);
		ModelImpl orderModel = (ModelImpl) orderResource.getCREATEModel(rep);
		return orderModel.property("specimenSource", new StringProperty().example("uuid"))
				.property("modality", new StringProperty().example("uuid"))
		        .property("laterality", new EnumProperty(RadiologyOrder.Laterality.class))
				.property("radiologyStatus", new EnumProperty(RadiologyOrderStatus.PENDING.getClass()))
		        .property("clinicalHistory", new StringProperty())
		        .property("frequency", new StringProperty().example("uuid"))
		        .property("numberOfRepeats", new IntegerProperty());
	}
	
}
