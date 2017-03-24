package org.onetwo.common.db.filequery.spi;

import java.util.Collection;

import org.onetwo.common.db.filequery.DbmNamedQueryFile;
import org.onetwo.common.db.filequery.DbmNamedQueryInfo;
import org.onetwo.common.propconf.ResourceAdapter;

public interface NamedSqlFileManager {

	public static final String GLOBAL_NS_KEY = "global";

	public DbmNamedQueryInfo getNamedQueryInfo(String name);
	public boolean contains(String fullname);
//	public void build();
	public DbmNamedQueryFile buildSqlFile(ResourceAdapter<?> sqlFile);
	
	public DbmNamedQueryFile getNamespaceProperties(String namespace);
	public boolean containsNamespace(String namespace);
	public Collection<DbmNamedQueryFile> getAllNamespaceProperties();
}
