package org.onetwo.common.fish.spring;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.onetwo.common.db.JFishQueryValue;
import org.onetwo.common.db.sql.DynamicQuery;
import org.onetwo.common.fish.JFishQuery;
import org.onetwo.common.utils.Page;

public interface JFishDao {

	/*****
	 * 保存对象和关联属性的对象到数据库，<br/>
	 * 无论是主对象还是关联对象，会根据id是否为null值去判断是保存还是更新，<br/>
	 * 更新时只会更新不为null的字段。<br/>
	 * 相当于{@link org.onetwo.common.fish.event.JFishEventSource#insertOrUpdate insertOrUpdate(entity, true, relatedFields)}
	 * 
	 * @param entity
	 * @param relatedFields
	 * @return
	 */
	public <T> int save(T entity, String... relatedFields);
	
	/*****
	 * 
	 * 根据对象属性插入一条数据到数据库
	 *  <br/><br/>
	 *  
	 * 执行insert语句
	 * 关联插入的所有实体，如果id为null，即被认为是新建实体，执行insert插入，和关联插入；否则为已保存过的实体，忽略，不执行任何操作
	 * @param entity
	 * @param relatedFields
	 * @return
	 */
	public <T> int insert(T entity, String... relatedFields);
	
	/********
	 * 用对象的id作为条件，根据对象的属性更新数据库记录，如果属性为null，则更新数据库为null值
	 * <br/><br/>
	 * 
	 * 执行update语句
	 * 关联更新的所有实体也是执行update语句
	 * 如果关联的实体id为null，即被认为是新建实体，忽略，不执行任何操作；否则为已保存过的实体，执行update操作
	 * 
	 * @param entity
	 * @param relatedFields
	 * @return
	 */
	public int update(Object entity, String... relatedFields);

	/********
	 * 根据id查找对象
	 * @param entityClass
	 * @param id
	 * @return
	 */
	public <T> T findById(Class<T> entityClass, Serializable id);

	/***********
	 * 根据id删除数据库记录
	 * @param entityClass
	 * @param id
	 * @return
	 */
	public int delete(Class<?> entityClass, Object id);
	
	public int deleteAll(Class<?> entityClass);
	
	/*******
	 * 删除实体和关联属性的实体
	 * 
	 * @param entity
	 * @param relatedFields
	 * @return
	 */
	public int delete(Object entity, String... relatedFields);
	
	/*****
	 * 返回一个<code>JFishQuery</code>查询对象
	 * @param sql
	 * @return
	 */
	public JFishQuery createJFishQuery(String sql);
	
	
	public JFishQuery createJFishQuery(DynamicQuery query);
	
	/***********
	 * 返回一个<code>JFishQuery</code>查询对象
	 * @param sql
	 * @param entityClass
	 * @return
	 */
	public JFishQuery createJFishQuery(String sql, Class<?> entityClass);
	
	/******
	 * 根据对象属性插入一条数据到数据库，但不会select主键，返回的对象没有id
	 * @param entity
	 * @return
	 */
	public <T> int justInsert(T entity);

	/*********
	 * 批量插入
	 * @param entities
	 * @return
	 */
	public <T> int batchInsert(Collection<T> entities);
	
	/*****
	 * 批量更新<br/>
	 * 更新实体的所有字段，非动态更新。
	 * @param entities
	 * @return
	 */
	public <T> int batchUpdate(Collection<T> entities);

	/*******
	 * 动态更新（忽略null值），用对象的id作为条件，根据对象的属性更新数据库记录
	 * @param entity
	 * @return
	 */
	public int dymanicUpdate(Object entity, String... relatedFields);

	/**********
	 * 根据命名的sql查询，返回唯一结果
	 * @param sql
	 * @param params
	 * @param type
	 * @return
	 */
	public <T> T findUnique(String sql, Map<String, ?> params, Class<T> type);
	
	/*****
	 * 根据原生sql查询，返回唯一结果
	 * @param sql
	 * @param args
	 * @param type
	 * @return
	 */
	public <T> T findUnique(String sql, Object[] args, Class<T> type);
	
	/**********
	 * 根据原生sql查询
	 * @param sql
	 * @param args
	 * @param type
	 * @return
	 */
	public <T> List<T> findList(String sql, Object[] args, Class<T> type);
	
	/**********
	 * 根据命名的sql查询
	 * @param sql
	 * @param params
	 * @param type
	 * @return
	 */
	public <T> List<T> findList(String sql, Map<String, ?> params, Class<T> type);
	
	/*****
	 * 通过<code>DynamicQuery</code>对象查找一条数据
	 * @param query
	 * @return
	 */
	public <T> T findUnique(DynamicQuery query);
	
	public Number count(JFishQueryValue queryValue);
	
	public int executeUpdate(DynamicQuery query);

	/*****
	 * 通过<code>DynamicQuery</code>对象查找数据
	 * @param query
	 * @return
	 */
	public <T> List<T> findList(DynamicQuery query);
	
	public <T> List<T> findAll(Class<T> entityClass);
	
	public <T> List<T> findByProperties(Class<T> entityClass, Map<Object, Object> properties);
	
	public void findPageByProperties(Class<?> entityClass, Page<?> page, Map<Object, Object> properties);
	
	public <T> T findUniqueByProperties(Class<T> entityClass, Map<Object, Object> properties);
	
	public Number countByProperties(Class<?> entityClass, Map<Object, Object> properties);
}