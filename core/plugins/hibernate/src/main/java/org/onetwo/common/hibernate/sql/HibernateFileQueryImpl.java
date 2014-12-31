package org.onetwo.common.hibernate.sql;

import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;
import org.onetwo.common.db.DataQuery;
import org.onetwo.common.db.QueryProvider;
import org.onetwo.common.db.sql.DynamicQuery;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.hibernate.HibernateQueryProvider;
import org.onetwo.common.spring.sql.DefaultFileQueryImpl;
import org.onetwo.common.spring.sql.FileSqlParser;
import org.onetwo.common.utils.Assert;

public class HibernateFileQueryImpl extends DefaultFileQueryImpl<HibernateNamedInfo> {

//	private DynamicQuery query;
//	private JFishNamedFileQueryInfo info;
//	private BaseEntityManager baseEntityManager;
//	private FileSqlParser parser;
	

	public HibernateFileQueryImpl(QueryProvider baseEntityManager, HibernateNamedInfo info, boolean count, FileSqlParser<HibernateNamedInfo> parser) {
		super(baseEntityManager, info, count, parser);
		Assert.notNull(baseEntityManager);
//		this.baseEntityManager = baseEntityManager;
//		this.parser = parser;
		
	}
	
	private HibernateQueryProvider getHibernateQueryProvider(){
		return (HibernateQueryProvider) baseEntityManager;
	}
	
	protected DataQuery createDataQuery(DynamicQuery query){
		/*DataQuery dataQuery = null;
		boolean statfull = this.parserContext.getQueryConfig().isStatful();
		if(info.isHql()){
			dataQuery = getHibernateQueryProvider().createQuery(query.getTransitionSql(), statfull);
		}else{
			dataQuery = getHibernateQueryProvider().createSQLQuery(query.getTransitionSql(), query.getEntityClass(), statfull);
		}
		return dataQuery;*/
		return createDataQuery(query.getTransitionSql(), query.getEntityClass());
	}
	
	protected DataQuery createDataQuery(String sql, Class<?> mappedClass){
		DataQuery dataQuery = null;
		boolean statfull = this.getParserContext().getQueryConfig().isStatful();
		if(info.isHql()){
			dataQuery = getHibernateQueryProvider().createQuery(sql, statfull);
		}else{
			dataQuery = getHibernateQueryProvider().createSQLQuery(sql, mappedClass, statfull);
		}
		return dataQuery;
	}

	public <T> T getSingleResult() {
		try {
			return createDataQueryIfNecessarry().getSingleResult();
		} catch (NonUniqueResultException e) {
			throw new BaseException("sql: " + getRawQuery(DataQuery.class).getRawQuery(Query.class).getQueryString() , e);
		}
	}


}
