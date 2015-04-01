package org.onetwo.common.hibernate;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.NonUniqueResultException;
import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.db.BaseEntityManagerAdapter;
import org.onetwo.common.db.DataQuery;
import org.onetwo.common.db.EntityManagerProvider;
import org.onetwo.common.db.ExtQuery;
import org.onetwo.common.db.ExtQuery.K;
import org.onetwo.common.db.FileNamedQueryFactory;
import org.onetwo.common.db.ILogicDeleteEntity;
import org.onetwo.common.db.JFishQueryValue;
import org.onetwo.common.db.SelectExtQuery;
import org.onetwo.common.db.exception.NotUniqueResultException;
import org.onetwo.common.db.sql.SequenceNameManager;
import org.onetwo.common.db.sqlext.SQLSymbolManager;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@SuppressWarnings({"unchecked", "rawtypes"})
abstract public class AbstractEntityManager extends BaseEntityManagerAdapter implements BaseEntityManager, ApplicationContextAware {

//	protected SequenceNameManager sequenceNameManager;
	private SQLSymbolManager sqlSymbolManager;
	

	public AbstractEntityManager(){
	}
	
	abstract public SequenceNameManager getSequenceNameManager();

	/*public void setSequenceNameManager(SequenceNameManager sequenceNameManager) {
		this.sequenceNameManager = sequenceNameManager;
	}*/
	

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
			if(createIfNotExist && EntityManagerProvider.JPA.equals(this.getEntityManagerProvider()))//jpa not support
				createIfNotExist = false;
			if(!(e.getCause() instanceof SQLException) || !createIfNotExist)
				throw new ServiceException("createSequences error: " + e.getMessage(), e);
			
			SQLException se = (SQLException) e.getCause();
			if ("42000".equals(se.getSQLState())) {
				this.createSequence(sequenceName);
			}
		}
		return id;
	}
	
	protected void createSequence(Class entityClass){
		String seqName = getSequenceNameManager().getSequenceName(entityClass);
		this.createSequence(seqName);
	}
	
	protected void createSequence(String sequenceName){
		String sql = getSequenceNameManager().getSequenceSql(sequenceName);
		Long id = null;
			try {
				DataQuery dq = this.createSQLQuery(getSequenceNameManager().getCreateSequence(sequenceName), null);
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
	
	/*protected DataQuery createQuery(SelectExtQuery extQuery){
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
	}*/
	
	protected SelectExtQuery createSelectExtQuery(Class entityClass, Map<Object, Object> properties){
		SelectExtQuery q = getSQLSymbolManager().createSelectQuery(entityClass, "ent", properties);
		return q;
	}
	
	protected void checkEntityIdValid(Serializable id){
		if(!MyUtils.checkIdValid(id))
			throw new ServiceException("invalid id on load: " + id);
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
	
	public <T> void findPage(final Class<T> entityClass, final Page<T> page, Object... properties) {
		Map<Object, Object> params = null;
		if(properties!=null && properties.length>0)
			params = MyUtils.convertParamMap(properties);
		this.findPage(entityClass, page, params);
	}

	public <T> void findPage(final Class<T>  entityClass, final Page<T> page, Map<Object, Object> properties) {
		properties = prepareProperties(properties);

		if (Page.ASC.equals(page.getOrder()) && StringUtils.isNotBlank(page.getOrderBy())) {
			properties.put(ExtQuery.K.ASC, page.getOrderBy());
		}

		if (Page.DESC.equals(page.getOrder()) && StringUtils.isNotBlank(page.getOrderBy())) {
			properties.put(ExtQuery.K.DESC, page.getOrderBy());
		}

		SelectExtQuery extQuery = this.createSelectExtQuery(entityClass, properties);
		extQuery.build();
		/*if (page.isAutoCount()) {
			Long totalCount = (Long)this.findUnique(extQuery.getCountSql(), (Map)extQuery.getParamsValue().getValues());
			page.setTotalCount(totalCount);
			if(page.getTotalCount()<1){
				return ;
			}
		}
		List datalist = createQuery(extQuery).setPageParameter(page).getResultList();
		page.setResult(datalist);*/
		this.selectPage(page, extQuery);
	}
	
	
	/*protected void findPageByExtQuery(Page page, SelectExtQuery extQuery){
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
	}*/

	
	public <T> T findUnique(Class<T> entityClass, Map<Object, Object> properties) {
//		return findUnique(entityClass, properties, true);
		prepareProperties(properties);

		SelectExtQuery extQuery = createSelectExtQuery(entityClass, properties);
		extQuery.setMaxResults(1);//add: support unique
		extQuery.build();
		DataQuery q = createQuery(extQuery);
		try {
			return q.getSingleResult();
		} catch (NonUniqueResultException e) {
			throw new NotUniqueResultException(e.getMessage(), e);
		}
	}
	
	public <T> T findOne(Class<T> entityClass, Map<Object, Object> properties) {
//		return findUnique(entityClass, properties, true);
		prepareProperties(properties);

		SelectExtQuery extQuery = createSelectExtQuery(entityClass, properties);
		extQuery.setMaxResults(1);//add: support unique
		extQuery.build();
		DataQuery q = createQuery(extQuery);
		T entity = null;
		List<T> list = q.getResultList();
		if(LangUtils.hasElement(list))
			entity = list.get(0);
		return entity;
	}

	/*public <T> T findUnique(Class<T> entityClass, Map<Object, Object> properties, boolean tryAgain) {
		prepareProperties(properties);

		ExtQuery extQuery = createExtQuery(entityClass, properties);
		extQuery.setMaxResults(1);//add: support unique
		extQuery.build();
		DataQuery q = createQuery(extQuery);
		T entity = null;
		try {
			entity = (T)q.getSingleResult();
		} catch (Exception e) {
			logger.error(e);
			if(!tryAgain){
				return null;
//				throw new ServiceException("find unique error : " + e.getMessage(), e);
			}
		}
		if(tryAgain){
			List<T> list = q.getResultList();
			if(LangUtils.hasElement(list))
				entity = list.get(0);
		}
		return entity;
	}*/
	
	
	protected Map<Object, Object> prepareProperties(Map<Object, Object> properties) {
		if(properties==null)
			properties = new HashMap<Object, Object>();
		return properties;
	}
	
	public <T> List<T> findByProperties(Class<T> entityClass, Object... properties) {
		return this.findByProperties(entityClass, MyUtils.convertParamMap(properties));
	}

	public <T> List<T> findByProperties(Class<T> entityClass, Map<Object, Object> properties) {
		return select(entityClass, properties);
	}
	
	public <T> List<T> selectFields(Class<?> entityClass, Object[] selectFields, Object... properties){
		Map<Object, Object> params = LangUtils.newHashMap();
		CUtils.arrayIntoMap(params, properties);
		params.put(K.SELECT, selectFields);
		return this.select(entityClass, params);
	}

	/****
	 * 与selectFields唯一不同的地方是自动把entityClass作为返回类型
	 * @param entityClass
	 * @param selectFields
	 * @param properties
	 * @return
	 */
	public <T> List<T> selectFieldsToEntity(Class<?> entityClass, Object[] selectFields, Object... properties){
		Object[] selectFieldsWithType = ArrayUtils.add(selectFields, 0, entityClass);
		return selectFields(entityClass, selectFieldsWithType, properties);
	}
	
	public <T> List<T> select(Class<?> entityClass, Map<Object, Object> properties) {
		prepareProperties(properties);

		SelectExtQuery extQuery = this.createSelectExtQuery(entityClass, properties);
		extQuery.build();
		return createQuery(extQuery).getResultList();
	}
	
	public <T> List<T> findByExample(Class entityClass, Object obj){
		Map properties = CUtils.bean2Map(obj);
		return this.findByProperties(entityClass, properties);
	}
	
	public <T> void findPageByExample(Class<T> entityClass, Page<T> page, Object obj){
		Map properties = CUtils.bean2Map(obj);
		this.findPage(entityClass, page, properties);
	}

	public <T> List<T> findAll(Class<T> entityClass){
		/*StringBuilder sqlStr = new StringBuilder(); 
		sqlStr.append("select ent from ").append(entityClass.getSimpleName()).append(" ent");*/
		return findByProperties(entityClass);
	}

	public Number countRecord(Class entityClass, Map<Object, Object> properties) {
		SelectExtQuery extQuery = this.createSelectExtQuery(entityClass, properties);
		extQuery.build();
		Number count = (Number)this.findUnique(extQuery.getCountSql(), (Map)extQuery.getParamsValue().getValues());
		if(count==null)
			count = 0;
		return count;
	}

	public Number countRecord(Class entityClass, Object... params) {
		Map<Object, Object> properties = MyUtils.convertParamMap(params);
		return this.countRecord(entityClass, properties);
	}

	public EntityManagerProvider getEntityManagerProvider(){
		return EntityManagerProvider.JPA;
	}
	
	public SQLSymbolManager getSQLSymbolManager(){
		return sqlSymbolManager;
	}
	

	public <T> List<T> findList(JFishQueryValue queryValue){
//		throw new UnsupportedOperationException();
		DataQuery dq = this.createQuery(queryValue.getSql());
		return dq.getResultList();
	}
	
	public <T> T findUnique(JFishQueryValue queryValue){
		throw new UnsupportedOperationException();
	}
	
	public <T> void findPage(Page<T> page, JFishQueryValue squery){
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T getRawManagerObject(Class<T> rawClass) {
		return rawClass.cast(getRawManagerObject());
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		/*SQLSymbolManager symbolManager = SpringUtils.getBean(applicationContext, SQLSymbolManager.class);
		if(symbolManager==null){
			symbolManager = SQLSymbolManagerFactory.getInstance().get(this.getEntityManagerProvider());
		}
		this.sqlSymbolManager = symbolManager;*/
	}

	public void setSqlSymbolManager(SQLSymbolManager sqlSymbolManager) {
		this.sqlSymbolManager = sqlSymbolManager;
	}

	@Override
	public FileNamedQueryFactory getFileNamedQueryFactory() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> Collection<T> saves(Collection<T> entities) {
		for(T entity : entities){
			save(entity);
		}
		return entities;
	}
	
	
}
