package org.onetwo.common.utils.propconf;

import java.util.Enumeration;

@SuppressWarnings("rawtypes")
public interface VariableConfig {

	public String getOriginalProperty(String key, String defaultValue);

	public String getOriginalProperty(String key);

	public Object setOriginalProperty(String key, Object value);
	
	public Enumeration configNames();
	
	public boolean load(String path);
	
	public String remove(String key);
	
	public void clear();
	
	public boolean containsKey(Object key);

}