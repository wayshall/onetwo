package org.onetwo.common.fish;

import org.onetwo.common.db.BaseEntityManager;
import org.onetwo.common.fish.spring.JFishDaoImplementor;
import org.onetwo.common.utils.Page;
import org.springframework.jdbc.core.RowMapper;

public interface JFishEntityManager extends BaseEntityManager {
	
	
	public <T> Page<T> findPageByQName(String queryName, RowMapper<T> rowMapper, Page<T> page, Object... params);
	
	public int removeAll(Class<?> entityClass);
	
	public JFishDaoImplementor getJfishDao();
	
	/*******
	 * 级联保存接口
	 * @param entity
	 * @param relatedFields 要保存的级联字段
	 * @return
	 */
	public <T> T saveWith(T entity, String... relatedFields);
	
	/********
	 * 保存实体和关联实体的关系引用<br/>
	 * 如果关联字段为null或者空，忽略<br/>
	 * 如果关联字段还没有保存，抛出{@linkplain org.onetwo.common.fish.exception.JFishEntityNotSavedException JFishEntityNotSavedException}
	 * <br/>
	 * @param entity
	 * @param relatedFields
	 * @return
	 */
	public <T> int saveRef(T entity, String... relatedFields);

	/*********
	 * 删除实体和关联实体的关系引用<br/>
	 * 如果关联字段为null或者空，忽略<br/>
	 * 如果关联字段还没有保存，抛出{@linkplain org.onetwo.common.fish.exception.JFishEntityNotSavedException JFishEntityNotSavedException}
	 * <br/>
	 * @param entity
	 * @param relatedFields
	 * @return
	 */
	public <T> int dropRef(T entity, String... relatedFields);

	/*********
	 * 清除实体和关联实体的关系引用<br/>
	 * 不管关联字段是否有值
	 * <br/>
	 * @param entity
	 * @param relatedFields
	 * @return
	 */
	public <T> int clearRef(T entity, String... relatedFields);
	
	public JFishQueryBuilder createQueryBuilder(Class<?> entityClass);
	
}
