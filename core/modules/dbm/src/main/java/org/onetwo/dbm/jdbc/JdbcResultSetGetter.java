package org.onetwo.dbm.jdbc;

import java.beans.PropertyDescriptor;
import java.sql.SQLException;

import org.onetwo.dbm.mapping.DbmMappedField;
import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;

public interface JdbcResultSetGetter {
	
	public Object getColumnValue(ResultSetWrappingSqlRowSet rs, int index, PropertyDescriptor pd) throws SQLException;

	public Object getColumnValue(ResultSetWrappingSqlRowSet rs, int index, DbmMappedField field) throws SQLException;
}
