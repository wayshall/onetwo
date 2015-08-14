package org.onetwo.common.spring.sql;

import java.util.List;
import java.util.Map;

import org.onetwo.common.db.DataQuery;
import org.onetwo.common.db.filequery.FileNamedQueryFactory;
import org.onetwo.common.db.filequery.FileNamedSqlGenerator;
import org.onetwo.common.db.filequery.QueryProvideManager;
import org.onetwo.common.spring.ftl.TemplateParser;
import org.onetwo.common.utils.LangUtils;

abstract public class AbstractFileNamedQueryFactory implements FileNamedQueryFactory<JFishNamedFileQueryInfo> {

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
	public <E> E findOne(String queryName, Object... params) {
		DataQuery jq = this.createQuery(queryName, params);
		E entity = null;
		List<E> list = jq.getResultList();
		if(LangUtils.hasElement(list))
			entity = list.get(0);
		return entity;
	}


	public JFishNamedFileQueryInfo getNamedQueryInfo(String name) {
		return sqlFileManager.getNamedQueryInfo(name);
	}
	@Override
	public FileNamedSqlGenerator<JFishNamedFileQueryInfo> createFileNamedSqlGenerator(String queryName, Map<Object, Object> params) {
		JFishNamedFileQueryInfo nameInfo = getNamedQueryInfo(queryName);
		FileNamedSqlGenerator<JFishNamedFileQueryInfo> g = new DefaultFileNamedSqlGenerator(nameInfo, false, parser, params, queryProvideManager.getDataBase());
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
