package org.onetwo.common.ioc.impl;

import java.util.ArrayList;
import java.util.List;

import org.onetwo.common.ioc.Container;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SuppressWarnings("rawtypes")
public class FilterObjectBinder extends DefaultObjectBinder {
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private List<String> ignoreNames = new ArrayList<String>();
	private List<Class> ignoreClasses = new ArrayList<Class>();
	
	public FilterObjectBinder(Container container) {
		super(container);
	}

	protected boolean validate(String name, Object value){
		boolean validate = false;
		if(value instanceof Class){
			validate = ignoreClasses.contains(value);
		}else{
			validate = ignoreClasses.contains(value.getClass());
		}
		return !(validate || this.ignoreNames.contains(name));
	}

	public List<String> getIgnoreNames() {
		return ignoreNames;
	}

	public void setIgnoreNames(List<String> ignoreNames) {
		this.ignoreNames = ignoreNames;
	}

	public List<Class> getIgnoreClasses() {
		return ignoreClasses;
	}

	public void setIgnoreClasses(List<Class> ignoreClasses) {
		this.ignoreClasses = ignoreClasses;
	}
	
}
