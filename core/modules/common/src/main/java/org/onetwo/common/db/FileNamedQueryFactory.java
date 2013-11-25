package org.onetwo.common.db;

import java.util.List;

import org.onetwo.common.db.ParamValues.PlaceHolder;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.propconf.AbstractPropertiesManager.NamespaceProperty;
import org.onetwo.common.utils.propconf.NamespacePropertiesManager;

public interface FileNamedQueryFactory<PT extends NamespaceProperty> {
	public String WATCH_SQL_FILE = "watch.sql.file";
	
	public NamespacePropertiesManager<PT> getNamespacePropertiesManager();
	public void initQeuryFactory(BaseEntityManager em);
	
	public DataQuery createQuery(String queryName, Object... args);
	
	public DataQuery createQuery(String queryName, PlaceHolder type, Object... args);

	public DataQuery createCountQuery(String queryName, Object... args);

	public <T> List<T> findList(String queryName, Object... params);

	public <T> T findUnique(String queryName, Object... params);

	public <T> Page<T> findPage(String queryName, Page<T> page, Object... params);
	
	
}
