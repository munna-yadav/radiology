package org.openmrs.module.radiology.api.service;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import org.openmrs.module.radiology.api.model.Modality;

public interface ModalityService {
    
    Optional<Modality> getByOrderTypeId(@NotNull int orderTypeId);
} 