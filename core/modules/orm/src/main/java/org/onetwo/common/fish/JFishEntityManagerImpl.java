package org.onetwo.common.fish;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.onetwo.common.db.BaseEntityManagerAdapter;
import org.onetwo.common.db.DataQuery;
import org.onetwo.common.db.EntityManagerProvider;
import org.onetwo.common.db.FileNamedQueryFactory;
import org.onetwo.common.db.FileNamedQueryFactoryListener;
import org.onetwo.common.db.ILogicDeleteEntity;
import org.onetwo.common.db.JFishQueryValue;
import org.onetwo.common.db.SelectExtQuery;
import org.onetwo.common.db.sql.SequenceNameManager;
import org.onetwo.common.db.sqlext.SQLSymbolManager;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.fish.exception.JFishEntityNotFoundException;
import org.onetwo.common.fish.spring.JFishDao;
import org.onetwo.common.fish.spring.JFishDaoImplementor;
import org.onetwo.common.fish.spring.JFishNamedFileQueryManagerImpl;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.map.M;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.RowMapper;

@SuppressWarnings("rawtypes")
public class JFishEntityManagerImpl extends BaseEntityManagerAdapter implements JFishEntityManager, ApplicationContextAware, InitializingBean , DisposableBean {

	private final Logger logger = MyLoggerFactory.getLogger(this.getClass());
	
//	private SQLSymbolManager SQLSymbolManager;

	private JFishDaoImplementor jfishDao;
//	private EntityManagerOperationImpl entityManagerWraper;
//	private JFishList<JFishEntityManagerLifeCycleListener> emListeners;
	private ApplicationContext applicationContext;
	
	private FileNamedQueryFactory fileNamedQueryFactory;
	private boolean watchSqlFile = false;
	
	public JFishEntityManagerImpl(){
	}
	
	
	@Override
	public void update(Object entity) {
		this.jfishDao.update(entity);
	}


	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}


	public void afterPropertiesSet() throws Exception{
		FileNamedQueryFactoryListener listener = SpringUtils.getBean(applicationContext, FileNamedQueryFactoryListener.class);
		this.fileNamedQueryFactory = new JFishNamedFileQueryManagerImpl(this, jfishDao.getDialect().getDbmeta().getDb(), watchSqlFile, listener);
		this.fileNamedQueryFactory.initQeuryFactory(this);
		
//		this.entityManagerWraper = jfishDao.getEntityManagerWraper();
		
		/*List<JFishEntityManagerLifeCycleListener> jlisteners = SpringUtils.getBeans(applicationContext, JFishEntityManagerLifeCycleListener.class);
		this.emListeners = JFishList.wrapObject(jlisteners);
		
		this.emListeners.each(new NoIndexIt<JFishEntityManagerLifeCycleListener>() {

			@Override
			protected void doIt(JFishEntityManagerLifeCycleListener element) throws Exception {
				element.onInit(JFishEntityManagerImpl.this);
			}
			
		});*/
	}

	@Override
	public void destroy() throws Exception {
		/*this.emListeners.each(new NoIndexIt<JFishEntityManagerLifeCycleListener>() {

			@Override
			protected void doIt(JFishEntityManagerLifeCycleListener element) throws Exception {
				element.onDestroy(JFishEntityManagerImpl.this);
			}
			
		});*/
	}
	
	public <T> List<T> findAll(Class<T> entityClass){
		return jfishDao.findAll(entityClass);
	}
	
	@Override
	public <T> T load(Class<T> entityClass, Serializable id){
		T entity = findById(entityClass, id);
//		Assert.notNull(entity, "can not load the object from db : " + id);
		if(entity==null){
			throw new JFishEntityNotFoundException("找不到数据：" + id);
		}
		return entity;
	}

	@Override
	public <T> T findById(Class<T> entityClass, Serializable id) {
		return getJfishDao().findById(entityClass, id);
	}

	@Override
	public <T> T save(T entity) {
		getJfishDao().save(entity);
		return entity;
	}

	@Override
	public void persist(Object entity) {
		getJfishDao().insert(entity);
	}

	@Override
	public void remove(Object entity) {
		getJfishDao().delete(entity);
	}

	@Override
	public int removeAll(Class<?> entityClass) {
		return getJfishDao().deleteAll(entityClass);
	}

	@Override
	public <T> T removeById(Class<T> entityClass, Serializable id) {
		T entity = getJfishDao().findById(entityClass, id);
		if(entity==null)
			return null;
		getJfishDao().delete(entity);
		return entity;
	}

	@Override
	public void flush() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T merge(T entity) {
		getJfishDao().dymanicUpdate(entity);
		return entity;
	}

	@Override
	public DataQuery createSQLQuery(String sqlString, Class entityClass) {
		JFishQuery jq = getJfishDao().createJFishQuery(sqlString, entityClass);
		DataQuery query = new JFishDataQuery(jq);
		return query;
	}

	@Override
	public DataQuery createMappingSQLQuery(String sqlString, String resultSetMapping) {
		throw new UnsupportedOperationException("not support operation!");
	}
	
	protected DataQuery createQuery(SelectExtQuery extQuery){
		return getJfishDao().createAsDataQuery(extQuery);
	}

	@Override
	public DataQuery createQuery(String sqlString) {
		return this.createSQLQuery(sqlString, null);
	}

	@Override
	public DataQuery createNamedQuery(String name) {
		return getFileNamedQueryFactory().createQuery(name);
	}
	

	@Override
	public JFishQueryBuilder createQueryBuilder(Class<?> entityClass) {
		JFishQueryBuilder query = JFishQueryBuilder.from(this, entityClass);
		return query;
	}
	

	public EntityManagerProvider getEntityManagerProvider(){
		return EntityManagerProvider.JDBC;
	}
	
	public SQLSymbolManager getSQLSymbolManager(){
		return this.jfishDao.getSqlSymbolManager();
	}
	
	
/*
	@Override
	public SequenceNameManager getSequenceNameManager() {
		return jfishDao.getSequenceNameManager();
	}*/

	public JFishDaoImplementor getJfishDao() {
		return jfishDao;
	}

	public void setJfishDao(JFishDaoImplementor jFishDao) {
		this.jfishDao = jFishDao;
	}

/*
	public void setSQLSymbolManager(SQLSymbolManager sQLSymbolManager) {
		SQLSymbolManager = sQLSymbolManager;
	}*/

	public JFishNamedFileQueryManagerImpl getFileNamedQueryFactory() {
		return (JFishNamedFileQueryManagerImpl)this.fileNamedQueryFactory;
	}


	public SequenceNameManager getSequenceNameManager(){
		return jfishDao.getSequenceNameManager();
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

	public DataQuery createQuery(String sql, Map<String, Object> values) {
		return jfishDao.createAsDataQuery(sql, values);
	}

	public void findPage(Class entityClass, Page page, Object... properties) {
		jfishDao.findPageByProperties(entityClass, page, CUtils.asOrCreateLinkedHashMap(properties));
	}

	public <T> void findPage(Class<T> entityClass, Page<T> page, Map<Object, Object> properties) {
		jfishDao.findPageByProperties(entityClass, page, properties);
	}

	public void removeList(Collection<?> entities) {
		if(LangUtils.isEmpty(entities))
			return ;
		getJfishDao().delete(entities);
	}
	
	public <T> List<T> findList(JFishQueryValue queryValue) {
		return getJfishDao().findList(queryValue);
	}
	
	public <T> T findUnique(JFishQueryValue queryValue) {
		return (T)getJfishDao().findUnique(queryValue);
	}
	
	public <T> void findPage(Page<T> page, JFishQueryValue squery) {
		getJfishDao().findPage(page, squery);
	}

	/*public void findPage(Page page, QueryBuilder squery) {
		entityManagerWraper.findPage(page, squery);
	}*/
	
	/*public <T> T findUnique(QueryBuilder squery) {
		return entityManagerWraper.findUnique(squery);
	}*/

	/*public <T> List<T> findList(QueryBuilder squery) {
		return entityManagerWraper.findList(squery);
	}*/


	public <T> T findUnique(Class<T> entityClass, Object... properties) {
		return jfishDao.findUniqueByProperties(entityClass, CUtils.asOrCreateLinkedHashMap(properties));
	}

	public <T> T findUnique(Class<T> entityClass, Map<Object, Object> properties) {
		return jfishDao.findUniqueByProperties(entityClass, properties);
	}

	public <T> T findUnique(String sql, Object... values) {
		DataQuery dq = jfishDao.createAsDataQuery(sql, (Class<?>)null);
		dq.setParameters(values);
		return (T) dq.getSingleResult();
	}

	public <T> T findUnique(String sql, Map<String, Object> values) {
		return (T)jfishDao.createAsDataQuery(sql, values).getSingleResult();
	}

	public <T> List<T> findByProperties(Class<T> entityClass, Object... properties) {
		return findByProperties(entityClass, CUtils.asOrCreateLinkedHashMap(properties));
	}

	public <T> List<T> findByProperties(Class<T> entityClass, Map<Object, Object> properties) {
		return jfishDao.findByProperties(entityClass, properties);
	}

	public <T> List<T> findByExample(Class<T> entityClass, Object obj) {
		Map properties = M.bean2Map(obj);
		return this.findByProperties(entityClass, properties);
	}

	public <T> void findPageByExample(Class<T> entityClass, Page<T> page, Object obj) {
		Map properties = M.bean2Map(obj);
		this.findPage(entityClass, page, properties);
	}

	public Number countRecord(Class entityClass, Map<Object, Object> properties) {
		return jfishDao.countByProperties(entityClass, properties);
	}

	public Number countRecord(Class entityClass, Object... params) {
		return countRecord(entityClass, CUtils.asOrCreateLinkedHashMap(params));
	}

	@Override
	public void delete(ILogicDeleteEntity entity) {
		entity.deleted();
		getJfishDao().save(entity);
	}

	@Override
	public <T extends ILogicDeleteEntity> T deleteById(Class<T> entityClass, Serializable id) {
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

	@Override
	public <T> T saveWith(T entity, String... relatedFields) {
		getJfishDao().save(entity, relatedFields);
		return entity;
	}

	@Override 
	public <T> int saveRef(T entity, String... relatedFields) {
		return getJfishDao().saveRef(entity, relatedFields);
	}

	@Override
	public <T> int dropRef(T entity, String... relatedFields) {
		return getJfishDao().dropRef(entity, relatedFields);
	}

	@Override
	public <T> int clearRef(T entity, String... relatedFields) {
		return getJfishDao().clearRef(entity, relatedFields);
	}

	@Override
	public <T> Page<T> findPageByQName(String queryName, RowMapper<T> rowMapper, Page<T> page, Object... params) {
		return getFileNamedQueryFactory().findPage(queryName, rowMapper, page, params);
	}


	@Override
	public JFishDao getRawManagerObject() {
		return jfishDao;
	}


	@Override
	public <T> T getRawManagerObject(Class<T> rawClass) {
		return rawClass.cast(getRawManagerObject());
	}


	public void setWatchSqlFile(boolean watchSqlFile) {
		this.watchSqlFile = watchSqlFile;
	}


}
