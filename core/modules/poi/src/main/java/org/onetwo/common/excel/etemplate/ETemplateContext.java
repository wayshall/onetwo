package org.onetwo.common.excel.etemplate;

import java.util.Map;

import org.onetwo.common.utils.LangUtils;

public class ETemplateContext {
	
	private Map<String, Object> context = LangUtils.newHashMap();

	public Object put(String key, Object value) {
		return context.put(key, value);
	}

	public void putAll(Map<? extends String, ? extends Object> m) {
		context.putAll(m);
	}

	public Object get(Object key) {
		return context.get(key);
	}

}
