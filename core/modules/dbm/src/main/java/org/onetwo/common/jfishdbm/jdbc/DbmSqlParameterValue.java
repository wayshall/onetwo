package org.onetwo.common.jfishdbm.jdbc;

import org.onetwo.common.jfishdbm.utils.DbmUtils;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlParameterValue;

public class DbmSqlParameterValue extends SqlParameterValue {
	
	public DbmSqlParameterValue(int sqlType, int scale, Object value) {
		super(sqlType, scale, value);
	}

	public DbmSqlParameterValue(int sqlType, Object value) {
		super(sqlType, value);
	}

	public DbmSqlParameterValue(int sqlType, String typeName, Object value) {
		super(sqlType, typeName, value);
	}

	public DbmSqlParameterValue(SqlParameter declaredParam, Object value) {
		super(declaredParam, value);
	}
	
	@Override
	public Object getValue() {
		Object value = super.getValue();
		value = unwrapSqlParameterValue(value);
		return value;
	}

	public static Object unwrapSqlParameterValue(Object value){
		return DbmUtils.getActualSqlValue(value);
	}
}
