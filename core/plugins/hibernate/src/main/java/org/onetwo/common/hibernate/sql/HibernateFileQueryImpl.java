package org.onetwo.common.hibernate.sql;

import org.onetwo.common.db.CreateQueryable;
import org.onetwo.common.db.DataQuery;
import org.onetwo.common.db.sql.DynamicQuery;
import org.onetwo.common.spring.sql.DefaultFileQueryImpl;
import org.onetwo.common.spring.sql.FileSqlParser;
import org.onetwo.common.utils.Assert;

public class HibernateFileQueryImpl extends DefaultFileQueryImpl<HibernateNamedInfo> {

//	private DynamicQuery query;
//	private JFishNamedFileQueryInfo info;
//	private BaseEntityManager baseEntityManager;
//	private FileSqlParser parser;
	

	public HibernateFileQueryImpl(CreateQueryable baseEntityManager, HibernateNamedInfo info, boolean count, FileSqlParser<HibernateNamedInfo> parser) {
		super(baseEntityManager, info, count, parser);
		Assert.notNull(baseEntityManager);
//		this.baseEntityManager = baseEntityManager;
//		this.parser = parser;
		
	}
	
	protected DataQuery createDataQuery(DynamicQuery query){
		DataQuery dataQuery = null;
		if(info.isHql()){
			dataQuery = this.baseEntityManager.createQuery(query.getTransitionSql());
		}else{
			dataQuery = this.baseEntityManager.createSQLQuery(query.getTransitionSql(), query.getEntityClass());
		}
		return dataQuery;
	}
	
	protected DataQuery createDataQuery(String sql, Class<?> mappedClass){
		DataQuery dataQuery = null;
		if(info.isHql()){
			dataQuery = this.baseEntityManager.createQuery(sql);
		}else{
			dataQuery = this.baseEntityManager.createSQLQuery(sql, mappedClass);
		}
		return dataQuery;
	}
	


}
