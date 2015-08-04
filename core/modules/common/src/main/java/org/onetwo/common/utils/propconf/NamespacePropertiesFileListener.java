package org.onetwo.common.utils.propconf;

import java.util.Map;

public interface NamespacePropertiesFileListener<T extends NamespaceProperty> {
	
	void afterBuild(ResourceAdapter<?>[] sqlfileArray, Map<String, PropertiesNamespaceInfo<T>> namespaceInfos);
	void afterReload(ResourceAdapter<?> file, PropertiesNamespaceInfo<T> namepsaceInfo);

}
