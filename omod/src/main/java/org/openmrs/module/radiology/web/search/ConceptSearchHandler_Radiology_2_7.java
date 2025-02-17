package org.openmrs.module.radiology.web.search;

import org.openmrs.Concept;
import org.openmrs.ConceptClass;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.api.SearchConfig;
import org.openmrs.module.webservices.rest.web.resource.api.SearchHandler;
import org.openmrs.module.webservices.rest.web.resource.api.SearchQuery;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.response.ResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class ConceptSearchHandler_Radiology_2_7 implements SearchHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConceptSearchHandler_Radiology_2_7.class);
	
	private final SearchConfig searchConfig = new SearchConfig("byConceptClassRadiology",
	        RestConstants.VERSION_1 + "/concept",
	        Collections.singletonList("2.4.* - 9.*"),
	        Collections.singletonList(new SearchQuery.Builder("Search concepts by concept class")
	                .withRequiredParameters("conceptClass").build()));
	
	@Override
	public SearchConfig getSearchConfig() {
		return this.searchConfig;
	}
	
	@Override
	public PageableResult search(RequestContext requestContext) throws ResponseException {
		LOGGER.info("Inside search");
		String conceptClass = requestContext.getParameter("conceptClass");
		ConceptService service = Context.getConceptService();
		ConceptClass conceptClazz = service.getConceptClassByUuid(conceptClass);
		
		List<Concept> results = service.getConceptsByClass(conceptClazz);
		return new NeedsPaging<>(results, requestContext);
	}
}
