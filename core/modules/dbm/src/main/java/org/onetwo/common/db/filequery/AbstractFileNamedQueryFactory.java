package org.onetwo.common.db.filequery;

import java.util.List;

import org.onetwo.common.db.DataBase;
import org.onetwo.common.db.DbmQueryWrapper;
import org.onetwo.common.db.dquery.NamedQueryInvokeContext;
import org.onetwo.common.db.filequery.spi.FileNamedQueryManager;
import org.onetwo.common.db.filequery.spi.FileNamedSqlGenerator;
import org.onetwo.common.utils.LangUtils;

abstract public class AbstractFileNamedQueryFactory implements FileNamedQueryManager {

//	private FileNamedQueryFactoryListener fileNamedQueryFactoryListener;
//	private QueryProvideManager queryProvideManager;
	protected DbmNamedSqlFileManager sqlFileManager;
	private DataBase dataBase;

	public AbstractFileNamedQueryFactory(DbmNamedSqlFileManager sqlFileManager, DataBase dataBase) {
		this.sqlFileManager = sqlFileManager;
		this.dataBase = dataBase;
	}


	@Override
	public <E> E findOne(NamedQueryInvokeContext invokeContex) {
		DbmQueryWrapper jq = this.createQuery(invokeContex);
		E entity = null;
		List<E> list = jq.getResultList();
		if(LangUtils.hasElement(list))
			entity = list.get(0);
		return entity;
	}
	
	/*@Override
	public JFishNamedFileQueryInfo getNamedQueryInfo(String queryName){
		JFishNamedFileQueryInfo queryInfo = sqlFileManager.getNamedQueryInfo(queryName);
		return queryInfo;
	}*/

	@Override
	public DbmNamedQueryInfo getNamedQueryInfo(NamedQueryInvokeContext invokeContex) {
		String qname = invokeContex.getQueryName();
		DbmNamedQueryInfo queryInfo = sqlFileManager.getNamedQueryInfo(qname);
		return queryInfo;
	}
	@Override
	public FileNamedSqlGenerator createFileNamedSqlGenerator(NamedQueryInvokeContext invokeContext) {
		DbmNamedQueryInfo nameInfo = getNamedQueryInfo(invokeContext);
		FileNamedSqlGenerator g = new DefaultFileNamedSqlGenerator(nameInfo, false, sqlFileManager.getSqlStatmentParser(), invokeContext.getParsedParams(), dataBase);
		return g;
	}

	/*public QueryProvideManager getQueryProvideManager() {
		return queryProvideManager;
	}


	public void setQueryProvideManager(QueryProvideManager queryProvideManager) {
		this.queryProvideManager = queryProvideManager;
	}*/

	public DataBase getDataBase() {
		return dataBase;
	}

	public void setDataBase(DataBase dataBase) {
		this.dataBase = dataBase;
	}


	public DbmNamedSqlFileManager getSqlFileManager() {
		return sqlFileManager;
	}

	
}
