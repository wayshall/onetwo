package org.onetwo.common.db.filequery.spi;

import java.util.Map;

import org.onetwo.common.db.filequery.DbmNamedQueryFile;
import org.onetwo.common.propconf.ResourceAdapter;

public interface DbmNamedQueryFileListener {

	public void afterBuild(Map<String, DbmNamedQueryFile> namespaceInfos, ResourceAdapter<?>... sqlfileArray);
	void afterReload(ResourceAdapter<?> file, DbmNamedQueryFile namepsaceInfo);

}
