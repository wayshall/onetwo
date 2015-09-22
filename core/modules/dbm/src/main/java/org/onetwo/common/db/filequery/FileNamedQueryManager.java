package org.onetwo.common.db.filequery;

import java.util.List;
import java.util.Map;

import org.onetwo.common.db.DataQuery;
import org.onetwo.common.db.dquery.InvokeContext;
import org.onetwo.common.utils.Page;

/****
 * 基于文件的命名查询工厂
 * @author wayshall
 *
 * @param <JFishNamedFileQueryInfo>
 */
public interface FileNamedQueryManager {
	
//	public void initQeuryFactory(QueryProvideManager createQueryable);
	
//	public JFishNamedSqlFileManager getSqlFileManager();
	/****
	 * 通过InvokeContext查找
	 * @param invokeContext
	 * @return
	 */
	public JFishNamedFileQueryInfo getNamedQueryInfo(InvokeContext invokeContext);
	/***
	 * 通过queryName查找
	 * @param queryName
	 * @return
	 */
	public JFishNamedFileQueryInfo getNamedQueryInfo(String queryName);
	public NamespacePropertiesManager<JFishNamedFileQueryInfo> getNamespacePropertiesManager();

	public DataQuery createQuery(JFishNamedFileQueryInfo nameInfo, Object... args);
	
	public FileNamedSqlGenerator createFileNamedSqlGenerator(JFishNamedFileQueryInfo nameInfo, Map<Object, Object> params);
	
//	public JFishQueryValue createQueryValue();
	
//	public DataQuery createQuery(JFishNamedFileQueryInfo nameInfo, PlaceHolder type, Object... args);

	public DataQuery createCountQuery(JFishNamedFileQueryInfo nameInfo, Object... args);

	public <T> List<T> findList(JFishNamedFileQueryInfo nameInfo, Object... params);

	public <T> T findUnique(JFishNamedFileQueryInfo nameInfo, Object... params);
	public <T> T findOne(JFishNamedFileQueryInfo nameInfo, Object... params);

	public <T> Page<T> findPage(JFishNamedFileQueryInfo nameInfo, Page<T> page, Object... params);
	
	
}
