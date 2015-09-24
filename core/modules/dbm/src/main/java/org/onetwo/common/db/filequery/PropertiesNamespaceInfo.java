package org.onetwo.common.db.filequery;

import java.util.Collection;
import java.util.Map;

import org.onetwo.common.propconf.ResourceAdapter;

public interface PropertiesNamespaceInfo<T extends NamespaceProperty> {
	public String getKey();
	public String getNamespace();
	public Collection<T> getNamedProperties();
	public T getNamedProperty(String name);
	public void addAll(Map<String, T> namedInfos, boolean throwIfExist);
	public void put(String name, T info, boolean throwIfExist);
	boolean isGlobal();
	
	public ResourceAdapter<?> getSource();
}
