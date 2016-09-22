package org.onetwo.dbm.jdbc;

import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

/***
 * 对jdbc查询的扩展，查询的sql里可包含命名的参数
 * @author weishao
 *
 */
public interface NamedJdbcTemplate extends NamedParameterJdbcOperations{

	public Object execute(String sql, Map<String, ?> paramMap) throws DataAccessException ;

}
