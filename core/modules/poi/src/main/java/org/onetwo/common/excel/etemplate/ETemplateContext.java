package org.onetwo.common.excel.etemplate;

import java.util.Map;

import org.onetwo.common.utils.LangUtils;

import com.google.common.collect.ImmutableMap;

public class ETemplateContext {
	
	private Map<String, Object> dataContext = LangUtils.newHashMap();

	public Object put(String key, Object value) {
		return dataContext.put(key, value);
	}

	public void putAll(Map<? extends String, ? extends Object> m) {
		dataContext.putAll(m);
	}

	public Object get(Object key) {
		return dataContext.get(key);
	}

	public Map<String, Object> getDataContext() {
		return ImmutableMap.copyOf(dataContext);
	}
	
	

}
