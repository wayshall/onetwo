package org.onetwo.common.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.onetwo.common.utils.LangUtils;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.SqlProvider;
import org.springframework.util.Assert;

public class SimpleArgsPreparedStatementCreator implements PreparedStatementCreator, SqlProvider {

	private final String sql;
	private final Object[] args;
	private String[] columnNames;
	
	public SimpleArgsPreparedStatementCreator(String sql, Object[] args) {
		this(sql, args, (String[])null);
	}

	public SimpleArgsPreparedStatementCreator(String sql, Object[] args, String... columnNames) {
		Assert.notNull(sql, "SQL must not be null");
		this.sql = sql;
		this.args = args;
		this.columnNames = columnNames;
	}

	public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
		if(LangUtils.hasElement(columnNames))
			return con.prepareStatement(sql, columnNames);
		else
			return con.prepareStatement(sql);
	}

	public String getSql() {
		return this.sql;
	}

	public Object[] getArgs() {
		return args;
	}
}
