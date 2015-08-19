package org.onetwo.common.db.filequery;

import java.util.List;
import java.util.Map;

import org.onetwo.common.db.DataQuery;
import org.onetwo.common.db.dquery.InvokeContext;
import org.onetwo.common.spring.ftl.TemplateParser;
import org.onetwo.common.utils.LangUtils;

abstract public class AbstractFileNamedQueryFactory implements FileNamedQueryManager {

//	private FileNamedQueryFactoryListener fileNamedQueryFactoryListener;
	private QueryProvideManager queryProvideManager;
	protected JFishNamedSqlFileManager sqlFileManager;
	protected TemplateParser parser;

	public AbstractFileNamedQueryFactory(JFishNamedSqlFileManager sqlFileManager) {
		super();
//		this.fileNamedQueryFactoryListener = fileNamedQueryFactoryListener;
		if(sqlFileManager!=null){
			this.parser = sqlFileManager.getSqlStatmentParser();
			this.sqlFileManager = sqlFileManager;
		}
	}


	/*public void initQeuryFactory(QueryProvideManager em){
		this.createQueryable = em;
//		this.sqlFileManager.build();
		if(this.fileNamedQueryFactoryListener!=null)
			this.fileNamedQueryFactoryListener.onInitialized(em, this);
	}*/
	
	
	public QueryProvideManager getCreateQueryable() {
		return getQueryProvideManager();
	}

	@Override
	public <E> E findOne(JFishNamedFileQueryInfo nameInfo, Object... params) {
		DataQuery jq = this.createQuery(nameInfo, params);
		E entity = null;
		List<E> list = jq.getResultList();
		if(LangUtils.hasElement(list))
			entity = list.get(0);
		return entity;
	}
	
	@Override
	public JFishNamedFileQueryInfo getNamedQueryInfo(String queryName){
		JFishNamedFileQueryInfo queryInfo = sqlFileManager.getNamedQueryInfo(queryName);
		return queryInfo;
	}

	@Override
	public JFishNamedFileQueryInfo getNamedQueryInfo(InvokeContext invokeContex) {
		String qname = invokeContex.getQueryName();
		JFishNamedFileQueryInfo queryInfo = sqlFileManager.getNamedQueryInfo(qname);
		return queryInfo;
	}
	@Override
	public FileNamedSqlGenerator createFileNamedSqlGenerator(JFishNamedFileQueryInfo nameInfo, Map<Object, Object> params) {
//		JFishNamedFileQueryInfo nameInfo = getNamedQueryInfo(queryName);
		FileNamedSqlGenerator g = new DefaultFileNamedSqlGenerator(nameInfo, false, parser, params, queryProvideManager.getDataBase());
		return g;
	}


	public QueryProvideManager getQueryProvideManager() {
		return queryProvideManager;
	}


	public void setQueryProvideManager(QueryProvideManager queryProvideManager) {
		this.queryProvideManager = queryProvideManager;
	}


	public JFishNamedSqlFileManager getSqlFileManager() {
		return sqlFileManager;
	}

	
}
