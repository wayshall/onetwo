package org.onetwo.common.excel.etemplate;

import java.util.Collections;
import java.util.Map;

import org.onetwo.common.excel.utils.TheFunction;

import com.google.common.collect.Maps;

public class ETemplateContext {
	
	private Map<String, Object> rootObject = Maps.newHashMap();
	private Map<String, Object> dataContext = Maps.newHashMap();
//	private Object rootObject;
	
	public ETemplateContext(){
		dataContext.put(TheFunction.ALIAS_NAME, TheFunction.getInstance());
	}

	public Object put(String key, Object value) {
		return rootObject.put(key, value);
	}

	public void putAll(Map<? extends String, ? extends Object> m) {
		rootObject.putAll(m);
	}
	

	public Object remove(Object key) {
		return rootObject.remove(key);
	}

	public Object get(Object key) {
		return rootObject.get(key);
	}

	public Map<String, Object> getRootObject() {
//		return ImmutableMap.copyOf(rootContext);
		return Collections.unmodifiableMap(rootObject);
	}

	public Map<String, Object> getDataContext() {
		return dataContext;
	}
	
	

	/*public Object getRootObject() {
		return rootObject;
	}

	public void setRootObject(Object rootObject) {
		this.rootObject = rootObject;
	}
	*/

}
