package org.onetwo.plugins.batch;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.onetwo.common.hibernate.RowToBeanTransformer;
import org.onetwo.common.hibernate.SingleColumnTransformer;
import org.onetwo.common.utils.LangUtils;
import org.springframework.batch.item.database.orm.AbstractHibernateQueryProvider;

public class HibernateNativeQueryProvider<E> extends AbstractHibernateQueryProvider {

	private String sqlQuery;

	private Class<E> entityClass;
	
	private boolean resultTransformer = false;

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
			if(resultTransformer){
				//如果是游标的方式，不能获取列名，所以transformer是无效的。。。。。。。。。。
				if(LangUtils.isSimpleType(entityClass))
					query.setResultTransformer(new SingleColumnTransformer(entityClass));
				else
					query.setResultTransformer(new RowToBeanTransformer(entityClass));
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

	public void setEntityClass(Class<E> entityClass) {
		this.entityClass = entityClass;
	}

	public boolean isResultTransformer() {
		return resultTransformer;
	}

	public void setResultTransformer(boolean resultTransformer) {
		this.resultTransformer = resultTransformer;
	}

	
}
