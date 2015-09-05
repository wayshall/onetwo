package org.onetwo.common.jfishdbm.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.onetwo.common.jfishdbm.exception.DbmException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

abstract public class AbstractRowMapper<T> implements RowMapper<T>{
	
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	public AbstractRowMapper() {
		super();
	}

	abstract protected T newInstance();
	abstract protected void mapValue(T obj, ResultSet rs, String columnName, int index);

	@Override
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();

		String column = null;
		T obj = newInstance();
		for (int index = 1; index <= columnCount; index++) {
			column = JdbcUtils.lookupColumnName(rsmd, index);
			mapValue(obj, rs, column, index);
		}		
		return obj;
	}

	protected <E> E getColumnValue(ResultSet rs, int index, Class<E> type) {
		try {
			return type.cast(JdbcUtils.getResultSetValue(rs, index, type));
		} catch (Exception e) {
			throw new DbmException("get the ["+index+"] column value error: " + e.getMessage(), e);
		}
	}

}
