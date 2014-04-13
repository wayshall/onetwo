package org.onetwo.common.utils.propconf;

import java.util.Map;

import org.onetwo.common.utils.propconf.AbstractPropertiesManager.NamespaceProperty;

public interface PropertiesNamespaceInfoListener<T extends NamespaceProperty> {
	
	void afterBuild(ResourceAdapter[] sqlfileArray, Map<String, PropertiesNamespaceInfo<T>> namespaceInfos);
	void afterReload(ResourceAdapter file, PropertiesNamespaceInfo<T> namepsaceInfo);

}
