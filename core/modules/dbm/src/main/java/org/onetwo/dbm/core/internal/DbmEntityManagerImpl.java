package org.onetwo.dbm.core.internal;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.sql.DataSource;

import org.onetwo.common.db.BaseEntityManagerAdapter;
import org.onetwo.common.db.DataBase;
import org.onetwo.common.db.DbmQueryValue;
import org.onetwo.common.db.DbmQueryWrapper;
import org.onetwo.common.db.EntityManagerProvider;
import org.onetwo.common.db.builder.QueryBuilder;
import org.onetwo.common.db.builder.Querys;
import org.onetwo.common.db.filequery.DbmNamedSqlFileManager;
import org.onetwo.common.db.filequery.FileNamedQueryManager;
import org.onetwo.common.db.filequery.SqlParamterPostfixFunctionRegistry;
import org.onetwo.common.db.sql.SequenceNameManager;
import org.onetwo.common.db.sqlext.SQLSymbolManager;
import org.onetwo.common.db.sqlext.SelectExtQuery;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.dbm.core.spi.DbmEntityManager;
import org.onetwo.dbm.core.spi.DbmSessionFactory;
import org.onetwo.dbm.core.spi.DbmSessionImplementor;
import org.onetwo.dbm.exception.EntityNotFoundException;
import org.onetwo.dbm.jdbc.mapper.RowMapperFactory;
import org.onetwo.dbm.query.DbmNamedFileQueryManagerImpl;
import org.onetwo.dbm.query.DbmQuery;
import org.onetwo.dbm.query.DbmQueryWrapperImpl;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

//@SuppressWarnings({"rawtypes", "unchecked"})
public class DbmEntityManagerImpl extends BaseEntityManagerAdapter implements DbmEntityManager, InitializingBean , DisposableBean {

	private DbmSessionFactory sessionFactory;
//	private EntityManagerOperationImpl entityManagerWraper;
//	private JFishList<JFishEntityManagerLifeCycleListener> emListeners;
//	private ApplicationContext applicationContext;
	
	private FileNamedQueryManager fileNamedQueryManager;
//	private boolean watchSqlFile = false;
	private SqlParamterPostfixFunctionRegistry sqlParamterPostfixFunctionRegistry;
	
	public DbmEntityManagerImpl(DbmSessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
	}

	@Override
	public DataSource getDataSource() {
		return this.sessionFactory.getDataSource();
	}
	
	public DbmSessionImplementor getCurrentSession(){
		return (DbmSessionImplementor)sessionFactory.getSession();
	}
	
	@Override
	public void update(Object entity) {
		this.getCurrentSession().update(entity);
//		throwIfEffectiveCountError("update", 1, rs);
	}


	/*@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}*/


	@Override
	public DataBase getDataBase() {
		return sessionFactory.getDialect().getDbmeta().getDataBase();
	}


	public void afterPropertiesSet() throws Exception{
		Objects.requireNonNull(sessionFactory, "sessionFactory");
		/*FileNamedQueryFactoryListener listener = SpringUtils.getBean(applicationContext, FileNamedQueryFactoryListener.class);
		this.fileNamedQueryFactory = new JFishNamedFileQueryManagerImpl(this, jfishDao.getDialect().getDbmeta().getDb(), watchSqlFile, listener);
		this.fileNamedQueryFactory.initQeuryFactory(this);*/
		
//		this.entityManagerWraper = jfishDao.getEntityManagerWraper();
		//不在set方法里设置，避免循环依赖
//		this.fileNamedQueryFactory = SpringUtils.getBean(applicationContext, FileNamedQueryFactory.class);
		
		DbmNamedSqlFileManager sqlFileManager = DbmNamedSqlFileManager.createNamedSqlFileManager(sessionFactory.getDataBaseConfig().isWatchSqlFile());
		DbmNamedFileQueryManagerImpl fq = new DbmNamedFileQueryManagerImpl(sqlFileManager);
		fq.setQueryProvideManager(this);
		this.fileNamedQueryManager = fq;
			
	}

	@Override
	public void destroy() throws Exception {
	}
	
	public <T> List<T> findAll(Class<T> entityClass){
		return getCurrentSession().findAll(entityClass);
	}
	
	@Override
	public <T> T load(Class<T> entityClass, Serializable id){
		T entity = findById(entityClass, id);
//		Assert.notNull(entity, "can not load the object from db : " + id);
		if(entity==null){
			throw new EntityNotFoundException("找不到数据：" + id);
		}
		return entity;
	}

	@Override
	public <T> T findById(Class<T> entityClass, Serializable id) {
		return getCurrentSession().findById(entityClass, id);
	}

	@Override
	public <T> T save(T entity) {
		getCurrentSession().save(entity);
		/*int expectsize = LangUtils.size(entity);
		throwIfEffectiveCountError("save", expectsize, rs);*/
		return entity;
	}
	
	/*private void throwIfEffectiveCountError(String operation, int expectCount, int effectiveCount){
		DbmUtils.throwIfEffectiveCountError(operation + " error.", expectCount, effectiveCount);
	}*/

	@Override
	public <T> void persist(T entity) {
		getCurrentSession().insert(entity);
		/*int expectsize = LangUtils.size(entity);
		throwIfEffectiveCountError("persist", expectsize, rs);*/
	}

	@Override
	public void remove(Object entity) {
		getCurrentSession().delete(entity);
		/*int rs = getDbmDao().delete(entity);
		int expectsize = LangUtils.size(entity);
		throwIfEffectiveCountError("remove", expectsize, rs);*/
	}

	@Override
	public int removeAll(Class<?> entityClass) {
		return getCurrentSession().deleteAll(entityClass);
	}

	@Override
	public <T> T removeById(Class<T> entityClass, Serializable id) {
		T entity = getCurrentSession().findById(entityClass, id);
		if(entity==null)
			return null;
		getCurrentSession().delete(entity);
		/*int rs = getDbmDao().delete(entity);
		throwIfEffectiveCountError("removeById", 1, rs);*/
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

	/***
	 * jfish dbm实现为dymanicUpdate, 更新非null字段到数据库
	 */
	@Override
	public <T> T merge(T entity) {
		getCurrentSession().dymanicUpdate(entity);
//		throwIfEffectiveCountError("merge", 1, rs);
		return entity;
	}

	@Override
	public DbmQueryWrapper createSQLQuery(String sqlString, Class<?> entityClass) {
		DbmQuery jq = getCurrentSession().createDbmQuery(sqlString, entityClass);
		DbmQueryWrapper query = new DbmQueryWrapperImpl(jq);
		return query;
	}

	
	protected DbmQueryWrapper createQuery(SelectExtQuery extQuery){
		return getCurrentSession().createAsDataQuery(extQuery);
	}

	/*@Override
	public DbmQueryWrapper createQuery(String sqlString) {
		return this.createSQLQuery(sqlString, null);
	}*/

	@Override
	public DbmQueryWrapper createNamedQuery(String name) {
		/*JFishNamedFileQueryInfo nameInfo = getFileNamedQueryFactory().getNamedQueryInfo(name);
		return getFileNamedQueryFactory().createQuery(nameInfo);*/
		throw new UnsupportedOperationException("jfish named query unsupported by this way!");
	}
	

	@Override
	public QueryBuilder createQueryBuilder(Class<?> entityClass) {
		QueryBuilder query = Querys.from(this, entityClass);
		return query;
	}
	

	public EntityManagerProvider getEntityManagerProvider(){
		return EntityManagerProvider.JDBC;
	}
	
	public SQLSymbolManager getSQLSymbolManager(){
		return sessionFactory.getSqlSymbolManager();
	}
	
	

	@Override
	public FileNamedQueryManager getFileNamedQueryManager() {
		return this.fileNamedQueryManager;
	}

	@Override
	public SequenceNameManager getSequenceNameManager(){
		return sessionFactory.getSequenceNameManager();
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
			DbmQueryWrapper dq = this.createSQLQuery(sql, null);
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
	public DbmQueryWrapper createQuery(String sql, Map<String, Object> values) {
		return getCurrentSession().createAsDataQuery(sql, values);
	}

	@Override
	public <T> void findPage(Class<T> entityClass, Page<T> page, Object... properties) {
		getCurrentSession().findPageByProperties(entityClass, page, CUtils.asLinkedMap(properties));
	}

	@Override
	public <T> void findPageByProperties(Class<T> entityClass, Page<T> page, Map<Object, Object> properties) {
		getCurrentSession().findPageByProperties(entityClass, page, properties);
	}

	/*public <T> void removeList(Collection<T> entities) {
		if(LangUtils.isEmpty(entities))
			return ;
		getJfishDao().delete(entities);
	}*/
	
	public <T> List<T> findList(DbmQueryValue queryValue) {
		return getCurrentSession().findList(queryValue);
	}
	
	public <T> T findUnique(DbmQueryValue queryValue) {
		return getCurrentSession().findUnique(queryValue);
	}
	
	public <T> void findPage(Page<T> page, DbmQueryValue squery) {
		getCurrentSession().findPage(page, squery);
	}

	/****
	 *  查找唯一记录，如果找不到返回null，如果多于一条记录，抛出异常。
	 */
	public <T> T findUniqueByProperties(Class<T> entityClass, Map<Object, Object> properties) {
		return getCurrentSession().findUniqueByProperties(entityClass, properties);
	}

	public <T> T findUnique(String sql, Object... values) {
		DbmQueryWrapper dq = getCurrentSession().createAsDataQuery(sql, (Class<?>)null);
		dq.setParameters(values);
		return dq.getSingleResult();
	}


	public <T> List<T> findList(Class<T> entityClass, Object... properties) {
		return findListByProperties(entityClass, CUtils.asLinkedMap(properties));
	}

	/***
	 * 根据属性查找数据
	 * 返回结果不为null
	 */
	public <T> List<T> findListByProperties(Class<T> entityClass, Map<Object, Object> properties) {
		return getCurrentSession().findByProperties(entityClass, properties);
	}

	public <T> List<T> findByExample(Class<T> entityClass, Object obj) {
		Map<String, Object> properties = ReflectUtils.toMap(obj);
		return this.findList(entityClass, properties);
	}

	public <T> void findPageByExample(Class<T> entityClass, Page<T> page, Object obj) {
		Map<String, Object> properties = ReflectUtils.toMap(obj);
		this.findPage(entityClass, page, properties);
	}

	public Number countRecordByProperties(Class<?> entityClass, Map<Object, Object> properties) {
		return getCurrentSession().countByProperties(entityClass, properties);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getRawManagerObject() {
		return (T)sessionFactory;
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


	@Override
	public RowMapperFactory getRowMapperFactory() {
		return this.sessionFactory.getRowMapperFactory();
	}

}
