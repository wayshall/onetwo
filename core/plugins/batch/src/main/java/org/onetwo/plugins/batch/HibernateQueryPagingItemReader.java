package org.onetwo.plugins.batch;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.hibernate.SessionFactory;
import org.onetwo.common.log.MyLoggerFactory;
import org.slf4j.Logger;
import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.batch.item.database.HibernateItemReaderHelper;
import org.springframework.batch.item.database.HibernatePagingItemReader;
import org.springframework.batch.item.database.orm.HibernateQueryProvider;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

public class HibernateQueryPagingItemReader<E> extends AbstractPagingItemReader<E> {
	
	private static final Logger logger = MyLoggerFactory.getLogger(HibernateQueryPagingItemReader.class);
	
	private HibernateItemReaderHelper<E> helper = new HibernateItemReaderHelper<E>();
	private Map<String, Object> parameterValues;
	private int fetchSize;
	
	private boolean hql;
	private String sqlQueryString;
	private Class<E> entityClass; 
	private boolean resultTransformer = false;
	private boolean debug;
	
	public HibernateQueryPagingItemReader(){
		setName(ClassUtils.getShortName(HibernatePagingItemReader.class));
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
		Assert.state(fetchSize >= 0, "fetchSize must not be negative");
		helper.afterPropertiesSet();
	}


	public boolean isHql() {
		return hql;
	}

	public void setHql(boolean hql) {
		this.hql = hql;
	}

	public void setQueryString(String queryString) {
		this.sqlQueryString = queryString;
		helper.setQueryString(queryString);
	}

	public void setEntityClass(Class<E> entityClass) {
		this.entityClass = entityClass;
	}

	public void setResultTransformer(boolean resultTransformer) {
		this.resultTransformer = resultTransformer;
	}
	
	public void setParameterValues(Map<String, Object> parameterValues) {
		this.parameterValues = parameterValues;
	}

	public void setQueryName(String queryName) {
		helper.setQueryName(queryName);
	}

	public void setFetchSize(int fetchSize) {
		this.fetchSize = fetchSize;
	}

	public void setQueryProvider(HibernateQueryProvider queryProvider) {
		helper.setQueryProvider(queryProvider);
	}


	public void setSessionFactory(SessionFactory sessionFactory) {
		helper.setSessionFactory(sessionFactory);
	}

	public void setUseStatelessSession(boolean useStatelessSession) {
		helper.setUseStatelessSession(useStatelessSession);
	}


	@Override
	protected void doOpen() throws Exception {
		super.doOpen();
	}

	@Override
	protected void doReadPage() {

		if (results == null) {
			results = new CopyOnWriteArrayList<E>();
		}
		else {
			results.clear();
		}

		Collection<? extends E> datas = helper.readPage(getPage(), getPageSize(), fetchSize, parameterValues);
		results.addAll(datas);

		if(debug){
			logger.info("read page: {}, pageSize: {}", getPage(), getPageSize());
			int index = 0;
			for(Object data : datas){
				logger.info("data[{}]: {}", index, data);
				index++;
			}
		}

	}

	@Override
	protected void doJumpToPage(int itemIndex) {
	}

	@Override
	protected void doClose() throws Exception {
		helper.close();
		super.doClose();
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	
}
