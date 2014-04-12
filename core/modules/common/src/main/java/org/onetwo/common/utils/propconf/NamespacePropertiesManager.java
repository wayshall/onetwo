package org.onetwo.common.utils.propconf;

import java.util.Collection;

import org.onetwo.common.utils.propconf.AbstractPropertiesManager.NamespaceProperty;

public interface NamespacePropertiesManager<T extends NamespaceProperty> extends JFishPropertiesManager<T>{

	public PropertiesNamespaceInfo<T> getNamespaceProperties(String namespace);
	public boolean containsNamespace(String namespace);
	public Collection<PropertiesNamespaceInfo<T>> getAllNamespaceProperties();
}
