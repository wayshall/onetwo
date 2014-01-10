package org.onetwo.plugins.batch;

import org.springframework.batch.item.database.HibernatePagingItemReader;
import org.springframework.util.Assert;

public class HibernateQueryPagingItemReader<E> extends HibernatePagingItemReader<E> {
	
	private boolean hql;
	private String sqlQueryString;
	private Class<E> entityClass; 

	private boolean resultTransformer = false;
	
	public HibernateQueryPagingItemReader(){
		setPageSize(1000);
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if(!isHql()){
			Assert.hasText(sqlQueryString, "queryString must be set!");
			Assert.notNull(entityClass, "entityClass must be set!");
			
			HibernateNativeQueryProvider<E> queryProvider = new HibernateNativeQueryProvider<E>();
			queryProvider.setSqlQuery(sqlQueryString);
			queryProvider.setResultClass(entityClass, resultTransformer);
			setQueryProvider(queryProvider);
		}
		super.afterPropertiesSet();
	}

	public boolean isHql() {
		return hql;
	}

	public void setHql(boolean hql) {
		this.hql = hql;
	}

	public void setQueryString(String queryString) {
		super.setQueryString(queryString);
		this.sqlQueryString = queryString;
	}

	public void setEntityClass(Class<E> entityClass) {
		this.entityClass = entityClass;
	}

	public void setResultTransformer(boolean resultTransformer) {
		this.resultTransformer = resultTransformer;
	}
	
	
}
