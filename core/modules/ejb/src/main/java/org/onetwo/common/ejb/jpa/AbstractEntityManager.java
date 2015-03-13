package org.onetwo.common.ejb.jpa;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.onetwo.common.db.BaseEntityManagerAdapter;
import org.onetwo.common.db.DataQuery;
import org.onetwo.common.db.EntityManagerProvider;
import org.onetwo.common.db.ExtQuery;
import org.onetwo.common.db.ILogicDeleteEntity;
import org.onetwo.common.db.JFishQueryValue;
import org.onetwo.common.db.QueryBuilder;
import org.onetwo.common.db.SelectExtQuery;
import org.onetwo.common.db.event.DbEventListeners;
import org.onetwo.common.db.event.EventSource;
import org.onetwo.common.db.sql.SequenceNameManager;
import org.onetwo.common.db.sqlext.SQLSymbolManager;
import org.onetwo.common.db.sqlext.SQLSymbolManagerFactory;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.map.M;
import org.slf4j.Logger;

@SuppressWarnings({"unchecked", "rawtypes"})
abstract public class AbstractEntityManager extends BaseEntityManagerAdapter implements EventSource {

	protected Logger logger = JFishLoggerFactory.getLogger(this.getClass());
//	protected SequenceNameManager sequenceNameManager;
	

	private DbEventListeners dbEventListeners;
	
	public AbstractEntityManager(){
		this.init();
	}
	
	public void init(){
		this.dbEventListeners = newDbEventListeners();
	}
	
	public DbEventListeners getDbEventListeners() {
		return dbEventListeners;
	}

	protected DbEventListeners newDbEventListeners() {
		return new DbEventListeners();
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
	
	protected SelectExtQuery createExtQuery(Class entityClass, Map<Object, Object> properties){
		SelectExtQuery q = getSQLSymbolManager().createSelectQuery(entityClass, "ent", properties);
		return q;
	}
	
	protected void checkEntityIdValid(Serializable id){
		if(!MyUtils.checkIdValid(id))
			throw new ServiceException("invalid id : " + id);
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
			throw new ServiceException("实体不支持删除！");
		}
		T logicDeleteEntity = (T) entity;
		logicDeleteEntity.deleted();
		this.save(logicDeleteEntity);
		return logicDeleteEntity;
	}
	
	public void findPage(final Class entityClass, final Page page, Object... properties) {
		Map<Object, Object> params = null;
		if(properties!=null && properties.length>0)
			params = MyUtils.convertParamMap(properties);
		this.findPage(entityClass, page, params);
	}
	

	public <T> void findPage(final Class<T> entityClass, final Page<T> page, Map<Object, Object> properties) {
		properties = prepareProperties(properties);

		if (Page.ASC.equals(page.getOrder()) && StringUtils.isNotBlank(page.getOrderBy())) {
			properties.put(ExtQuery.K.ASC, page.getOrderBy());
		}

		if (Page.DESC.equals(page.getOrder()) && StringUtils.isNotBlank(page.getOrderBy())) {
			properties.put(ExtQuery.K.DESC, page.getOrderBy());
		}

		SelectExtQuery extQuery = this.createExtQuery(entityClass, properties);
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
		this.findPageByExtQuery(page, extQuery);
	}
	
	protected void findPageByExtQuery(Page page, SelectExtQuery extQuery){
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
//		return findUnique(entityClass, properties, true);
		prepareProperties(properties);

		SelectExtQuery extQuery = createExtQuery(entityClass, properties);
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
	

	public <T> T findUnique(final String sql, final Object... values){
		T entity = null;
		try {
			entity = (T)this.createQuery(sql, values).getSingleResult();
		} catch (Exception e) {
			throw new BaseException("find the unique result error : " + sql, e);
		}
		return entity;
	}
	
	
	
	public <T> T findUnique(final String sql, final Map<String, Object> values){
		T entity = null;
		try {
			entity = (T)this.createQuery(sql, values).getSingleResult();
		}catch(Exception e){
			throw new BaseException("find the unique result error : " + sql, e);
		}
		return entity;
	}
	
	protected Map<Object, Object> prepareProperties(Map<Object, Object> properties) {
		if(properties==null)
			properties = new HashMap<Object, Object>();
		return properties;
	}
	
	public <T> List<T> findByProperties(Class<T> entityClass, Object... properties) {
		return this.findByProperties(entityClass, MyUtils.convertParamMap(properties));
	}

	public <T> List<T> findList(QueryBuilder squery) {
		return findByProperties((Class<T>)squery.getEntityClass(), squery.getParams());
	}

	public <T> List<T> findByProperties(Class<T> entityClass, Map<Object, Object> properties) {
		prepareProperties(properties);

		SelectExtQuery extQuery = this.createExtQuery(entityClass, properties);
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
		/*StringBuilder sqlStr = new StringBuilder(); 
		sqlStr.append("select ent from ").append(entityClass.getSimpleName()).append(" ent");
		return createQuery(sqlStr.toString()).getResultList();*/
		SelectExtQuery extQuery = createExtQuery(entityClass, null);
		extQuery.build();
		return this.createQuery(extQuery).getResultList();
	}

	public Number countRecord(Class entityClass, Map<Object, Object> properties) {
		SelectExtQuery extQuery = this.createExtQuery(entityClass, properties);
		extQuery.build();
		return (Number) this.findUnique(extQuery.getCountSql(), (Map)extQuery.getParamsValue().getValues());
	}

	public Number countRecord(Class entityClass, Object... params) {
		Map<Object, Object> properties = MyUtils.convertParamMap(params);
		return this.countRecord(entityClass, properties);
	}

	public EntityManagerProvider getEntityManagerProvider(){
		return EntityManagerProvider.JPA;
	}
	
	public SQLSymbolManager getSQLSymbolManager(){
		return SQLSymbolManagerFactory.getInstance().get(this.getEntityManagerProvider());
	}
	

	public <T> List<T> findList(JFishQueryValue queryValue){
		throw new UnsupportedOperationException();
	}
	
	public <T> T findUnique(JFishQueryValue queryValue){
		throw new UnsupportedOperationException();
	}
	
	public <T> void findPage(Page<T> page, JFishQueryValue squery){
		throw new UnsupportedOperationException();
	}
}
