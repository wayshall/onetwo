package org.onetwo.common.jfishdbm.support;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.onetwo.common.db.BaseEntityManagerAdapter;
import org.onetwo.common.db.DataBase;
import org.onetwo.common.db.DataQuery;
import org.onetwo.common.db.EntityManagerProvider;
import org.onetwo.common.db.JFishQueryValue;
import org.onetwo.common.db.filequery.FileNamedQueryManager;
import org.onetwo.common.db.filequery.JFishNamedSqlFileManager;
import org.onetwo.common.db.filequery.SqlParamterPostfixFunctionRegistry;
import org.onetwo.common.db.sql.SequenceNameManager;
import org.onetwo.common.db.sqlext.SQLSymbolManager;
import org.onetwo.common.db.sqlext.SelectExtQuery;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.jfishdbm.exception.JFishEntityNotFoundException;
import org.onetwo.common.jfishdbm.exception.JFishOrmException;
import org.onetwo.common.jfishdbm.query.JFishDataQuery;
import org.onetwo.common.jfishdbm.query.JFishNamedFileQueryManagerImpl;
import org.onetwo.common.jfishdbm.query.JFishQuery;
import org.onetwo.common.jfishdbm.query.JFishQueryBuilder;
import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.Page;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

//@SuppressWarnings({"rawtypes", "unchecked"})
public class JFishEntityManagerImpl extends BaseEntityManagerAdapter implements JFishEntityManager, InitializingBean , DisposableBean {

	private JFishDaoImplementor jfishDao;
//	private EntityManagerOperationImpl entityManagerWraper;
//	private JFishList<JFishEntityManagerLifeCycleListener> emListeners;
//	private ApplicationContext applicationContext;
	
	private FileNamedQueryManager fileNamedQueryManager;
//	private boolean watchSqlFile = false;
	private SqlParamterPostfixFunctionRegistry sqlParamterPostfixFunctionRegistry;
	
	public JFishEntityManagerImpl(){
	}
	
	
	@Override
	public void update(Object entity) {
		this.jfishDao.update(entity);
	}


	/*@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}*/


	@Override
	public DataBase getDataBase() {
		return jfishDao.getDialect().getDbmeta().getDataBase();
	}


	public void afterPropertiesSet() throws Exception{
		/*FileNamedQueryFactoryListener listener = SpringUtils.getBean(applicationContext, FileNamedQueryFactoryListener.class);
		this.fileNamedQueryFactory = new JFishNamedFileQueryManagerImpl(this, jfishDao.getDialect().getDbmeta().getDb(), watchSqlFile, listener);
		this.fileNamedQueryFactory.initQeuryFactory(this);*/
		
//		this.entityManagerWraper = jfishDao.getEntityManagerWraper();
		//不在set方法里设置，避免循环依赖
//		this.fileNamedQueryFactory = SpringUtils.getBean(applicationContext, FileNamedQueryFactory.class);
		
		JFishNamedSqlFileManager sqlFileManager = JFishNamedSqlFileManager.createNamedSqlFileManager(jfishDao.getDataBaseConfig().isWatchSqlFile());
		JFishNamedFileQueryManagerImpl fq = new JFishNamedFileQueryManagerImpl(sqlFileManager);
		fq.setQueryProvideManager(this);
		this.fileNamedQueryManager = fq;
			
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
		int rs = getJfishDao().save(entity);
		throwIfEffectiveCountError("save", 1, rs);
		return entity;
	}
	
	private void throwIfEffectiveCountError(String operation, int expectCount, int effectiveCount){
		if(effectiveCount<expectCount)
			throw new JFishOrmException(operation + " error, expect effective: " + expectCount+", but actual effective: " + effectiveCount);
	}

	@Override
	public void persist(Object entity) {
		int rs = getJfishDao().insert(entity);
		throwIfEffectiveCountError("persist", 1, rs);
	}

	@Override
	public void remove(Object entity) {
		int rs = getJfishDao().delete(entity);
		throwIfEffectiveCountError("remove", 1, rs);
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
		int rs = getJfishDao().delete(entity);
		throwIfEffectiveCountError("removeById", 1, rs);
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
	public DataQuery createSQLQuery(String sqlString, Class<?> entityClass) {
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
		/*JFishNamedFileQueryInfo nameInfo = getFileNamedQueryFactory().getNamedQueryInfo(name);
		return getFileNamedQueryFactory().createQuery(nameInfo);*/
		throw new UnsupportedOperationException("jfish named query unsupported by this way!");
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

	@Override
	public FileNamedQueryManager getFileNamedQueryManager() {
		return this.fileNamedQueryManager;
	}

	@Override
	public SequenceNameManager getSequenceNameManager(){
		return jfishDao.getSequenceNameManager();
	}
	
	@Override
	public Long getSequences(Class<?> entityClass, boolean createIfNotExist) {
		String seqName = getSequenceNameManager().getSequenceName(entityClass);
		return getSequences(seqName, createIfNotExist);
	}

	@Override
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
				this.createSequence(sequenceName);
				/*try {
					DataQuery dq = this.createSQLQuery(getSequenceNameManager().getCreateSequence(sequenceName), null);
					dq.executeUpdate();
					
					dq = this.createSQLQuery(sql, null);
					id = ((Number)dq.getSingleResult()).longValue();
				} catch (Exception ne) {
					ne.printStackTrace();
					throw new ServiceException("createSequences error: " + e.getMessage(), e);
				}
				if (id == null)
					throw new ServiceException("createSequences error: " + e.getMessage(), e);*/
			}
		}
		return id;
	}

	@Override
	public DataQuery createQuery(String sql, Map<String, Object> values) {
		return jfishDao.createAsDataQuery(sql, values);
	}

	@Override
	public void findPage(Class<?> entityClass, Page<?> page, Object... properties) {
		jfishDao.findPageByProperties(entityClass, page, CUtils.asLinkedMap(properties));
	}

	@Override
	public <T> void findPage(Class<T> entityClass, Page<T> page, Map<Object, Object> properties) {
		jfishDao.findPageByProperties(entityClass, page, properties);
	}

	/*public <T> void removeList(Collection<T> entities) {
		if(LangUtils.isEmpty(entities))
			return ;
		getJfishDao().delete(entities);
	}*/
	
	public <T> List<T> findList(JFishQueryValue queryValue) {
		return getJfishDao().findList(queryValue);
	}
	
	public <T> T findUnique(JFishQueryValue queryValue) {
		return (T)getJfishDao().findUnique(queryValue);
	}
	
	public <T> void findPage(Page<T> page, JFishQueryValue squery) {
		getJfishDao().findPage(page, squery);
	}

	public <T> T findUnique(Class<T> entityClass, Map<Object, Object> properties) {
		return jfishDao.findUniqueByProperties(entityClass, properties);
	}

	public <T> T findUnique(String sql, Object... values) {
		DataQuery dq = jfishDao.createAsDataQuery(sql, (Class<?>)null);
		dq.setParameters(values);
		return (T) dq.getSingleResult();
	}


	public <T> List<T> findByProperties(Class<T> entityClass, Object... properties) {
		return findByProperties(entityClass, CUtils.asLinkedMap(properties));
	}

	public <T> List<T> findByProperties(Class<T> entityClass, Map<Object, Object> properties) {
		return jfishDao.findByProperties(entityClass, properties);
	}

	public <T> List<T> findByExample(Class<T> entityClass, Object obj) {
		Map<Object, Object> properties = CUtils.bean2Map(obj);
		return this.findByProperties(entityClass, properties);
	}

	public <T> void findPageByExample(Class<T> entityClass, Page<T> page, Object obj) {
		Map<Object, Object> properties = CUtils.bean2Map(obj);
		this.findPage(entityClass, page, properties);
	}

	public Number countRecord(Class<?> entityClass, Map<Object, Object> properties) {
		return jfishDao.countByProperties(entityClass, properties);
	}

	/*@Override
	public <T> Page<T> findPageByQName(String queryName, RowMapper<T> rowMapper, Page<T> page, Object... params) {
		JFishNamedFileQueryInfo nameInfo = getFileNamedQueryFactory().getNamedQueryInfo(queryName);
		return getFileNamedQueryFactory().findPage(nameInfo, page, params);
	}*/

	@Override
	public <T> T getRawManagerObject() {
		return (T)jfishDao;
	}

	@Override
	public <T> T getRawManagerObject(Class<T> rawClass) {
		return rawClass.cast(getRawManagerObject());
	}

	public void setFileNamedQueryManager(FileNamedQueryManager fileNamedQueryFactory) {
		this.fileNamedQueryManager = fileNamedQueryFactory;
	}


	public SqlParamterPostfixFunctionRegistry getSqlParamterPostfixFunctionRegistry() {
		return sqlParamterPostfixFunctionRegistry;
	}

	public void setSqlParamterPostfixFunctionRegistry(
			SqlParamterPostfixFunctionRegistry sqlParamterPostfixFunctionRegistry) {
		this.sqlParamterPostfixFunctionRegistry = sqlParamterPostfixFunctionRegistry;
	}

}
