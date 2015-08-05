package org.onetwo.common.db.filequery;

import java.util.Map;

import org.onetwo.common.utils.propconf.ResourceAdapter;

public interface NamespacePropertiesFileListener<T extends NamespaceProperty> {
	
	void afterBuild(ResourceAdapter<?>[] sqlfileArray, Map<String, PropertiesNamespaceInfo<T>> namespaceInfos);
	void afterReload(ResourceAdapter<?> file, PropertiesNamespaceInfo<T> namepsaceInfo);

}
