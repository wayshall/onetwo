package org.onetwo.plugins.groovy;

import java.util.Properties;

import javax.annotation.Resource;

import org.onetwo.common.utils.propconf.JFishProperties;

public class GroovyPluginConfig {

	private JFishProperties wrapper;
	
	public String getModelPackage(){
		return wrapper.getProperty("package.model", "");
	}
	
	public String getControllerPackage(){
		return wrapper.getAndThrowIfEmpty("package.controller");
	}
	
	@Resource
	public void setGroovyPluginProperties(Properties groovyProperties) {
		this.wrapper = JFishProperties.wrap(groovyProperties);
	}
	
}
