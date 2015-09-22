package org.onetwo.common.jfishdbm.query;

import java.util.List;
import java.util.Map;

import org.onetwo.common.db.DataQuery;
import org.onetwo.common.db.filequery.AbstractFileNamedQueryFactory;
import org.onetwo.common.db.filequery.JFishNamedFileQueryInfo;
import org.onetwo.common.db.filequery.JFishNamedSqlFileManager;
import org.onetwo.common.db.filequery.NamespacePropertiesManager;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.springframework.jdbc.core.RowMapper;


public class JFishNamedFileQueryManagerImpl extends  AbstractFileNamedQueryFactory {


	public JFishNamedFileQueryManagerImpl(JFishNamedSqlFileManager sqlFileManager) {
		super(sqlFileManager);
		/*if(sqlFileManager!=null){
			this.parser = (TemplateParser)sqlFileManager.getListener();
			this.sqlFileManager = sqlFileManager;
		}*/
	}


	@Override
	public JFishDataQuery createQuery(JFishNamedFileQueryInfo nameInfo, Object... args){
		return createDataQuery(false, nameInfo, args);
	}
	
	public JFishDataQuery createCountQuery(JFishNamedFileQueryInfo nameInfo, Object... args){
		return createDataQuery(true, nameInfo, args);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JFishDataQuery createDataQuery(boolean count, JFishNamedFileQueryInfo nameInfo, Object... args){
//		public JFishDataQuery createDataQuery(boolean count, String queryName, PlaceHolder type, Object... args){
//		Assert.notNull(type);

//		JFishNamedFileQueryInfo nameInfo = getNamedQueryInfo(queryName);
		JFishFileQueryImpl jq = new JFishFileQueryImpl(getCreateQueryable(), nameInfo, count, parser);

		if(args.length==1 && LangUtils.isMap(args[0])){
			jq.setParameters((Map)args[0]);
		}else{
			jq.setQueryAttributes(LangUtils.asMap(args));
		}
		return jq.getRawQuery(JFishDataQuery.class);
	}
	

	/*@Override
	public DataQuery createQuery(JFishNamedFileQueryInfo nameInfo, PlaceHolder type, Object... args){
		return createDataQuery(false, nameInfo, type, args);
	}*/
	


	@Override
	public <T> List<T> findList(JFishNamedFileQueryInfo nameInfo, Object... params) {
		DataQuery jq = this.createQuery(nameInfo, params);
		return jq.getResultList();
	}


	@Override
	public <T> T findUnique(JFishNamedFileQueryInfo nameInfo, Object... params) {
		DataQuery jq = this.createQuery(nameInfo, params);
		return jq.getSingleResult();
	}


	@Override
	public <T> Page<T> findPage(JFishNamedFileQueryInfo nameInfo, Page<T> page, Object... params) {
		DataQuery jq = this.createCountQuery(nameInfo, params);
		Long total = jq.getSingleResult();
		total = (total==null?0:total);
		page.setTotalCount(total);
		if(total>0){
			jq = this.createQuery(nameInfo, params);
			jq.setFirstResult(page.getFirst()-1);
			jq.setMaxResults(page.getPageSize());
			List<T> datalist = jq.getResultList();
			page.setResult(datalist);
		}
		return page;
	}
	
	
//	@Override
	public <T> Page<T> findPage(JFishNamedFileQueryInfo nameInfo, RowMapper<T> rowMapper, Page<T> page, Object... params) {
		JFishDataQuery jq = this.createCountQuery(nameInfo, params);
		Long total = jq.getSingleResult();
		page.setTotalCount(total);
		if(total!=null && total>0){
			jq = this.createQuery(nameInfo, params);
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
