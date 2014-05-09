package org.onetwo.plugins.groovy;

import java.util.Properties;

import javax.annotation.Resource;

import org.onetwo.common.utils.propconf.PropertiesWraper;

public class GroovyPluginConfig {

	private PropertiesWraper wrapper;
	
	public String getModelPackage(){
		return wrapper.getProperty("package.model", "");
	}
	
	public String getControllerPackage(){
		return wrapper.getAndThrowIfEmpty("package.controller");
	}
	
	@Resource
	public void setGroovyPluginProperties(Properties groovyProperties) {
		this.wrapper = PropertiesWraper.wrap(groovyProperties);
	}
	
}
