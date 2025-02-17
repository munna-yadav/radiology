package org.openmrs.module.radiology;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.BaseModuleActivator;
import org.openmrs.module.ModuleFactory;

/**
 * This class contains the logic that is run every time this module is either started or stopped.
 */
public class RadiologyModuleActivator extends BaseModuleActivator {
	
	private static final Log LOG = LogFactory.getLog(RadiologyModuleActivator.class);
	
	/**
	 * @see BaseModuleActivator#contextRefreshed()
	 */
	@Override
	public void contextRefreshed() {
		LOG.error("OpenMRS Radiology Module refreshed");
//		Context.getService(ModuleFactory.class).();
	}
	
	/**
	 * @see BaseModuleActivator#started()
	 */
	@Override
	public void started() {
		LOG.error("OpenMRS Radiology Module started");
	}
	
	/**
	 * @see BaseModuleActivator#stopped()
	 */
	@Override
	public void stopped() {
		LOG.error("OpenMRS Radiology Module stopped");
	}
}
