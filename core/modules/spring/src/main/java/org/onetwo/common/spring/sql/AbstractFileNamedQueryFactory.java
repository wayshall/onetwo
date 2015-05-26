package org.onetwo.common.spring.sql;

import java.util.List;
import java.util.Map;

import org.onetwo.common.db.DataQuery;
import org.onetwo.common.db.FileNamedQueryFactory;
import org.onetwo.common.db.FileNamedQueryFactoryListener;
import org.onetwo.common.db.FileNamedSqlGenerator;
import org.onetwo.common.db.QueryProvideManager;
import org.onetwo.common.spring.ftl.TemplateParser;
import org.onetwo.common.utils.LangUtils;

abstract public class AbstractFileNamedQueryFactory<T extends JFishNamedFileQueryInfo> implements FileNamedQueryFactory<T> {

	private FileNamedQueryFactoryListener fileNamedQueryFactoryListener;
	private QueryProvideManager createQueryable;
	protected JFishNamedSqlFileManager<T> sqlFileManager;
	protected TemplateParser parser;

	public AbstractFileNamedQueryFactory(JFishNamedSqlFileManager<T> sqlFileManager, FileNamedQueryFactoryListener fileNamedQueryFactoryListener) {
		super();
		this.fileNamedQueryFactoryListener = fileNamedQueryFactoryListener;
		if(sqlFileManager!=null){
			this.parser = (TemplateParser)sqlFileManager.getListener();
			this.sqlFileManager = sqlFileManager;
		}
	}


	public void initQeuryFactory(QueryProvideManager em){
		this.createQueryable = em;
		this.buildNamedQueryInfos();
		if(this.fileNamedQueryFactoryListener!=null)
			this.fileNamedQueryFactoryListener.onInitialized(em, this);
	}
	
	
	public QueryProvideManager getCreateQueryable() {
		return createQueryable;
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


	public T getNamedQueryInfo(String name) {
		return sqlFileManager.getNamedQueryInfo(name);
	}
	@Override
	public FileNamedSqlGenerator<T> createFileNamedSqlGenerator(String queryName, Map<Object, Object> params) {
		T nameInfo = getNamedQueryInfo(queryName);
		FileNamedSqlGenerator<T> g = new DefaultFileNamedSqlGenerator<T>(nameInfo, false, parser, params);
		return g;
	}

	public void buildNamedQueryInfos() {
		this.sqlFileManager.build();
	}
	
}
