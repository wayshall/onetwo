package org.onetwo.common.jfishdbm.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/***
 * 对jdbc查询的扩展，查询的sql里可包含命名的参数
 * @author weishao
 *
 */
public class JFishNamedJdbcTemplate extends NamedParameterJdbcTemplate implements NamedJdbcTemplate{

	public JFishNamedJdbcTemplate(DataSource dataSource) {
		super(dataSource);
	}

	public JFishNamedJdbcTemplate(JdbcOperations classicJdbcTemplate) {
		super(classicJdbcTemplate);
	}

	public Object execute(String sql, Map<String, ?> paramMap) throws DataAccessException {
		PreparedStatementCreator pstCreator = getPreparedStatementCreator(sql, new MapSqlParameterSource(paramMap));
		final PreparedStatementSetter setter = (PreparedStatementSetter) pstCreator;
//		System.out.println("sql: " + ((SqlProvider)setter).getSql());
		return execute(sql, new MapSqlParameterSource(paramMap), new PreparedStatementCallback<Object>(){

			@Override
			public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				setter.setValues(ps);
				return ps.execute();
			}
			
		});
	}

}
