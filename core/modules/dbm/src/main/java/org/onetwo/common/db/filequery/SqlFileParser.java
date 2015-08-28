package org.onetwo.common.db.filequery;

import org.onetwo.common.db.filequery.NamespacePropertiesFileManagerImpl.JFishPropertyConf;
import org.onetwo.common.propconf.ResourceAdapter;

public interface SqlFileParser<T extends NamespaceProperty> {

	void parseToNamespaceProperty(JFishPropertyConf<T> conf, PropertiesNamespaceInfo<T> np, ResourceAdapter<?> file);
	
}
