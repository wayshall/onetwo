package org.onetwo.boot.core.config;

import org.onetwo.common.propconf.JFishProperties;

import lombok.Data;

/**
 * @author wayshall
 * <br/>
 */
@Data
public class PluginProperties {
	public static final String LAYOUT_KEY = "layoutPath";
	public static final String DEFAULT_LAYOUT = "easyui-js.html";
	/***
	 * jfish: 
		 	plugin: 
	        	ProductPlugin: 
	            	appendPluginContextPath: false
	 */
	boolean appendPluginContextPath = true;
	String templatePath;
	String layoutFile = DEFAULT_LAYOUT;
	
	JFishProperties viewMapping = new JFishProperties();
	
	String contextPath;
}
