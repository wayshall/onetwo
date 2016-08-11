package org.onetwo.common.db;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.onetwo.common.db.builder.QueryBuilder;
import org.onetwo.common.db.filequery.QueryProvideManager;
import org.onetwo.common.db.sqlext.SQLSymbolManager;
import org.onetwo.common.utils.Page;

/****
 * 通用的实体查询接口
 * ByProperties后缀的方法名一般以Map为参数，其作用和没有ByProperties后缀的一样
 * @author way
 *
 */
public interface BaseEntityManager extends QueryProvideManager {

	public <T> T load(Class<T> entityClass, Serializable id);
	
	public <T> T findById(Class<T> entityClass, Serializable id);

	public <T> T save(T entity);
	public <T> Collection<T> saves(Collection<T> entities);
	
	public <T> void persist(T entity);
	
	/****
	 * 根据id把实体的所有字段更新到数据库
	 * @param entity
	 */
	public void update(Object entity);

	/****
	 * 如果实现了ILogicDeleteEntity接口，着逻辑删除
	 * 否则，物理删除
	 * @param entity
	 */
	public void remove(Object entity);
	
	public <T> void removes(Collection<T> entities);
	public <T> Collection<T> removeByIds(Class<T> entityClass, Serializable[] id);
	public <T> T removeById(Class<T> entityClass, Serializable id);

	public <T> List<T> findAll(Class<T> entityClass);

	public Number countRecordByProperties(Class<?> entityClass, Map<Object, Object> properties);

	public Number countRecord(Class<?> entityClass, Object... params);

	/***
	 *  查找唯一记录，如果找不到返回null，如果多于一条记录，抛出异常。
	 * @param entityClass
	 * @param properties
	 * @return
	 */
	public <T> T findUniqueByProperties(Class<T> entityClass, Map<Object, Object> properties);
	public <T> T findUnique(Class<T> entityClass, Object... properties);
	
	public <T> T findOne(Class<T> entityClass, Object... properties);
	public <T> T findOneByProperties(Class<T> entityClass, Map<Object, Object> properties);


	/*****
	 * 根据属性查询列表
	 * @param entityClass
	 * @param properties
	 * @return
	 */
	public <T> List<T> findList(Class<T> entityClass, Object... properties);
	public <T> List<T> findListByProperties(Class<T> entityClass, Map<Object, Object> properties);
	public <T> List<T> findList(QueryBuilder squery);

	public <T> List<T> selectFields(Class<?> entityClass, Object[] selectFields, Object... properties);
	public <T> List<T> selectFieldsToEntity(Class<?> entityClass, Object[] selectFields, Object... properties);
	

	public void findPage(final Class<?> entityClass, final Page<?> page, Object... properties);

	public <T> void findPageByProperties(final Class<T>  entityClass, final Page<T> page, Map<Object, Object> properties);
	
	public <T> void findPage(final Page<T> page, QueryBuilder query);
	
	public <T> void findPage(Page<T> page, DbmQueryValue squery);
	
	public void flush();
	
	public void clear();
	
	/***
	 * 依赖于实现
	 * @param entity
	 * @return
	 */
	public <T> T merge(T entity);
	
//	public EntityManager getEntityManager();
	
//	public DataQuery createMappingSQLQuery(String sqlString, String resultSetMapping);
	
	
	public DataQuery createNamedQuery(String name);
	public DataQuery createQuery(String sql, Map<String, Object> values);
	
	public Long getSequences(String sequenceName, boolean createIfNotExist);
	public Long getSequences(Class<?> entityClass, boolean createIfNotExist);
//	public SequenceNameManager getSequenceNameManager();
	
	public EntityManagerProvider getEntityManagerProvider();
	
	public SQLSymbolManager getSQLSymbolManager();
	


	public <T> T getRawManagerObject();
	public <T> T getRawManagerObject(Class<T> rawClass);
	
	public <T> T narrowAs(Class<T> entityManagerClass);
	
}