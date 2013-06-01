package org.onetwo.common.ioc.impl;

import java.util.HashMap;
import java.util.Map;

import org.onetwo.common.ioc.Container;
import org.onetwo.common.ioc.ObjectBinder;
import org.onetwo.common.ioc.ObjectInfo;
import org.onetwo.common.ioc.Valuer;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SuppressWarnings("unchecked")
public class DefaultObjectBinder implements ObjectBinder {
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private Container container;
	
	private Map<String, ObjectInfo> mapper = new HashMap<String, ObjectInfo>();
	
	DefaultObjectBinder(){
	}
	
	public DefaultObjectBinder(Container container) {
		super();
		this.container = container;
	}
	
	public void setContainer(Container container) {
		this.container = container;
	}

	protected boolean validate(String name, Object value){
		return true;
	}

	public void bind(Class clazz){
		bind(StringUtils.uncapitalize(clazz.getSimpleName()), clazz);
	}

	public void bind(String name, String value){ 
		if(!validate(name, value))
			return ;
		ObjectInfo objInfo = this.container.registerObject(name, value);
		mapper.put(name, objInfo);
	}

	public void bind(String name, Valuer valuer){
		if(!validate(name, valuer))
			return ;
		ObjectInfo objInfo = this.container.registerObject(name, valuer);
		mapper.put(name, objInfo);
	}

	public void bind(String name, Class clazz, Object...objects){
		if(!validate(name, clazz))
			return ;
		ObjectInfo objInfo = this.container.registerClass(name, clazz, objects);
		mapper.put(name, objInfo);
	}

	public void bindAsList(String name, Object... value){
		ObjectInfo objInfo = this.container.registerList(name, value);
		mapper.put(name, objInfo);
	}


	public void bindAsMap(String name, Map map){
		ObjectInfo objInfo = this.container.registerMap(name, map);
		mapper.put(name, objInfo);
	}

	public void bindAsMap(String name, Object... value){
		ObjectInfo objInfo = this.container.registerMap(name, value);
		mapper.put(name, objInfo);
	}

	public MapBinder map(String name){
		MapBinder mapBinder = new MapBinder(this, name);
		return mapBinder;
	}

	public Map<String, ObjectInfo> getMapper() {
		return mapper;
	}
	
	public static class MapBinder {
		private Map map;
		private String name;
		private ObjectBinder binder;
		
		private MapBinder(ObjectBinder binder, String name){
			this.name = name;
			this.map = new HashMap();
			this.binder = binder;
		}
		public MapBinder map(String key, Object value){
			map.put(key, value);
			return this;
		}
		
		public void bindit(){
			binder.bindAsMap(name, map);
		}
	}
	
}
