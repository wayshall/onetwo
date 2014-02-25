package org.onetwo.plugins.groovy;

import java.util.Properties;

import javax.annotation.Resource;

import org.onetwo.common.utils.propconf.PropertiesWraper;

public class GroovyPluginConfig {

	private PropertiesWraper wrapper;
	
	public String getSrcModelDir(){
		return wrapper.getAndThrowIfEmpty("src.model.dir");
	}
	
	public String getSrcWebDir(){
		return wrapper.getAndThrowIfEmpty("src.web.dir");
	}
	
	@Resource
	public void setGroovyPluginProperties(Properties groovyProperties) {
		this.wrapper = PropertiesWraper.wrap(groovyProperties);
	}
	
}
