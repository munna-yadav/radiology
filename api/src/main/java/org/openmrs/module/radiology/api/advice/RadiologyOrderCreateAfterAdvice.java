package org.openmrs.module.radiology.api.advice;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Order;
import org.openmrs.api.context.Context;
import org.openmrs.module.radiology.api.enums.RadiologyOrderStatus;
import org.openmrs.module.radiology.api.model.RadiologyOrder;
import org.openmrs.module.radiology.api.model.RadiologyOrderQueue;
import org.openmrs.module.radiology.api.service.PacsIntegrationService;
import org.openmrs.module.radiology.api.service.RadiologyOrderQueueService;
import org.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;

public class RadiologyOrderCreateAfterAdvice implements AfterReturningAdvice {

    private static final Log LOG = LogFactory.getLog(RadiologyOrderCreateAfterAdvice.class);

    RadiologyOrderQueueService radiologyOrderQueueService = Context.getService(RadiologyOrderQueueService.class);
    PacsIntegrationService pacsService = Context.getService(PacsIntegrationService.class);


    @Override
    public void afterReturning(Object o, Method method, Object[] args, Object target) throws Throwable {
        LOG.info("Inside RadiologyOrderCreateAfterAdvice > afterReturning method");
        try {
            if (method.getName().equals("saveOrder") && args.length > 0 && args[0] instanceof Order) {
                Order order = (Order) args[0];


                if (order instanceof RadiologyOrder) {
                    RadiologyOrder radiologyOrder = (RadiologyOrder) order;

                    //Create a queue
                    RadiologyOrderQueue queue = radiologyOrderQueueService.saveOrUpdate(radiologyOrder);

                    //Call pacs Service to send
                    pacsService.processOrder(radiologyOrder);

                    //Update record in radiologyQueueService with status
                    queue.setStatus(RadiologyOrderStatus.SENT);
                    radiologyOrderQueueService.saveOrUpdate(queue);

                    //Also update record in RadiologyOrder table
                }

            }
        } catch (Exception e) {
            LOG.error("Error intercepting after order creation: " + e.getMessage(), e);
        }
    }
}