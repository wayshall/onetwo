package org.onetwo.common.fish.spring;

import java.util.List;
import java.util.Map;

import org.onetwo.common.db.AbstractFileNamedQueryFactory;
import org.onetwo.common.db.DataQuery;
import org.onetwo.common.db.FileNamedQueryFactoryListener;
import org.onetwo.common.db.ParamValues.PlaceHolder;
import org.onetwo.common.fish.JFishDataQuery;
import org.onetwo.common.fish.JFishEntityManager;
import org.onetwo.common.fish.JFishQuery;
import org.onetwo.common.spring.sql.JFishNamedFileQueryInfo;
import org.onetwo.common.spring.sql.JFishNamedSqlFileManager;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.propconf.NamespacePropertiesManager;
import org.springframework.jdbc.core.RowMapper;


public class JFishNamedFileQueryManagerImpl extends  AbstractFileNamedQueryFactory<JFishNamedFileQueryInfo>{

	private JFishDaoImplementor jfishFishDao;
	private JFishNamedSqlFileManager<JFishNamedFileQueryInfo> sqlFileManager;
	
	public JFishNamedFileQueryManagerImpl(JFishEntityManager jem, String dbname, boolean watchSqlFile, FileNamedQueryFactoryListener fileNamedQueryFactoryListener) {
		super(fileNamedQueryFactoryListener);
		sqlFileManager = new JFishNamedSqlFileManager<JFishNamedFileQueryInfo> (dbname, watchSqlFile, JFishNamedFileQueryInfo.class);
		this.jfishFishDao = jem.getJfishDao();
	}


	public JFishNamedFileQueryInfo getNamedQueryInfo(String name) {
		return sqlFileManager.getNamedQueryInfo(name);
	}
	@Override
	public void buildNamedQueryInfos() {
		this.sqlFileManager.build();
	}

	public JFishQuery createJFishQuery(String queryName, Object... args){
		return createJFishQuery(false, queryName, PlaceHolder.NAMED, args);
	}
	
	public JFishQuery createCountJFsihQuery(String queryName, Object... args){
		return createJFishQuery(true, queryName, PlaceHolder.NAMED, args);
	}

	public JFishQuery createJFishQuery(boolean count, String queryName, PlaceHolder type, Object... args){
		Assert.notNull(type);

		JFishNamedFileQueryInfo nameInfo = getNamedQueryInfo(queryName);
		JFishQuery jq = new JFishFileQueryImpl(jfishFishDao, nameInfo, count);
		
		if(type==PlaceHolder.POSITION){
			jq.setParameters(LangUtils.asList(args));
		}else{
			if(args.length==1 && LangUtils.isMap(args[0])){
				jq.setParameters((Map)args[0]);
			}else{
				jq.setQueryAttributes(LangUtils.asMap(args));
			}
		}
		return jq;
	}
	

	@Override
	public DataQuery createQuery(String queryName, PlaceHolder type, Object... args){
		return new JFishDataQuery(createJFishQuery(false, queryName, type, args));
	}
	
	@Override
	public DataQuery createQuery(String queryName, Object... args) {
		JFishQuery jq = createJFishQuery(queryName, args);
		return new JFishDataQuery(jq);
	}

	@Override
	public DataQuery createCountQuery(String queryName, Object... args) {
		JFishQuery jq = createCountJFsihQuery(queryName, args);
//		jq.setParameters(LangUtils.asMap(args));
		jq.setQueryAttributes(LangUtils.asMap(args));
//		jq.setResultClass(Long.class);
		return new JFishDataQuery(jq);
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
		JFishQuery jq = this.createCountJFsihQuery(queryName, params);
		Long total = jq.getSingleResult();
		page.setTotalCount(total);
		if(total!=null && total>0){
			jq = this.createJFishQuery(queryName, params);
			jq.setFirstResult(page.getFirst()-1);
			jq.setMaxResults(page.getPageSize());
			jq.setRowMapper(rowMapper);
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
