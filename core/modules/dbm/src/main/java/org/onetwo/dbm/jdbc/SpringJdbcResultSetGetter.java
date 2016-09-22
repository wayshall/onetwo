package org.onetwo.dbm.jdbc;

import java.beans.PropertyDescriptor;
import java.sql.SQLException;

import org.onetwo.common.convert.Types;
import org.onetwo.dbm.mapping.DbmMappedField;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;

public class SpringJdbcResultSetGetter implements JdbcResultSetGetter {

	@Override
	public Object getColumnValue(ResultSetWrappingSqlRowSet rs, int index, PropertyDescriptor pd) throws SQLException {
//		JFishProperty jproperty = Intro.wrap(pd.getWriteMethod().getDeclaringClass()).getJFishProperty(pd.getName(), false);
		Object value = JdbcUtils.getResultSetValue(rs.getResultSet(), index, pd.getPropertyType());
		return value;
	}

	@Override
	public Object getColumnValue(ResultSetWrappingSqlRowSet rs, int index, DbmMappedField field) throws SQLException {
		Object value = JdbcUtils.getResultSetValue(rs.getResultSet(), index, field.getColumnType());
		if(!field.getColumnType().equals(field.getPropertyInfo().getType())){
			value = Types.convertValue(value, field.getPropertyInfo().getType());
		}
		return value;
	}
	
	

}
