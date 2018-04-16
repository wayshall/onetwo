package org.onetwo.boot.core.config;

import lombok.Data;

import org.onetwo.common.propconf.JFishProperties;

/**
 * @author wayshall
 * <br/>
 */
@Data
public class PluginProperties {
	/***
	 * jfish: 
		 	plugin: 
	        	ProductPlugin: 
	            	appendPluginContextPath: false
	 */
	boolean appendPluginContextPath = true;
	String templatePath;
	JFishProperties viewMapping = new JFishProperties();
	
	String contextPath;
}
