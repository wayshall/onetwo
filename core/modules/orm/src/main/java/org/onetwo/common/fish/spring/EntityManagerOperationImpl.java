package org.onetwo.common.fish.spring;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.onetwo.common.db.DataQuery;
import org.onetwo.common.db.ExtQuery;
import org.onetwo.common.db.ILogicDeleteEntity;
import org.onetwo.common.db.QueryBuilder;
import org.onetwo.common.db.sql.SequenceNameManager;
import org.onetwo.common.db.sqlext.SQLSymbolManager;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.fish.JFishDataQuery;
import org.onetwo.common.fish.JFishQuery;
import org.onetwo.common.fish.exception.JFishOrmException;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.map.M;

@SuppressWarnings({"unchecked", "rawtypes"})
public class EntityManagerOperationImpl {

	protected final Logger logger = Logger.getLogger(this.getClass());

	private JFishDaoImplementor jfishDao;
	private SequenceNameManager sequenceNameManager;


	public EntityManagerOperationImpl(JFishDaoImplementor jfishDao) {
		this(jfishDao, null);
	}
	
	public EntityManagerOperationImpl(JFishDaoImplementor jfishDao, SequenceNameManager sequenceNameManager) {
		super();
		this.jfishDao = jfishDao;
		this.sequenceNameManager = sequenceNameManager;
	}

	public SequenceNameManager getSequenceNameManager(){
		if(sequenceNameManager==null)
			throw new JFishOrmException("no sequenceNameManager");
		return sequenceNameManager;
	}


	
	public DataQuery createSQLQuery(String sqlString, Class entityClass) {
		JFishQuery jq = jfishDao.createJFishQuery(sqlString, entityClass);
		DataQuery query = new JFishDataQuery(jq);
		return query;
	}


	
	public Long getSequences(Class entityClass, boolean createIfNotExist) {
		String seqName = getSequenceNameManager().getSequenceName(entityClass);
		return getSequences(seqName, createIfNotExist);
	}

	
	public Long getSequences(String sequenceName, boolean createIfNotExist) {
		String sql = getSequenceNameManager().getSequenceSql(sequenceName);
		Long id = null;
		try {
			DataQuery dq = this.createSQLQuery(sql, null);
			id = ((Number)dq.getSingleResult()).longValue();
//			logger.info("createSequences id : "+id);
		} catch (Exception e) {
			if(!(e.getCause() instanceof SQLException) || !createIfNotExist)
				throw new ServiceException("createSequences error: " + e.getMessage(), e);
			
			SQLException se = (SQLException) e.getCause();
			if ("42000".equals(se.getSQLState())) {
				try {
					DataQuery dq = this.createSQLQuery(getSequenceNameManager().getCreateSequence(sequenceName), null);
					dq.executeUpdate();
					
					dq = this.createSQLQuery(sql, null);
					id = ((Number)dq.getSingleResult()).longValue();
				} catch (Exception ne) {
					ne.printStackTrace();
					throw new ServiceException("createSequences error: " + e.getMessage(), e);
				}
				if (id == null)
					throw new ServiceException("createSequences error: " + e.getMessage(), e);
			}
		}
		return id;
	}

	
	public DataQuery createQuery(String sqlString) {
		return this.createSQLQuery(sqlString, null);
	}
	
	
	public DataQuery createQuery(String sql, Map<String, Object> values){
		DataQuery q = createQuery(sql);
		q.setParameters(values);
		return q;
	}
 
	protected DataQuery createQuery(String sql, Object[] values){
		DataQuery q = createQuery(sql);
		q.setParameters(values);
		return q;
	}
	
	
	public DataQuery createQuery(ExtQuery extQuery){
		DataQuery q = null;
		if(extQuery.isSqlQuery()){
			q = this.createSQLQuery(extQuery.getSql(), extQuery.getEntityClass());
			q.setParameters((List)extQuery.getParamsValue().getValues());
		}else{
			q = this.createQuery(extQuery.getSql(), (Map)extQuery.getParamsValue().getValues());
		}
		JFishQuery jq = q.getRawQuery();
		jq.setResultClass(extQuery.getEntityClass());
		if(extQuery.needSetRange()){
			q.setLimited(extQuery.getFirstResult(), extQuery.getMaxResults());
		}
		q.setQueryConfig(extQuery.getQueryConfig());
		return q;
	}
	
	protected ExtQuery createExtQuery(Class entityClass, Map<Object, Object> properties){
		ExtQuery q = getSQLSymbolManager().createQuery(entityClass, "ent", properties);
		return q;
	}
	
	protected void checkEntityIdValid(Serializable id){
		if(!MyUtils.checkIdValid(id))
			throw new ServiceException("invalid id : " + id);
	}

	
	
	public void findPage(final Class entityClass, final Page page, Object... properties) {
		Map<Object, Object> params = null;
		if(properties!=null && properties.length>0)
			params = MyUtils.convertParamMap(properties);
		this.findPage(entityClass, page, params);
	}
	
	
	public void findPage(final Page page, QueryBuilder squery){
		findPage(squery.getEntityClass(), page, squery.getParams());
	}

	
	public void findPage(final Class entityClass, final Page page, Map<Object, Object> properties) {
		properties = prepareProperties(properties);

		if (Page.ASC.equals(page.getOrder()) && StringUtils.isNotBlank(page.getOrderBy())) {
			properties.put(ExtQuery.K.ASC, page.getOrderBy());
		}

		if (Page.DESC.equals(page.getOrder()) && StringUtils.isNotBlank(page.getOrderBy())) {
			properties.put(ExtQuery.K.DESC, page.getOrderBy());
		}

		ExtQuery extQuery = this.createExtQuery(entityClass, properties);
		extQuery.build();

		this.findPageByExtQuery(page, extQuery);
	}
	
	protected void findPageByExtQuery(Page page, ExtQuery extQuery){
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
		page.setResult(datalist);
	}

	
	public void removeList(List entities) {
		if(LangUtils.isEmpty(entities))
			return ;
		getJfishDao().delete(entities);
	}

	
	public int removeAll(Class<?> entityClass) {
		return getJfishDao().deleteAll(entityClass);
	}
	

	
	public <T> T findUnique(QueryBuilder squery) {
		return findUnique((Class<T>)squery.getEntityClass(), squery.getParams());
	}
	
	
	public <T> T findUnique(Class<T> entityClass, boolean tryTheBest, Object... properties) {
		return this.findUnique(entityClass, MyUtils.convertParamMap(properties), tryTheBest);
	}
	
	public <T> T findUnique(Class<T> entityClass, Object... properties) {
		return this.findUnique(entityClass, MyUtils.convertParamMap(properties));
	}
	
	
	public <T> T findUnique(Class<T> entityClass, Map<Object, Object> properties) {
		prepareProperties(properties);

		ExtQuery extQuery = createExtQuery(entityClass, properties);
		extQuery.setMaxResults(1);//add: support unique
		extQuery.build();
		DataQuery q = createQuery(extQuery);
		T entity = null;
		List<T> list = q.getResultList();
		if(LangUtils.hasElement(list)){
			if(list.size()>1){
				logger.warn(list.size() + " entity["+entityClass+"] found when findUnique");
			}
			entity = list.get(0);
		}else{
//			logger.warn("no entity["+entityClass+"] found when findUnique");
		}
		return entity;
	}

	
	public <T> T findUnique(final String sql, final Object... values){
		T entity = null;
		try {
			entity = (T)this.createQuery(sql, values).getSingleResult();
		} catch (Exception e) {
			logger.error(e);
			throw new BaseException("find the unique result error : " + sql, e);
		}
		return entity;
	}
	
	
	
	
	public <T> T findUnique(final String sql, final Map<String, Object> values){
		T entity = null;
		try {
			entity = (T)this.createQuery(sql, values).getSingleResult();
		}catch(Exception e){
			logger.error(e);
			throw new BaseException("find the unique result error : " + sql, e);
		}
		return entity;
	}
	
	protected Map<Object, Object> prepareProperties(Map<Object, Object> properties) {
		if(properties==null)
			properties = new HashMap<Object, Object>();
		return properties;
	}
	
	
	public <T> List<T> findByProperties(Class entityClass, Object... properties) {
		return this.findByProperties(entityClass, MyUtils.convertParamMap(properties));
	}

	
	public <T> List<T> findList(QueryBuilder squery) {
		return findByProperties(squery.getEntityClass(), squery.getParams());
	}

	
	public <T> List<T> findByProperties(Class entityClass, Map<Object, Object> properties) {
		prepareProperties(properties);

		ExtQuery extQuery = this.createExtQuery(entityClass, properties);
		extQuery.build();
		return createQuery(extQuery).getResultList();
	}
	
	
	public <T> List<T> findByExample(Class entityClass, Object obj){
		Map properties = M.bean2Map(obj);
		return this.findByProperties(entityClass, properties);
	}
	
	
	public <T> void findPageByExample(Class<T> entityClass, Page<T> page, Object obj){
		Map properties = M.bean2Map(obj);
		this.findPage(entityClass, page, properties);
	}

	
	public <T> List<T> findAll(Class<T> entityClass){
		StringBuilder sqlStr = new StringBuilder(); 
		sqlStr.append("select ent from ").append(entityClass.getSimpleName()).append(" ent");
		return createQuery(sqlStr.toString()).getResultList();
	}

	
	public Number countRecord(Class entityClass, Map<Object, Object> properties) {
		ExtQuery extQuery = this.createExtQuery(entityClass, properties);
		extQuery.build();
		return (Number) this.findUnique(extQuery.getCountSql(), (Map)extQuery.getParamsValue().getValues());
	}

	
	public void delete(ILogicDeleteEntity entity){
		entity.deleted();
		getJfishDao().save(entity);
	}

	
	public <T extends ILogicDeleteEntity> T deleteById(Class<? extends ILogicDeleteEntity> entityClass, Serializable id){
		Object entity = getJfishDao().findById(entityClass, id);
		if(entity==null)
			return null;
		if(!ILogicDeleteEntity.class.isAssignableFrom(entity.getClass())){
			throw new ServiceException("实体不支持删除！");
		}
		T logicDeleteEntity = (T) entity;
		logicDeleteEntity.deleted();
		getJfishDao().save(logicDeleteEntity);
		return logicDeleteEntity;
	}

	
	public Number countRecord(Class entityClass, Object... params) {
		Map<Object, Object> properties = MyUtils.convertParamMap(params);
		return this.countRecord(entityClass, properties);
	}
	
	protected SQLSymbolManager getSQLSymbolManager(){
		return getJfishDao().getSqlSymbolManager();
	}

	public JFishDaoImplementor getJfishDao() {
		return jfishDao;
	}
}
