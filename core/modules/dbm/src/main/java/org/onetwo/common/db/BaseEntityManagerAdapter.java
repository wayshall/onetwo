package org.onetwo.common.db;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.onetwo.common.db.builder.QueryBuilder;
import org.onetwo.common.db.filequery.SqlParamterPostfixFunctionRegistry;
import org.onetwo.common.db.sql.SequenceNameManager;
import org.onetwo.common.db.sqlext.SelectExtQuery;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.slf4j.Logger;

@SuppressWarnings("unchecked")
public abstract class BaseEntityManagerAdapter implements InnerBaseEntityManager {

	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());

	/*@Override
	public DataQuery createMappingSQLQuery(String sqlString, String resultSetMapping) {
		throw new UnsupportedOperationException("not support operation!");
	}*/
	
	public SqlParamterPostfixFunctionRegistry getSqlParamterPostfixFunctionRegistry(){
		throw new UnsupportedOperationException("no SqlParamterPostfixFunctionRegistry found!");
	}

	abstract public SequenceNameManager getSequenceNameManager();
		
	protected void createSequence(Class<?> entityClass){
		String seqName = getSequenceNameManager().getSequenceName(entityClass);
		this.createSequence(seqName);
	}
	

	/*****
	 * 创建序列，for oracle
	 * @param sequenceName
	 */
	protected void createSequence(String sequenceName){
		String sql = getSequenceNameManager().getSequenceSql(sequenceName);
		Long id = null;
			try {
				DbmQueryWrapper dq = this.createSQLQuery(getSequenceNameManager().getCreateSequence(sequenceName), null);
				dq.executeUpdate();
				
				dq = this.createSQLQuery(sql, null);
				id = ((Number)dq.getSingleResult()).longValue();
			} catch (Exception ne) {
				ne.printStackTrace();
				throw new ServiceException("createSequences error: " + ne.getMessage(), ne);
			}
			if (id == null)
				throw new ServiceException("createSequences error. ");
	}
	
	

	public Number countRecord(Class<?> entityClass, Object... params) {
		return countRecordByProperties(entityClass, CUtils.asLinkedMap(params));
	}
	public void delete(ILogicDeleteEntity entity){
		entity.deleted();
		this.save(entity);
	}

	public <T extends ILogicDeleteEntity> T deleteById(Class<T> entityClass, Serializable id){
		Object entity = this.findById(entityClass, id);
		if(entity==null)
			return null;
		if(!ILogicDeleteEntity.class.isAssignableFrom(entity.getClass())){
			throw new ServiceException("实体不支持逻辑删除，请实现相关接口！");
		}
		T logicDeleteEntity = (T) entity;
		logicDeleteEntity.deleted();
		this.save(logicDeleteEntity);
		return logicDeleteEntity;
	}
	
	public <T> List<T> findList(QueryBuilder squery) {
		return findListByProperties((Class<T>)squery.getEntityClass(), squery.getParams());
	}

	@Override
	public <T> void findPage(final Page<T> page, QueryBuilder squery){
		findPageByProperties((Class<T>)squery.getEntityClass(), page, squery.getParams());
	}
	
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
		return (T)createQuery((SelectExtQuery)extQuery).getSingleResult();
	}

	
	protected <T> T findUnique(final String sql, final Map<String, Object> values){
		T entity = null;
		try {
			entity = (T)this.createQuery(sql, values).getSingleResult();
		}catch(Exception e){
			throw new BaseException("find the unique result error : " + sql, e);
		}
		return entity;
	}

	public <T> void selectPage(Page<T> page, SelectExtQuery extQuery){
		if (page.isAutoCount()) {
//			Long totalCount = (Long)this.findUnique(extQuery.getCountSql(), (Map)extQuery.getParamsValue().getValues());
			Long totalCount = 0l;
			Number countNumber = (Number)this.findUnique(extQuery.getCountSql(), extQuery.getParamsValue().asMap());
			totalCount = countNumber.longValue();
			page.setTotalCount(totalCount);
			if(page.getTotalCount()<1){
				return ;
			}
		}
		List<T> datalist = createQuery(extQuery).setPageParameter(page).getResultList();
		if(!page.isAutoCount()){
			page.setTotalCount(datalist.size());
		}
		page.setResult(datalist);
	}
	
	protected DbmQueryWrapper createQuery(SelectExtQuery extQuery){
		DbmQueryWrapper q = null;
		q = this.createQuery(extQuery.getSql(), extQuery.getParamsValue().asMap());
		if(extQuery.needSetRange()){
			q.setLimited(extQuery.getFirstResult(), extQuery.getMaxResults());
		}
		q.setQueryConfig(extQuery.getQueryConfig());
		return q;
	}

	/*@Override
	public <T> List<T> select(Class<?> entityClass, Map<Object, Object> properties) {
//		throw new UnsupportedOperationException();
		return findByProperties(entityClass, properties);
	}*/


	public <T> T findUnique(QueryBuilder squery) {
		return findUniqueByProperties((Class<T>)squery.getEntityClass(), squery.getParams());
	}

	/****
	 *  查找唯一记录，如果找不到返回null，如果多于一条记录，抛出异常。
	 */
	public <T> T findUnique(Class<T> entityClass, Object... properties) {
		return this.findUniqueByProperties(entityClass, CUtils.asMap(properties));
	}
	
	public <T> T findOne(Class<T> entityClass, Object... properties) {
		return this.findOneByProperties(entityClass, CUtils.asMap(properties));
	}

	public <T> T findOneByProperties(Class<T> entityClass, Map<Object, Object> properties) {
		/*try {
			return findUniqueByProperties(entityClass, properties);
		} catch (NotUniqueResultException e) {
			return null;//return null if error
		}*/
		List<T> list = findListByProperties(entityClass, properties);
		return list.isEmpty()?null:list.get(0);
	}

	@Override
	public <T> Collection<T> saves(Collection<T> entities) {
		for(T entity : entities){
			save(entity);
		}
		return entities;
	}

	
	@Override
	public <T> Collection<T> removeByIds(Class<T> entityClass, Serializable[] ids) {
		if(LangUtils.isEmpty(ids))
			throw new BaseException("error args: " +LangUtils.toString(ids));
		
		Collection<T> removeds = LangUtils.newArrayList(ids.length);
		for(Serializable id : ids){
			T e = removeById(entityClass, id);
			removeds.add(e);
		}
		return removeds;
	}

	@Override
	public <T> void removes(Collection<T> entities) {
		if(entities==null)
			return ;
		for(Object entity : entities){
			this.remove(entity);
		}
	}

	@Override
	public <T> T narrowAs(Class<T> entityManagerClass) {
		return entityManagerClass.cast(this);
	}
	
	
}
