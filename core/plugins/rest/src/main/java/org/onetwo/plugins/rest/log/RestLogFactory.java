package org.onetwo.plugins.rest.log;

import org.apache.log4j.Logger;

final public class RestLogFactory {

	public static final String INTERFACE_INVOKER_LOGGER = "INTERFACE_INVOKER_LOGGER";

	public static Logger getLogger(){
		return Logger.getLogger(INTERFACE_INVOKER_LOGGER);
	}
	
	private RestLogFactory(){}

}
