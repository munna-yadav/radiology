package org.openmrs.module.radiology.web.resources;

import org.openmrs.DrugOrder;
import org.openmrs.Order;
import org.openmrs.OrderType;
import org.openmrs.ReferralOrder;
import org.openmrs.TestOrder;
import org.openmrs.api.OrderContext;
import org.openmrs.api.context.Context;
import org.openmrs.module.radiology.api.enums.RadiologyOrderStatus;
import org.openmrs.module.radiology.api.model.RadiologyOrder;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.v1_0.resource.openmrs2_2.OrderResource2_2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Resource(name = RestConstants.VERSION_1 + "/order", supportedClass = Order.class, supportedOpenmrsVersions = {
        "2.6.* - 9.*" }, order = 3)
public class OrderResource2_5 extends OrderResource2_2 {
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderResource2_5.class);

	private final String RADIOLOGY_ORDER_TYPE_UUID = "c19c8e82-8b8d-4b4e-b1ff-3f09890b2db3";

	@Override
	public Order save(Order delegate) {
		LOGGER.info("Inside save");
		return Context.getOrderService().saveOrder(delegate, setOrderContext(delegate));
	}

	private OrderContext setOrderContext(Order order) {
		LOGGER.info("Inside setOrderContext");
		OrderContext orderContext = new OrderContext();

		OrderType orderType = Context.getOrderService().getOrderTypeByConcept(order.getConcept());

		if (orderType == null && order instanceof DrugOrder) {
			orderType = Context.getOrderService().getOrderTypeByUuid(OrderType.DRUG_ORDER_TYPE_UUID);

		} else if (orderType == null && order instanceof TestOrder) {
			orderType = Context.getOrderService().getOrderTypeByUuid(OrderType.TEST_ORDER_TYPE_UUID);
		} else if (orderType == null && order instanceof ReferralOrder) {
			orderType = Context.getOrderService().getOrderTypeByUuid(OrderType.REFERRAL_ORDER_TYPE_UUID);
		} else if (orderType == null && order instanceof RadiologyOrder) {
			orderType = Context.getOrderService().getOrderTypeByUuid(RADIOLOGY_ORDER_TYPE_UUID);
		}

		orderContext.setAttribute("radiologyStatus", RadiologyOrderStatus.PENDING);
		orderContext.setCareSetting(null);
		orderContext.setOrderType(orderType);
		return orderContext;
	}
}
