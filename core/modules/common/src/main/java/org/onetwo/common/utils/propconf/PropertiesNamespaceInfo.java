package org.onetwo.common.utils.propconf;

import java.util.Collection;
import java.util.Map;

public interface PropertiesNamespaceInfo<T extends NamespaceProperty> {
	public String getKey();
	public String getNamespace();
	public Collection<T> getNamedProperties();
	public T getNamedProperty(String name);
	public void addAll(Map<String, T> namedInfos, boolean throwIfExist);
	public void put(String name, T info, boolean throwIfExist);
	boolean isGlobal();
}
