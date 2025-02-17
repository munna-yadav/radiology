package org.openmrs.module.radiology.api.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.*;
import org.openmrs.api.APIException;
import org.openmrs.api.OrderContext;
import org.openmrs.api.OrderEntryException;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.OrderServiceImpl;
import org.openmrs.module.radiology.api.model.RadiologyOrder;
import org.springframework.stereotype.Service;

//@Service("orderService")
public class RadiologyOrderServiceImpl extends OrderServiceImpl {
    private static final Log LOG = LogFactory.getLog(RadiologyOrderServiceImpl.class);

    private final String RADIOLOGY_ORDER_TYPE_UUID = "c19c8e82-8b8d-4b4e-b1ff-3f09890b2db3";

    @Override
    public Order saveOrder(Order order, OrderContext context) throws APIException {
        LOG.info("Inside RadiologyOrderServiceImpl > saveOrder method");
        OrderType orderType = null;

        //Check and set RadiologyOrderType
        if (order instanceof RadiologyOrder) {
            orderType = Context.getOrderService().getOrderTypeByUuid(RADIOLOGY_ORDER_TYPE_UUID);
        }
        if (orderType != null) {
            order.setOrderType(orderType);
        }
        return super.saveOrder(order, context);
    }

//    protected void ensureOrderTypeIsSet(Order order, OrderContext orderContext) {
//        if (order.getOrderType() != null) {
//            return;
//        }
//        OrderType orderType = null;
//        if (orderContext != null) {
//            orderType = orderContext.getOrderType();
//        }
//        if (orderType == null) {
//            orderType = getOrderTypeByConcept(order.getConcept());
//        }
//        if (orderType == null && order instanceof DrugOrder) {
//            orderType = Context.getOrderService().getOrderTypeByUuid(OrderType.DRUG_ORDER_TYPE_UUID);
//        }
//        if (orderType == null && order instanceof TestOrder) {
//            orderType = Context.getOrderService().getOrderTypeByUuid(OrderType.TEST_ORDER_TYPE_UUID);
//        }
//        if (orderType == null && order instanceof ReferralOrder) {
//            orderType = Context.getOrderService().getOrderTypeByUuid(OrderType.REFERRAL_ORDER_TYPE_UUID);
//        }
//        // Custom logic for RadiologyOrder
//        if (orderType == null && order instanceof RadiologyOrder) {
//            orderType = Context.getOrderService().getOrderTypeByUuid(RADIOLOGY_ORDER_TYPE_UUID);
//        }
//        if (orderType == null) {
//            throw new OrderEntryException("Order.type.cannot.determine");
//        }
//        Order previousOrder = order.getPreviousOrder();
//        if (previousOrder != null && !orderType.equals(previousOrder.getOrderType())) {
//            throw new OrderEntryException("Order.type.does.not.match");
//        }
//        order.setOrderType(orderType);
//    }
}
