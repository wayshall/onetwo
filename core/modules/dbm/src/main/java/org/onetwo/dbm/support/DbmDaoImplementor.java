package org.onetwo.dbm.support;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.onetwo.common.db.DataQuery;
import org.onetwo.common.db.DbmQueryValue;
import org.onetwo.common.db.sql.SequenceNameManager;
import org.onetwo.common.db.sqlext.SQLSymbolManager;
import org.onetwo.common.db.sqlext.SelectExtQuery;
import org.onetwo.common.utils.Page;
import org.onetwo.dbm.dialet.DBDialect;
import org.onetwo.dbm.jdbc.DbmJdbcOperations;
import org.onetwo.dbm.jdbc.NamedJdbcTemplate;
import org.onetwo.dbm.mapping.DbmConfig;
import org.onetwo.dbm.mapping.MappedEntryManager;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

public interface DbmDaoImplementor extends DbmDao {

	public MappedEntryManager getMappedEntryManager();
	
	public <T> T findUnique(String sql, Map<String, ?> params, RowMapper<T> rowMapper);
	
	public <T> T findUnique(String sql, Object[] args, RowMapper<T> row);
	
	/**********
	 * 通过<code>JFishQueryValue</code>查询一条数据
	 * JFishQueryValue只是对sql、参数和结果类型的封装
	 * @param queryValue
	 * @return
	 */
	public <T> T findUnique(DbmQueryValue queryValue);
	public <T> T findUnique(DbmQueryValue queryValue, RowMapper<T> row);
	
	public <T> List<T> findList(String sql, Object[] args, RowMapper<T> rowMapper);
	
	public <T> List<T> findList(String sql, Map<String, ?> params, RowMapper<T> rowMapper);
	

	/**********
	 * 通过<code>JFishQueryValue</code>查询数据列表
	 * JFishQueryValue只是对sql、参数和结果类型的封装
	 * @param queryValue
	 * @return
	 */
	public <T> List<T> findList(DbmQueryValue queryValue);
	
	public <T> void findPage(Page<T> page, DbmQueryValue queryValue);
	
	public <T> T find(DbmQueryValue queryValue, ResultSetExtractor<T> rse);
	
	public <T> List<T> findList(DbmQueryValue queryValue, RowMapper<T> rowMapper);
	
	public <T> RowMapper<T> getDefaultRowMapper(Class<T> type);
	
	public DBDialect getDialect();
	
	public int executeUpdate(String sql, Map<String, ?> params);
	
	public int executeUpdate(String sql, Object...args);
	
	public int executeUpdate(DbmQueryValue queryValue);

	public DbmJdbcOperations getJFishJdbcTemplate();
	
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
	 
	public <T> int saveRef(T entity, String... relatedFields);
	public <T> int saveRef(T entity, boolean dropInFirst, String... relatedFields);
	*/
	/*********
	 * 删除实体和关联实体的关系引用<br/>
	 * 如果关联字段为null或者空，忽略<br/>
	 * 如果关联字段还没有保存，抛出{@link JFishEntityNotSavedException}
	 * <br/>
	 * @param entity
	 * @param relatedFields
	 * @return
	
	public <T> int dropRef(T entity, String... relatedFields);
	 */
	/*********
	 * 清除实体和关联实体的关系引用<br/>
	 * 不管关联字段是否有值
	 * <br/>
	 * @param entity
	 * @param relatedFields
	 * @return
	 
	public <T> int clearRef(T entity, String... relatedFields);
	*/
	/****
	 * wrap JFishQuery as a DataQuery
	 * @param extQuery
	 * @return
	 */
	public DataQuery createAsDataQuery(SelectExtQuery extQuery);
	
	public DataQuery createAsDataQuery(String sqlString, Class<?> entityClass);
	
	public DataQuery createAsDataQuery(String sql, Map<String, Object> values);
	public DbmConfig getDataBaseConfig();

	public DataSource getDataSource();
}
