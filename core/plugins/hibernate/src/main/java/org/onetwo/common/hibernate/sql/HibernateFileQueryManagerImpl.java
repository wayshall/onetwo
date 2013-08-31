package org.onetwo.common.hibernate.sql;

import java.util.List;
import java.util.Map;

import org.onetwo.common.db.AbstractFileNamedQueryFactory;
import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.db.DataQuery;
import org.onetwo.common.db.FileNamedQueryFactoryListener;
import org.onetwo.common.db.ParamValues.PlaceHolder;
import org.onetwo.common.spring.sql.JFishNamedSqlFileManager;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.propconf.NamespacePropertiesManager;


public class HibernateFileQueryManagerImpl extends AbstractFileNamedQueryFactory<HibernateNamedInfo> {
	
	private BaseEntityManager baseEntityManager;
	private JFishNamedSqlFileManager<HibernateNamedInfo> sqlFileManager;
	
	public HibernateFileQueryManagerImpl(String dbname, boolean watchSqlFile, BaseEntityManager baseEntityManager, FileNamedQueryFactoryListener fileNamedQueryFactoryListener) {
		super(fileNamedQueryFactoryListener);
		sqlFileManager = new JFishNamedSqlFileManager<HibernateNamedInfo> (dbname, watchSqlFile, HibernateNamedInfo.class);
		this.baseEntityManager = baseEntityManager;
	}

	public HibernateNamedInfo getNamedQueryInfo(String name) {
		return sqlFileManager.getNamedQueryInfo(name);
	}
	@Override
	public void buildNamedQueryInfos() {
		this.sqlFileManager.build();
	}
	
	public HibernateFileQueryImpl createHibernateFileQuery(String queryName, Object... args){
		return createHibernateFileQuery(false, queryName, PlaceHolder.NAMED, args);
	}
	
	public HibernateFileQueryImpl createCountHibernateFileQuery(String queryName, Object... args){
		return createHibernateFileQuery(true, queryName, PlaceHolder.NAMED, args);
	}

	public HibernateFileQueryImpl createHibernateFileQuery(boolean count, String queryName, PlaceHolder type, Object... args){
		Assert.notNull(type);

		HibernateNamedInfo nameInfo = getNamedQueryInfo(queryName);
		HibernateFileQueryImpl jq = new HibernateFileQueryImpl(baseEntityManager, nameInfo, count);
		
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
	
	public DataQuery createCountQuery(String queryName){
		HibernateNamedInfo nameInfo = getNamedQueryInfo(queryName);
		return new HibernateFileQueryImpl(baseEntityManager, nameInfo, true);
	}
	

	@Override
	public DataQuery createQuery(String queryName, PlaceHolder type, Object... args){
		return createHibernateFileQuery(false, queryName, type, args);
	}
	
	@Override
	public DataQuery createQuery(String queryName, Object... args) {
		return createHibernateFileQuery( queryName, args);
	}

	@Override
	public DataQuery createCountQuery(String queryName, Object... args) {
		return createCountHibernateFileQuery(queryName, args);
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
	@Override
	public NamespacePropertiesManager<HibernateNamedInfo> getNamespacePropertiesManager() {
		return sqlFileManager;
	}



	
}
