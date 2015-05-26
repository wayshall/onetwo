package org.onetwo.common.db;

import java.util.List;
import java.util.Map;

import org.onetwo.common.db.ParamValues.PlaceHolder;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.propconf.AbstractPropertiesManager.NamespaceProperty;
import org.onetwo.common.utils.propconf.NamespacePropertiesManager;

/****
 * 基于文件的命名查询工厂
 * @author wayshall
 *
 * @param <PT>
 */
public interface FileNamedQueryFactory<PT extends NamespaceProperty> {
	public String WATCH_SQL_FILE = "watch.sql.file";
	
	/***
	 * 初始化
	 * @param createQueryable
	 */
	public void initQeuryFactory(QueryProvideManager createQueryable);
	
	public NamespacePropertiesManager<PT> getNamespacePropertiesManager();

	public DataQuery createQuery(String queryName, Object... args);
	
	public FileNamedSqlGenerator<PT> createFileNamedSqlGenerator(String queryName, Map<Object, Object> params);
	
//	public JFishQueryValue createQueryValue();
	
	public DataQuery createQuery(String queryName, PlaceHolder type, Object... args);

	public DataQuery createCountQuery(String queryName, Object... args);

	public <T> List<T> findList(String queryName, Object... params);

	public <T> T findUnique(String queryName, Object... params);
	public <T> T findOne(String queryName, Object... params);

	public <T> Page<T> findPage(String queryName, Page<T> page, Object... params);
	
	
}
