package org.openmrs.module.radiology.api.dao;

import org.openmrs.module.radiology.api.model.OrderLog;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface OrderLogDao {
    
    Optional<OrderLog> get(@NotNull int id);
    
    OrderLog saveOrUpdate(@NotNull OrderLog orderLogs);
} 