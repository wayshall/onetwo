package org.onetwo.common.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.onetwo.common.db.sql.DynamicQuery;
import org.onetwo.common.db.sql.DynamicQueryFactory;
import org.onetwo.common.db.sql.SQLFile;
import org.onetwo.common.utils.Page;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.util.Assert;


/****
 * 基于jdbc的数据访问基类
 * 
 * @author weishao
 *
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class JdbcDao extends JdbcDaoSupport {
	
	protected SQLFile sqlFileInst;
	
	public JdbcDao(){
	}

	public SQLFile getSqlFileInst() {
		return sqlFileInst;
	}

	public void setSqlFileInst(SQLFile sqlFileInst) {
		this.sqlFileInst = sqlFileInst;
	}

	public int updateByName(String queryName, Object[] objects){
		DynamicQuery query = getSqlFileInst().createQuery(queryName, objects);
		return this.updateBySQL(query.getTransitionSql(), query.getValues().toArray());
	}

	public int updateByName(String queryName, Map paramMap){
		String sql = getSqlFileInst().getSqlOnly(queryName);
		return (Integer)getNamedParameterJdbcTemplate().update(sql, paramMap);
	}

	public void execute(String sql, Map paramMap){
		getNamedParameterJdbcTemplate().execute(sql, paramMap);
	}
	
	public void execute(String sql){
		this.getJdbcTemplate().execute(sql);
	}
	
	public List query(String sql, Map paramMap){
		if(paramMap==null || paramMap.isEmpty())
			return this.getJdbcTemplate().queryForList(sql);
		else
			return getNamedParameterJdbcTemplate().queryForList(sql, paramMap);
	}
	
	public long queryForLong(String sql) {
		return this.getJdbcTemplate().queryForLong(sql);
	}
	
	public long queryForLong(String sql, Object...values) {
		return this.getJdbcTemplate().queryForLong(sql, values);
	}
	
	public long queryForLong(String sql, Map params) {
		Number number = (Number) getNamedParameterJdbcTemplate().queryForObject(sql, new MapSqlParameterSource(params), getSingleColumnRowMapper(Long.class));
		return number.longValue();
	}
	
	public List query(String sql, Class clazz, Object... args){
		return getJdbcTemplate().query(sql, args, getBeanPropertyRowMapper(clazz));
	}
	
	public List query(String sql, Object[] args, RowMapper rowMapper) {
		return getJdbcTemplate().query(sql, args, rowMapper);
	}

	public List queryForList(String sql, Object[] args){
		return getJdbcTemplate().queryForList(sql, args);
	}
 
	public int updateBySQL(String sql, Object... args) {
		return this.getJdbcTemplate().update(sql, args);
	}

	public void delete(String tableName, Map<String, Object> params) {
		Assert.notEmpty(params);
		StringBuffer sql = new StringBuffer();
		sql.append("delete from ").append(tableName).append(" where 1=1 ");

		List values = new ArrayList();
		if (params != null) {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				sql.append(" and ").append(entry.getKey()).append(" = ? ");
				values.add(entry.getValue());
			}
		}

		this.updateBySQL(sql.toString(), values.toArray());
	}
	
	protected RowMapper getSingleColumnRowMapper(Class requiredType) {
		return new SingleColumnRowMapper(requiredType);
	}
	public List namedQuery(String queryName, Object... objects){
		return namedQuery(queryName, objects, getColumnMapRowMapper());
	}
	public List namedQuery(String queryName, Class clazz, Object... objects){
		return namedQuery(queryName, objects, this.getBeanPropertyRowMapper(clazz));
	}
	
	public List namedQuery(String queryName, Object[] objects, RowMapper mapper){
		DynamicQuery query = getSqlFileInst().createQuery(queryName, objects);
		return getJdbcTemplate().query(query.getTransitionSql(), query.getValues().toArray(), mapper);
	}
	
	public List namedQuery(String queryName, Map<String, Object> params){
		return this.namedQuery(queryName, params, this.getColumnMapRowMapper());
	}
	
	public List namedQuery(String queryName, Map<String, Object> params, RowMapper mapper){
		DynamicQuery query = getSqlFileInst().createQuery(queryName, params);
		return getJdbcTemplate().query(query.getTransitionSql(), query.getValues().toArray(), mapper);
	}

	
	public void findPageBySQL(Page page, String sql, Map<String, Object> params){
		DynamicQuery query = DynamicQueryFactory.create(sql);
		query.setParameters(params);
		this.findPageByQuery(page, query, null);
	}
	
	public void findPageByNamedQuery(Page page, String queryName, Map<String, Object> params){
		this.findPageByNamedQuery(page, queryName, params, this.getColumnMapRowMapper());
	}
	
	public void findPageByNamedQuery(Page page, String queryName, Map<String, Object> params, RowMapper mapper){
		DynamicQuery query = getSqlFileInst().createQuery(queryName, params);
		/*if(params!=null && !params.isEmpty()){
			if(params.containsKey(K.ASC)){
				query.setAsc(params.get(K.ASC).toString());
			}
			if(params.containsKey(K.DESC)){
				query.setDesc(params.get(K.DESC).toString());
			}
		}*/
		this.findPageByQuery(page, query, mapper);
	}
	
	public void findPageByQuery(Page page, DynamicQuery query, RowMapper mapper){
		query.setFirstRecord(page.getFirst()-1).setMaxRecord(page.getPageSize());
		/*if (Page.ASC.equals(page.getOrder()) && StringUtils.isNotBlank(page.getOrderBy())) {
			query.setAsc(page.getOrderBy());
		}

		if (Page.DESC.equals(page.getOrder()) && StringUtils.isNotBlank(page.getOrderBy())) {
			query.setDesc(page.getOrderBy());
		}*/
		query.compile();
		long totalCount = ((Number)this.getJdbcTemplate().queryForObject(query.getCountSql(), query.getValues().toArray(), Number.class)).longValue();
		page.setTotalCount(totalCount);
		if(totalCount>0){
			if(mapper == null)
				mapper = this.getColumnMapRowMapper();
			List result = getJdbcTemplate().query(query.getTransitionSql(), query.getValues().toArray(), mapper);
			page.setResult(result);
		}
	}
	
	protected RowMapper getColumnMapRowMapper() {
		return new ColumnMapRowMapper();
	}
	
	protected RowMapper getBeanPropertyRowMapper(Class entityClass) {
		return new BeanPropertyRowMapper(entityClass);
	}
	
}
