package org.onetwo.common.utils.propconf;

import java.util.Collection;

public interface NamespacePropertiesManager<T extends NamespaceProperty> /*extends JFishPropertiesManager<T>*/{


	public T getJFishProperty(String name);
	public boolean contains(String fullname);
	public void build();
	
	public PropertiesNamespaceInfo<T> getNamespaceProperties(String namespace);
	public boolean containsNamespace(String namespace);
	public Collection<PropertiesNamespaceInfo<T>> getAllNamespaceProperties();
}
