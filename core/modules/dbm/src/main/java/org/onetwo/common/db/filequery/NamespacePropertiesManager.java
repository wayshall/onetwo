package org.onetwo.common.db.filequery;

import java.util.Collection;

import org.onetwo.common.propconf.ResourceAdapter;

public interface NamespacePropertiesManager<T extends NamespaceProperty> /*extends JFishPropertiesManager<T>*/{

	public static final String GLOBAL_NS_KEY = "global";

	public T getJFishProperty(String name);
	public boolean contains(String fullname);
//	public void build();
	public PropertiesNamespaceInfo<T> buildSqlFile(ResourceAdapter<?> sqlFile);
	
	public PropertiesNamespaceInfo<T> getNamespaceProperties(String namespace);
	public boolean containsNamespace(String namespace);
	public Collection<PropertiesNamespaceInfo<T>> getAllNamespaceProperties();
}
