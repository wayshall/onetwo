package org.onetwo.common.db.filequery;

import java.util.List;
import java.util.Map;

import org.onetwo.common.db.DataQuery;
import org.onetwo.common.db.ParamValues.PlaceHolder;
import org.onetwo.common.utils.Page;

/****
 * 基于文件的命名查询工厂
 * @author wayshall
 *
 * @param <PT>
 */
public interface FileNamedQueryFactory<PT extends NamespaceProperty> {
	
//	public void initQeuryFactory(QueryProvideManager createQueryable);
	
//	public JFishNamedSqlFileManager getSqlFileManager();
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
