package org.onetwo.dbm.core.spi;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.onetwo.common.db.DbmQueryValue;
import org.onetwo.common.db.sql.DynamicQuery;
import org.onetwo.common.utils.Page;
import org.onetwo.dbm.annotation.DataBaseOperation;
import org.onetwo.dbm.query.DbmQuery;

public interface DbmSession {
	public boolean isProxyManagedTransaction();
	public void flush();
	public DbmSessionFactory getSessionFactory();
	public DbmTransaction beginTransaction();

	/*****
	 * 保存对象和关联属性的对象到数据库，<br/>
	 * 无论是主对象还是关联对象，会根据id是否为null值去判断是保存还是更新，<br/>
	 * 更新时只会更新不为null的字段。<br/>
	 * 相当于{@link org.onetwo.dbm.event.DbmSessionEventSource#insertOrUpdate insertOrUpdate(entity, true, relatedFields)}
	 * 
	 * @param entity
	 * @return
	 */
	@DataBaseOperation
	public <T> int save(T entity);
	
	/*****
	 * 
	 * 根据对象属性插入一条数据到数据库
	 *  <br/><br/>
	 *  
	 * 执行insert语句
	 * 关联插入的所有实体，如果id为null，即被认为是新建实体，执行insert插入，和关联插入；否则为已保存过的实体，忽略，不执行任何操作
	 * @param entity
	 * @return
	 */
	@DataBaseOperation
	public <T> int insert(T entity);
	
	/********
	 * 用对象的id作为条件，根据对象的属性更新数据库记录，如果属性为null，则更新数据库为null值
	 * <br/><br/>
	 * 
	 * 执行update语句
	 * 关联更新的所有实体也是执行update语句
	 * 如果关联的实体id为null，即被认为是新建实体，忽略，不执行任何操作；否则为已保存过的实体，执行update操作
	 * 
	 * @param entity
	 * @return
	 */
	@DataBaseOperation
	public int update(Object entity);

	/********
	 * 根据id查找对象
	 * @param entityClass
	 * @param id
	 * @return
	 */
	@DataBaseOperation
	public <T> T findById(Class<T> entityClass, Serializable id);

	/***********
	 * 根据id删除数据库记录
	 * @param entityClass
	 * @param id
	 * @return
	 */
	@DataBaseOperation
	public int delete(Class<?> entityClass, Object id);

	@DataBaseOperation
	public int deleteAll(Class<?> entityClass);
	
	/*******
	 * 删除实体和关联属性的实体
	 * 
	 * @param entity
	 * @return
	 */
	@DataBaseOperation
	public int delete(Object entity);
	
	/*****
	 * 返回一个<code>JFishQuery</code>查询对象
	 * @param sql
	 * @return
	 */
	public DbmQuery createDbmQuery(String sql);
	
	
	public DbmQuery createDbmQuery(DynamicQuery query);
	
	/***********
	 * 返回一个<code>JFishQuery</code>查询对象
	 * @param sql
	 * @param entityClass
	 * @return
	 */
	public DbmQuery createDbmQuery(String sql, Class<?> entityClass);
	
	/******
	 * 根据对象属性插入一条数据到数据库，但不会select主键，返回的对象没有id
	 * @param entity
	 * @return
	 */
	@DataBaseOperation
	public <T> int justInsert(T entity);

	/*********
	 * 批量插入
	 * @param entities
	 * @return
	 */
	@DataBaseOperation
	public <T> int batchInsert(Collection<T> entities);
	
	/*****
	 * 批量更新<br/>
	 * 更新实体的所有字段，非动态更新。
	 * @param entities
	 * @return
	 */
	@DataBaseOperation
	public <T> int batchUpdate(Collection<T> entities);

	/*******
	 * 动态更新（忽略null值），用对象的id作为条件，根据对象的属性更新数据库记录
	 * @param entity
	 * @return
	 */
	@DataBaseOperation
	public int dymanicUpdate(Object entity);

	/**********
	 * 根据命名的sql查询，返回唯一结果
	 * @param sql
	 * @param params
	 * @param type
	 * @return
	 */
	@DataBaseOperation
	public <T> T findUnique(String sql, Map<String, ?> params, Class<T> type);
	
	/*****
	 * 根据原生sql查询，返回唯一结果
	 * @param sql
	 * @param args
	 * @param type
	 * @return
	 */
	@DataBaseOperation
	public <T> T findUnique(String sql, Object[] args, Class<T> type);
	
	/**********
	 * 根据原生sql查询
	 * @param sql
	 * @param args
	 * @param type
	 * @return
	 */
	@DataBaseOperation
	public <T> List<T> findList(String sql, Object[] args, Class<T> type);
	
	/**********
	 * 根据命名的sql查询
	 * @param sql
	 * @param params
	 * @param type
	 * @return
	 */
	@DataBaseOperation
	public <T> List<T> findList(String sql, Map<String, ?> params, Class<T> type);
	
	/*****
	 * 通过<code>DynamicQuery</code>对象查找一条数据
	 * @param query
	 * @return
	 */
	@DataBaseOperation
	public <T> T findUnique(DynamicQuery query);

	@DataBaseOperation
	public Number count(DbmQueryValue queryValue);

	@DataBaseOperation
	public int executeUpdate(DynamicQuery query);

	/*****
	 * 通过<code>DynamicQuery</code>对象查找数据
	 * @param query
	 * @return
	 */
	@DataBaseOperation
	public <T> List<T> findList(DynamicQuery query);

	@DataBaseOperation
	public <T> List<T> findAll(Class<T> entityClass);

	@DataBaseOperation
	public <T> List<T> findByProperties(Class<T> entityClass, Map<Object, Object> properties);

	@DataBaseOperation
	public void findPageByProperties(Class<?> entityClass, Page<?> page, Map<Object, Object> properties);
	
	/*****
	 *  查找唯一记录，如果找不到返回null，如果多于一条记录，抛出异常。
	 * @param entityClass
	 * @param properties
	 * @return
	 */
	@DataBaseOperation
	public <T> T findUniqueByProperties(Class<T> entityClass, Map<Object, Object> properties);

	@DataBaseOperation
	public Number countByProperties(Class<?> entityClass, Map<Object, Object> properties);
	
}