package org.onetwo.common.fish;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.onetwo.common.db.DataQuery;
import org.onetwo.common.db.EntityManagerProvider;
import org.onetwo.common.db.ExtQuery;
import org.onetwo.common.db.ILogicDeleteEntity;
import org.onetwo.common.db.JFishQueryValue;
import org.onetwo.common.db.ParamValues.PlaceHolder;
import org.onetwo.common.db.sqlext.SQLSymbolManager;
import org.onetwo.common.fish.exception.JFishOrmException;
import org.onetwo.common.fish.spring.EntityManagerOperationImpl;
import org.onetwo.common.fish.spring.JFishDaoImplementor;
import org.onetwo.common.fish.spring.JFishFileQueryDao;
import org.onetwo.common.utils.Page;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.RowMapper;

@SuppressWarnings("rawtypes")
public class JFishEntityManagerImpl implements JFishEntityManager, InitializingBean {

//	private SQLSymbolManager SQLSymbolManager;

	private JFishDaoImplementor jfishDao;
	private EntityManagerOperationImpl entityManagerWraper;
	
	public JFishEntityManagerImpl(){
		super();
	}
	
	public void afterPropertiesSet() throws Exception{
		this.entityManagerWraper = jfishDao.getEntityManagerWraper();
	}
	
	public <T> List<T> findAll(Class<T> entityClass){
		return jfishDao.findAll(entityClass);
	}
	
	@Override
	public <T> T load(Class<T> entityClass, Serializable id){
		T entity = findById(entityClass, id);
//		Assert.notNull(entity, "can not load the object from db : " + id);
		if(entity==null){
			throw new JFishOrmException("找不到数据：" + id);
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
	
	protected DataQuery createQuery(ExtQuery extQuery){
		return getJfishDao().getEntityManagerWraper().createQuery(extQuery);
	}

	@Override
	public DataQuery createQuery(String sqlString) {
		return this.createSQLQuery(sqlString, null);
	}

	@Override
	public DataQuery createNamedQuery(String name) {
		JFishQuery jfq = getJfishFileQueryDao().createJFishQueryByQName(name);
		return new JFishDataQuery(jfq);
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
	
	public JFishQuery createJFishQueryByQName(String queryName, Object... args) {
		return getJfishFileQueryDao().createJFishQueryByQName(queryName, args);
	}

	public JFishQuery createJFishQueryByQName(String queryName, PlaceHolder type, Object... args) {
		return getJfishFileQueryDao().createJFishQueryByQName(queryName, type, args);
	}

	public JFishQuery createCountJFishQueryByQName(String queryName, Object... args) {
		return getJfishFileQueryDao().createCountJFishQueryByQName(queryName, args);
	}

	public <T> List<T> findListByQName(String queryName, Object... params) {
		return getJfishFileQueryDao().findListByQName(queryName, params);
	}

	public <T> T findUniqueByQName(String queryName, Object... params) {
		return getJfishFileQueryDao().findUniqueByQName(queryName, params);
	}

	public <T> Page<T> findPageByQName(String queryName, Page<T> page, Object... params) {
		return getJfishFileQueryDao().findPageByQName(queryName, page, params);
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

	public JFishFileQueryDao getJfishFileQueryDao() {
		return (JFishFileQueryDao)jfishDao;
	}

	public Long getSequences(Class entityClass, boolean createIfNotExist) {
		return entityManagerWraper.getSequences(entityClass, createIfNotExist);
	}

	public Long getSequences(String sequenceName, boolean createIfNotExist) {
		return entityManagerWraper.getSequences(sequenceName, createIfNotExist);
	}

	public DataQuery createQuery(String sql, Map<String, Object> values) {
		return entityManagerWraper.createQuery(sql, values);
	}

	public void findPage(Class entityClass, Page page, Object... properties) {
		entityManagerWraper.findPage(entityClass, page, properties);
	}

	public void findPage(Class entityClass, Page page, Map<Object, Object> properties) {
		entityManagerWraper.findPage(entityClass, page, properties);
	}

	public void removeList(List entities) {
		entityManagerWraper.removeList(entities);
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

	public <T> T findUnique(Class<T> entityClass, boolean tryTheBest, Object... properties) {
		return entityManagerWraper.findUnique(entityClass, tryTheBest, properties);
	}

	public <T> T findUnique(Class<T> entityClass, Object... properties) {
		return entityManagerWraper.findUnique(entityClass, properties);
	}

	public <T> T findUnique(Class<T> entityClass, Map<Object, Object> properties) {
		return entityManagerWraper.findUnique(entityClass, properties);
	}

	public <T> T findUnique(String sql, Object... values) {
		return entityManagerWraper.findUnique(sql, values);
	}

	public <T> T findUnique(String sql, Map<String, Object> values) {
		return entityManagerWraper.findUnique(sql, values);
	}

	public <T> List<T> findByProperties(Class entityClass, Object... properties) {
		return entityManagerWraper.findByProperties(entityClass, properties);
	}

	public <T> List<T> findByProperties(Class entityClass, Map<Object, Object> properties) {
		return entityManagerWraper.findByProperties(entityClass, properties);
	}

	public <T> List<T> findByExample(Class entityClass, Object obj) {
		return entityManagerWraper.findByExample(entityClass, obj);
	}

	public <T> void findPageByExample(Class<T> entityClass, Page<T> page, Object obj) {
		entityManagerWraper.findPageByExample(entityClass, page, obj);
	}

	public Number countRecord(Class entityClass, Map<Object, Object> properties) {
		return entityManagerWraper.countRecord(entityClass, properties);
	}

	public Number countRecord(Class entityClass, Object... params) {
		return entityManagerWraper.countRecord(entityClass, params);
	}

	@Override
	public void delete(ILogicDeleteEntity entity) {
		entityManagerWraper.delete(entity);
	}

	@Override
	public <T extends ILogicDeleteEntity> T deleteById(Class<T> entityClass, Serializable id) {
		return entityManagerWraper.deleteById(entityClass, id);
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
		return getJfishFileQueryDao().findPageByQName(queryName, rowMapper, page, params);
	}

}
