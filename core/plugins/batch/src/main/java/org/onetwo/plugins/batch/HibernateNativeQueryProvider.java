package org.onetwo.plugins.batch;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.ResultTransformer;
import org.onetwo.common.hibernate.RowToBeanTransformer;
import org.onetwo.common.hibernate.SingleColumnTransformer;
import org.onetwo.common.utils.LangUtils;
import org.springframework.batch.item.database.orm.AbstractHibernateQueryProvider;

public class HibernateNativeQueryProvider<E> extends AbstractHibernateQueryProvider {

	private String sqlQuery;

	private Class<E> entityClass;
	
//	private boolean resultTransformer = false;
	
	private ResultTransformer resultTransformerInstance;

	@Override
	public Query createQuery() {
		SQLQuery query = null;
		if (isStatelessSession()) {
			query = getStatelessSession().createSQLQuery(sqlQuery);
		}else {
			query = getStatefulSession().createSQLQuery(sqlQuery);
		}
		configSQLQuery(query);
		return query;
	}
	
	protected void configSQLQuery(SQLQuery query){
		if(entityClass!=null){
			if(resultTransformerInstance!=null){
				query.setResultTransformer(resultTransformerInstance);
			}else{
				query.addEntity(entityClass);
			}
		}
	}

	public String getSqlQuery() {
		return sqlQuery;
	}

	public void setSqlQuery(String sqlQuery) {
		this.sqlQuery = sqlQuery;
	}

	public Class<E> getEntityClass() {
		return entityClass;
	}

	public void setResultClass(Class<E> entityClass, boolean resultTransformer) {
		this.entityClass = entityClass;
//		this.resultTransformer = resultTransformer;
		
		if(resultTransformer){
			//如果是游标的方式，不能获取列名，所以transformer是无效的。。。。。。。。。。
			if(LangUtils.isSimpleType(entityClass))
				this.resultTransformerInstance = new SingleColumnTransformer(entityClass);
			else
				this.resultTransformerInstance = new RowToBeanTransformer(entityClass, false);
		}
	}
	
}
