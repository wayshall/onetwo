package org.onetwo.ext.poi.excel.generator;

import java.util.Collections;
import java.util.Map;

import com.google.common.collect.Maps;

public class ExecutorModel {

	private String name;
	private String executor;
	private FieldValueExecutor instance;
	
	/**
	 * 扩展属性
	 */
	private Map<String, String> attributes;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExecutor() {
		return executor;
	}

	public void setExecutor(String executor) {
		this.executor = executor;
	}

	public FieldValueExecutor getInstance() {
		return instance;
	}

	public void setInstance(FieldValueExecutor instance) {
		this.instance = instance;
	}

	public Map<String, String> getAttributes() {
		return attributes==null?Collections.EMPTY_MAP:attributes;
	}

	public void addAttribute(String name, String value) {
		if(this.attributes==null){
			this.attributes = Maps.newHashMap();
		}
		this.attributes.put(name, value);
	}
	
	

}
