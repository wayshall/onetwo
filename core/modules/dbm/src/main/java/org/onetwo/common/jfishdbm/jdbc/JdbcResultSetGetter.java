package org.onetwo.common.jfishdbm.jdbc;

import java.beans.PropertyDescriptor;
import java.sql.SQLException;

import org.onetwo.common.jfishdbm.mapping.DbmMappedField;
import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;

public interface JdbcResultSetGetter {
	
	public Object getColumnValue(ResultSetWrappingSqlRowSet rs, int index, PropertyDescriptor pd) throws SQLException;

	public Object getColumnValue(ResultSetWrappingSqlRowSet rs, int index, DbmMappedField field) throws SQLException;
}
