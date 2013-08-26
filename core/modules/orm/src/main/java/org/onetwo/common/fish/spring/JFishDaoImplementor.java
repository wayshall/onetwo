package org.onetwo.common.fish.spring;

import java.util.List;
import java.util.Map;

import org.onetwo.common.db.DataQuery;
import org.onetwo.common.db.JFishQueryValue;
import org.onetwo.common.db.SelectExtQuery;
import org.onetwo.common.db.sql.SequenceNameManager;
import org.onetwo.common.db.sqlext.SQLSymbolManager;
import org.onetwo.common.fish.exception.JFishEntityNotSavedException;
import org.onetwo.common.fish.orm.DBDialect;
import org.onetwo.common.fish.orm.MappedEntryManager;
import org.onetwo.common.jdbc.JFishJdbcOperations;
import org.onetwo.common.jdbc.NamedJdbcTemplate;
import org.onetwo.common.utils.Page;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

public interface JFishDaoImplementor extends JFishDao {

	public MappedEntryManager getMappedEntryManager();
	public JFishNamedFileQueryManager getJfishFileQueryFactory();
	
	public <T> T findUnique(String sql, Map<String, ?> params, RowMapper<T> rowMapper);
	
	public <T> T findUnique(String sql, Object[] args, RowMapper<T> row);
	
	/**********
	 * 通过<code>JFishQueryValue</code>查询一条数据
	 * JFishQueryValue只是对sql、参数和结果类型的封装
	 * @param queryValue
	 * @return
	 */
	public <T> T findUnique(JFishQueryValue queryValue);
	public <T> T findUnique(JFishQueryValue queryValue, RowMapper<T> row);
	
	public <T> List<T> findList(String sql, Object[] args, RowMapper<T> rowMapper);
	
	public <T> List<T> findList(String sql, Map<String, ?> params, RowMapper<T> rowMapper);
	

	/**********
	 * 通过<code>JFishQueryValue</code>查询数据列表
	 * JFishQueryValue只是对sql、参数和结果类型的封装
	 * @param queryValue
	 * @return
	 */
	public <T> List<T> findList(JFishQueryValue queryValue);
	
	public <T> void findPage(Page<T> page, JFishQueryValue queryValue);
	
	public <T> T find(JFishQueryValue queryValue, ResultSetExtractor<T> rse);
	
	public <T> List<T> findList(JFishQueryValue queryValue, RowMapper<T> rowMapper);
	
	public <T> RowMapper<T> getDefaultRowMapper(Class<T> type);
	
	public DBDialect getDialect();
	
	public int executeUpdate(String sql, Map<String, ?> params);
	
	public int executeUpdate(String sql, Object...args);
	
	public int executeUpdate(JFishQueryValue queryValue);

	public JFishJdbcOperations getJFishJdbcTemplate();
	
	public NamedJdbcTemplate getNamedParameterJdbcTemplate();
	
	public SQLSymbolManager getSqlSymbolManager();
	
	public SequenceNameManager getSequenceNameManager();
//	public EntityManagerOperationImpl getEntityManagerWraper();
	
	/********
	 * 保存实体和关联实体的关系引用<br/>
	 * 如果关联字段为null或者空，忽略<br/>
	 * 如果关联字段还没有保存，抛出{@link JFishEntityNotSavedException}
	 * <br/>
	 * @param entity
	 * @param relatedFields
	 * @return
	 */
	public <T> int saveRef(T entity, String... relatedFields);
	public <T> int saveRef(T entity, boolean dropInFirst, String... relatedFields);
	
	/*********
	 * 删除实体和关联实体的关系引用<br/>
	 * 如果关联字段为null或者空，忽略<br/>
	 * 如果关联字段还没有保存，抛出{@link JFishEntityNotSavedException}
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
	
	/****
	 * wrap JFishQuery as a DataQuery
	 * @param extQuery
	 * @return
	 */
	public DataQuery createAsDataQuery(SelectExtQuery extQuery);
	
	public DataQuery createAsDataQuery(String sqlString, Class<?> entityClass);
	
	public DataQuery createAsDataQuery(String sql, Map<String, Object> values);
	
}
