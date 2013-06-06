package org.onetwo.common.utils.propconf;

import org.onetwo.common.utils.propconf.AbstractPropertiesManager.NamespaceProperty;
import org.onetwo.common.utils.propconf.NamespacePropertiesManagerImpl.NamespaceProperties;

public interface NamespacePropertiesManager<T extends NamespaceProperty> extends JFishPropertiesManager<T>{

	public NamespaceProperties<T> getNamespaceProperties(String namespace);
	public boolean containsNamespace(String namespace);
}
