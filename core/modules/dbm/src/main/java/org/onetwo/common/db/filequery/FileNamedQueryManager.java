package org.onetwo.common.db.filequery;

import java.util.List;

import org.onetwo.common.db.DbmQueryWrapper;
import org.onetwo.common.db.dquery.NamedQueryInvokeContext;
import org.onetwo.common.utils.Page;

/****
 * 基于文件的命名查询工厂
 * @author wayshall
 *
 */
public interface FileNamedQueryManager {
	
//	public void initQeuryFactory(QueryProvideManager createQueryable);
	
	/****
	 * 通过InvokeContext查找
	 * @param invokeContext
	 * @return
	 */
	public DbmNamedFileQueryInfo getNamedQueryInfo(NamedQueryInvokeContext invokeContext);
	/***
	 * 通过queryName查找
	 * @param queryName
	 * @return
	 */
	public NamespacePropertiesManager<DbmNamedFileQueryInfo> getNamespacePropertiesManager();

	public DbmQueryWrapper createQuery(NamedQueryInvokeContext invokeContext);
	
	public FileNamedSqlGenerator createFileNamedSqlGenerator(NamedQueryInvokeContext invokeContext);
	
//	public DataQuery createQuery(JFishNamedFileQueryInfo nameInfo, PlaceHolder type, Object... args);

	public DbmQueryWrapper createCountQuery(NamedQueryInvokeContext invokeContext);

	public <T> List<T> findList(NamedQueryInvokeContext invokeContext);

	public <T> T findUnique(NamedQueryInvokeContext invokeContext);
	public <T> T findOne(NamedQueryInvokeContext invokeContext);

	public <T> Page<T> findPage(Page<T> page, NamedQueryInvokeContext invokeContext);
	
	
}
