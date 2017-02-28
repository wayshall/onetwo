package org.onetwo.dbm.query;

import java.util.List;

import org.onetwo.common.db.DbmQueryWrapper;
import org.onetwo.common.db.dquery.NamedQueryInvokeContext;
import org.onetwo.common.db.filequery.AbstractFileNamedQueryFactory;
import org.onetwo.common.db.filequery.DbmNamedFileQueryInfo;
import org.onetwo.common.db.filequery.JFishNamedSqlFileManager;
import org.onetwo.common.db.filequery.NamespacePropertiesManager;
import org.onetwo.common.utils.Assert;
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
	public DbmQueryWrapperImpl createQuery(NamedQueryInvokeContext invokeContext){
		return createDataQuery(false, invokeContext);
	}
	
	public DbmQueryWrapperImpl createCountQuery(NamedQueryInvokeContext invokeContext){
		return createDataQuery(true, invokeContext);
	}

	/*@SuppressWarnings({ "unchecked", "rawtypes" })
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
	}*/
	
	public DbmQueryWrapperImpl createDataQuery(boolean count, NamedQueryInvokeContext invokeContext){
//		public JFishDataQuery createDataQuery(boolean count, String queryName, PlaceHolder type, Object... args){
		Assert.notNull(invokeContext);

		invokeContext.setParser(parser);
		DbmNamedFileQueryInfo nameInfo = getNamedQueryInfo(invokeContext);
		DbmFileQueryWrapperImpl jq = new DbmFileQueryWrapperImpl(getCreateQueryable(), nameInfo, count, invokeContext);

		jq.setQueryAttributes(invokeContext.getParsedParams());
//		jq.setRowMapper(rowMapper);
		return jq.getRawQuery(DbmQueryWrapperImpl.class);
	}
	

	/*@Override
	public DataQuery createQuery(JFishNamedFileQueryInfo nameInfo, PlaceHolder type, Object... args){
		return createDataQuery(false, nameInfo, type, args);
	}*/
	


	@Override
	public <T> List<T> findList(NamedQueryInvokeContext invokeContext) {
		DbmQueryWrapper jq = this.createQuery(invokeContext);
		return jq.getResultList();
	}


	@Override
	public <T> T findUnique(NamedQueryInvokeContext invokeContext) {
		DbmQueryWrapper jq = this.createQuery(invokeContext);
		return jq.getSingleResult();
	}


	@Override
	public <T> Page<T> findPage(Page<T> page, NamedQueryInvokeContext invokeContext) {
		DbmQueryWrapper jq = this.createCountQuery(invokeContext);
		Long total = jq.getSingleResult();
		total = (total==null?0:total);
		page.setTotalCount(total);
		if(total>0){
			jq = this.createQuery(invokeContext);
			jq.setFirstResult(page.getFirst()-1);
			jq.setMaxResults(page.getPageSize());
			List<T> datalist = jq.getResultList();
			page.setResult(datalist);
		}
		return page;
	}
	
	
//	@Override
	public <T> Page<T> findPage(Page<T> page, NamedQueryInvokeContext invokeContext, RowMapper<T> rowMapper) {
		DbmQueryWrapperImpl jq = this.createCountQuery(invokeContext);
		Long total = jq.getSingleResult();
		page.setTotalCount(total);
		if(total!=null && total>0){
			jq = this.createQuery(invokeContext);
			jq.setFirstResult(page.getFirst()-1);
			jq.setMaxResults(page.getPageSize());
			jq.getJfishQuery().setRowMapper(rowMapper);
			List<T> datalist = jq.getResultList();
			page.setResult(datalist);
		}
		return page;
	}


	@Override
	public NamespacePropertiesManager<DbmNamedFileQueryInfo> getNamespacePropertiesManager() {
		return sqlFileManager;
	}

}
