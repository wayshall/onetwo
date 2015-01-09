package org.onetwo.common.db;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.onetwo.common.db.exception.NotUniqueResultException;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.utils.Page;

public abstract class BaseEntityManagerAdapter implements InnerBaseEntityManager {

	protected final Logger logger = Logger.getLogger(this.getClass());
	
	@Override
	public <T> List<T> selectFields(Class<?> entityClass, Object[] selectFields, Object... properties) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> List<T> selectFieldsToEntity(Class<?> entityClass, Object[] selectFields, Object... properties){
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> List<T> select(SelectExtQuery extQuery) {
		extQuery.build();
		return createQuery(extQuery).getResultList();
	}

	@Override
	public <T> T selectOne(SelectExtQuery extQuery) {
		List<T> list = select(extQuery);
		T entity = null;
		if(LangUtils.hasElement(list))
			entity = list.get(0);
		return entity;
	}

	@Override
	public <T> T selectUnique(SelectExtQuery extQuery) {
		extQuery.build();
		return createQuery((SelectExtQuery)extQuery).getSingleResult();
	}

	
	protected <T> T findUnique(final String sql, final Map<String, Object> values){
		T entity = null;
		try {
			entity = (T)this.createQuery(sql, values).getSingleResult();
		}catch(Exception e){
			logger.error(e);
			throw new BaseException("find the unique result error : " + sql, e);
		}
		return entity;
	}

	public void selectPage(Page<?> page, SelectExtQuery extQuery){
		if (page.isAutoCount()) {
//			Long totalCount = (Long)this.findUnique(extQuery.getCountSql(), (Map)extQuery.getParamsValue().getValues());
			Long totalCount = 0l;
			if(extQuery.isSqlQuery()){
				DataQuery countQuery = this.createSQLQuery(extQuery.getCountSql(), Long.class);
				countQuery.setParameters((List)extQuery.getParamsValue().getValues());
				totalCount = (Long)countQuery.getSingleResult();
			}else{
				Number countNumber = (Number)this.findUnique(extQuery.getCountSql(), (Map)extQuery.getParamsValue().getValues());
				totalCount = countNumber.longValue();
			}
			page.setTotalCount(totalCount);
			if(page.getTotalCount()<1){
				return ;
			}
		}
		List datalist = createQuery(extQuery).setPageParameter(page).getResultList();
		if(!page.isAutoCount()){
			page.setTotalCount(datalist.size());
		}
		page.setResult(datalist);
	}
	
	protected DataQuery createQuery(SelectExtQuery extQuery){
		DataQuery q = null;
		if(extQuery.isSqlQuery()){
			q = this.createSQLQuery(extQuery.getSql(), extQuery.getEntityClass());
			q.setParameters((List)extQuery.getParamsValue().getValues());
		}else{
			q = this.createQuery(extQuery.getSql(), (Map)extQuery.getParamsValue().getValues());
		}
		if(extQuery.needSetRange()){
			q.setLimited(extQuery.getFirstResult(), extQuery.getMaxResults());
		}
		q.setQueryConfig(extQuery.getQueryConfig());
		return q;
	}

	@Override
	public <T> List<T> select(Class<?> entityClass, Map<Object, Object> properties) {
		throw new UnsupportedOperationException();
	}


	public <T> T findUnique(QueryBuilder squery) {
		return findUnique((Class<T>)squery.getEntityClass(), squery.getParams());
	}

	public <T> T findUnique(Class<T> entityClass, Object... properties) {
		return this.findUnique(entityClass, MyUtils.convertParamMap(properties));
	}
	
	public <T> T findOne(Class<T> entityClass, Object... properties) {
		return this.findOne(entityClass, MyUtils.convertParamMap(properties));
	}

	public <T> T findOne(Class<T> entityClass, Map<Object, Object> properties) {
		try {
			return findUnique(entityClass, properties);
		} catch (NotUniqueResultException e) {
			return null;
		}
	}

}
