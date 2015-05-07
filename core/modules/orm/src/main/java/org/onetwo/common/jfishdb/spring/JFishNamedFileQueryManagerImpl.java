package org.onetwo.common.jfishdb.spring;

import java.util.List;
import java.util.Map;

import org.onetwo.common.db.DataQuery;
import org.onetwo.common.db.FileNamedQueryFactoryListener;
import org.onetwo.common.db.ParamValues.PlaceHolder;
import org.onetwo.common.jfishdb.JFishDataQuery;
import org.onetwo.common.spring.sql.AbstractFileNamedQueryFactory;
import org.onetwo.common.spring.sql.JFishNamedFileQueryInfo;
import org.onetwo.common.spring.sql.JFishNamedSqlFileManager;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.propconf.NamespacePropertiesManager;
import org.springframework.jdbc.core.RowMapper;


public class JFishNamedFileQueryManagerImpl extends  AbstractFileNamedQueryFactory<JFishNamedFileQueryInfo>{

//	private JFishEntityManager baseEntityManager;
	/*private JFishNamedSqlFileManager<JFishNamedFileQueryInfo> sqlFileManager;
	private TemplateParser parser;
	*/
	/*public JFishNamedFileQueryManagerImpl(JFishEntityManager jem, DataBase dbname, boolean watchSqlFile, FileNamedQueryFactoryListener fileNamedQueryFactoryListener) {
		super(fileNamedQueryFactoryListener);
//		this.baseEntityManager = jem;
		
		StringTemplateLoaderFileSqlParser<JFishNamedFileQueryInfo> p = new StringTemplateLoaderFileSqlParser<JFishNamedFileQueryInfo>();
//		p.initialize();
		this.parser = p;
		sqlFileManager = new JFishNamedSqlFileManager<JFishNamedFileQueryInfo> (dbname, watchSqlFile, JFishNamedFileQueryInfo.class, p);
	}*/

	public JFishNamedFileQueryManagerImpl(JFishNamedSqlFileManager<JFishNamedFileQueryInfo> sqlFileManager, FileNamedQueryFactoryListener fileNamedQueryFactoryListener) {
		super(sqlFileManager, fileNamedQueryFactoryListener);
		/*if(sqlFileManager!=null){
			this.parser = (TemplateParser)sqlFileManager.getListener();
			this.sqlFileManager = sqlFileManager;
		}*/
	}


	@Override
	public JFishDataQuery createQuery(String queryName, Object... args){
		return createDataQuery(false, queryName, PlaceHolder.NAMED, args);
	}
	
	public JFishDataQuery createCountQuery(String queryName, Object... args){
		return createDataQuery(true, queryName, PlaceHolder.NAMED, args);
	}

	public JFishDataQuery createDataQuery(boolean count, String queryName, PlaceHolder type, Object... args){
		Assert.notNull(type);

		JFishNamedFileQueryInfo nameInfo = getNamedQueryInfo(queryName);
		JFishFileQueryImpl jq = new JFishFileQueryImpl(getCreateQueryable(), nameInfo, count, parser);
		
		if(type==PlaceHolder.POSITION){
			jq.setParameters(LangUtils.asList(args));
		}else{
			if(args.length==1 && LangUtils.isMap(args[0])){
				jq.setParameters((Map)args[0]);
			}else{
				jq.setQueryAttributes(LangUtils.asMap(args));
			}
		}
		return jq.getRawQuery(JFishDataQuery.class);
	}
	

	@Override
	public DataQuery createQuery(String queryName, PlaceHolder type, Object... args){
		return createDataQuery(false, queryName, type, args);
	}
	


	@Override
	public <T> List<T> findList(String queryName, Object... params) {
		DataQuery jq = this.createQuery(queryName, params);
		return jq.getResultList();
	}


	@Override
	public <T> T findUnique(String queryName, Object... params) {
		DataQuery jq = this.createQuery(queryName, params);
		return jq.getSingleResult();
	}


	@Override
	public <T> Page<T> findPage(String queryName, Page<T> page, Object... params) {
		DataQuery jq = this.createCountQuery(queryName, params);
		Long total = jq.getSingleResult();
		total = (total==null?0:total);
		page.setTotalCount(total);
		if(total>0){
			jq = this.createQuery(queryName, params);
			jq.setFirstResult(page.getFirst()-1);
			jq.setMaxResults(page.getPageSize());
			List<T> datalist = jq.getResultList();
			page.setResult(datalist);
		}
		return page;
	}
	
	
//	@Override
	public <T> Page<T> findPage(String queryName, RowMapper<T> rowMapper, Page<T> page, Object... params) {
		JFishDataQuery jq = this.createCountQuery(queryName, params);
		Long total = jq.getSingleResult();
		page.setTotalCount(total);
		if(total!=null && total>0){
			jq = this.createQuery(queryName, params);
			jq.setFirstResult(page.getFirst()-1);
			jq.setMaxResults(page.getPageSize());
			jq.getJfishQuery().setRowMapper(rowMapper);
			List<T> datalist = jq.getResultList();
			page.setResult(datalist);
		}
		return page;
	}


	@Override
	public NamespacePropertiesManager<JFishNamedFileQueryInfo> getNamespacePropertiesManager() {
		return sqlFileManager;
	}

}
