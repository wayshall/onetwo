package org.onetwo.common.jfish;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.onetwo.common.utils.DateUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

/***
 * 对jdbc查询的扩展，查询的sql里可包含命名的参数
 * @author weishao
 *
 */
@SuppressWarnings("unchecked")
public class NamedJdbcTemplateTest extends NamedParameterJdbcTemplate{

	public NamedJdbcTemplateTest(DataSource dataSource) {
		super(dataSource);
	}

	public NamedJdbcTemplateTest(JdbcOperations classicJdbcTemplate) {
		super(classicJdbcTemplate);
	}

	public Object execute(String sql, Map<String, ?> paramMap) throws DataAccessException {
		PreparedStatementCreator pstCreator = getPreparedStatementCreator(sql, new MapSqlParameterSource(paramMap));
		final PreparedStatementSetter setter = (PreparedStatementSetter) pstCreator;
//		System.out.println("sql: " + ((SqlProvider)setter).getSql());
		return execute(sql, new MapSqlParameterSource(paramMap), new PreparedStatementCallback(){

			@Override
			public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				setter.setValues(ps);
				return ps.execute();
			}
			
		});
	}
	
	public void test(){
		String sql = "select * from user t where t.user_name = :userName and t.age = ? and t.birthday=:birthDay limit ?, ?";
		Map map = new HashMap();
		map.put("userName", "test");
		map.put("birthDay", DateUtil.now());
		SqlParameterSource paramSource = new MapSqlParameterSource(map);
		getPreparedStatementCreator(sql, paramSource);
	}
	
	public static void main(String[] args){
		NamedJdbcTemplateTest a = new NamedJdbcTemplateTest(new DataSource() {
			
			@Override
			public <T> T unwrap(Class<T> iface) throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public boolean isWrapperFor(Class<?> iface) throws SQLException {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void setLoginTimeout(int seconds) throws SQLException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setLogWriter(PrintWriter out) throws SQLException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public int getLoginTimeout() throws SQLException {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public PrintWriter getLogWriter() throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Connection getConnection(String username, String password) throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Connection getConnection() throws SQLException {
				// TODO Auto-generated method stub
				return null;
			}

			public Logger getParentLogger() throws SQLFeatureNotSupportedException {
				// TODO Auto-generated method stub
				return null;
			}
		});
		a.test();
	}

}
