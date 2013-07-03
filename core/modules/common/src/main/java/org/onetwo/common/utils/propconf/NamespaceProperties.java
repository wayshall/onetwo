package org.onetwo.common.utils.propconf;

import java.util.Collection;
import java.util.Map;

import org.onetwo.common.utils.propconf.AbstractPropertiesManager.NamespaceProperty;

public interface NamespaceProperties<T extends NamespaceProperty> {
	public String getNamespace();
	public Collection<T> getNamedProperties();
	public T getNamedProperty(String name);
	public void addAll(Map<String, T> namedInfos, boolean throwIfExist);
	boolean isGlobal();
}
