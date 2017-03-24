package org.onetwo.common.db.filequery;

import java.util.Collection;
import java.util.Map;

import org.onetwo.common.propconf.ResourceAdapter;

public interface DbmNamedQueryFile {
	public String getKey();
	public String getNamespace();
	public Collection<DbmNamedQueryInfo> getNamedProperties();
	public DbmNamedQueryInfo getNamedProperty(String name);
	public void addAll(Map<String, DbmNamedQueryInfo> namedInfos, boolean throwIfExist);
	public void put(String name, DbmNamedQueryInfo info, boolean throwIfExist);
	boolean isGlobal();
	
	public ResourceAdapter<?> getSource();
}
