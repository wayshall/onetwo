package org.onetwo.common.db.filequery;

import java.util.Map;

import org.onetwo.common.propconf.ResourceAdapter;

public interface NamespacePropertiesFileListener<T extends NamespaceProperty> {

	public void afterBuild(Map<String, PropertiesNamespaceInfo<T>> namespaceInfos, ResourceAdapter<?>... sqlfileArray);
	void afterReload(ResourceAdapter<?> file, PropertiesNamespaceInfo<T> namepsaceInfo);

}
