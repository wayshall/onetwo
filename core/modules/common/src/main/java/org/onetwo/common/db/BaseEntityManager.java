package org.onetwo.common.db;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.onetwo.common.db.sqlext.SQLSymbolManager;
import org.onetwo.common.utils.Page;

@SuppressWarnings("rawtypes")
public interface BaseEntityManager extends CreateQueryable {

	public <T> T load(Class<T> entityClass, Serializable id);
	
	public <T> T findById(Class<T> entityClass, Serializable id);

	public <T> T save(T entity);
	
	public void persist(Object entity);
	
	public void update(Object entity);

	public void remove(Object entity);
	
	public void removeList(Collection<?> entities);

	public <T> T removeById(Class<T> entityClass, Serializable id);

	public <T> List<T> findAll(Class<T> entityClass);

	public Number countRecord(Class entityClass, Map<Object, Object> properties);

	public Number countRecord(Class entityClass, Object... params);

//	public <T> T findUnique(final String sql, final Object... values);
//	public <T> T findUnique(QueryBuilder squery);
	
	public <T> T findUnique(Class<T> entityClass, Object... properties);
	
//	public <T> T findUnique(Class<T> entityClass, boolean tryTheBest, Object... properties);
	
	public <T> T findUnique(Class<T> entityClass, Map<Object, Object> properties);
	
	public <T> T findOne(Class<T> entityClass, Object... properties);
	public <T> T findOne(Class<T> entityClass, Map<Object, Object> properties);

	public <T> List<T> findByProperties(Class<T> entityClass, Object... properties);

	public <T> List<T> selectFields(Class<?> entityClass, String[] selectFields, Object... properties);
	/*****
	 * {@link #findByProperties(Class, Map)} 同义词
	 * @param entityClass
	 * @param properties
	 * @return
	 */
	public <T> List<T> select(Class<?> entityClass, Map<Object, Object> properties);

	/*****
	 * 根据属性查询列表
	 * @param entityClass
	 * @param properties
	 * @return
	 */
	public <T> List<T> findByProperties(Class<T> entityClass, Map<Object, Object> properties);
	
//	public <T> List<T> findList(QueryBuilder squery);
	
//	public void findPage(final Page page, QueryBuilder squery);

	public void findPage(final Class entityClass, final Page page, Object... properties);

	public void findPage(final Class entityClass, final Page page, Map<Object, Object> properties);

	/****
	 * @see remove
	 * @param entity
	 */
	@Deprecated
	public void delete(ILogicDeleteEntity entity);

	@Deprecated
	public <T extends ILogicDeleteEntity> T deleteById(Class<T> entityClass, Serializable id);
	
	public void flush();
	
	public void clear();
	
	public <T> T merge(T entity);
	
//	public EntityManager getEntityManager();
	
	public DataQuery createMappingSQLQuery(String sqlString, String resultSetMapping);
	
	
	public DataQuery createNamedQuery(String name);
	public DataQuery createQuery(String sql, Map<String, Object> values);
	
	public Long getSequences(String sequenceName, boolean createIfNotExist);
	public Long getSequences(Class entityClass, boolean createIfNotExist);
//	public SequenceNameManager getSequenceNameManager();
	
	public EntityManagerProvider getEntityManagerProvider();
	
	public SQLSymbolManager getSQLSymbolManager();
	

	public <T> List<T> findList(JFishQueryValue queryValue);
	
	public <T> T findUnique(JFishQueryValue queryValue);
	
	public <T> void findPage(Page<T> page, JFishQueryValue squery);

	public <T> T getRawManagerObject();
	public <T> T getRawManagerObject(Class<T> rawClass);
	
}