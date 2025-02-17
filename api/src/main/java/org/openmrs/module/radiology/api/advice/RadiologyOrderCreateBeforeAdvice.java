package org.openmrs.module.radiology.api.advice;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Order;
import org.openmrs.api.OrderService;
import org.openmrs.api.context.Context;
import org.openmrs.module.radiology.api.enums.RadiologyOrderStatus;
import org.openmrs.module.radiology.api.model.RadiologyOrder;
import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

public class RadiologyOrderCreateBeforeAdvice implements MethodBeforeAdvice {

    private static final Log LOG = LogFactory.getLog(RadiologyOrderCreateBeforeAdvice.class);

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        LOG.info("Inside RadiologyOrderCreateBeforeAdvice > before method");
        OrderService orderService = Context.getOrderService();

        try {
            // Extract the Order object from the arguments
            if (method.getName().equals("saveOrder") && args.length > 0 && args[0] instanceof Order) {
                Order order = (Order) args[0];

                // Check if the order already exists by looking at the database
                if (orderService.getOrderByUuid(order.getUuid()) == null) {
                    // This is a new radiology order
                    if (order instanceof RadiologyOrder) {
                        RadiologyOrder radiologyOrder = (RadiologyOrder) order;
                        radiologyOrder.setRadiologyStatus(RadiologyOrderStatus.PENDING);
                    }
                }
            }
        }
        catch (Exception ex) {
            LOG.error("Error setting radiologyStatus: " + ex.getMessage(), ex);
        }
    }
}