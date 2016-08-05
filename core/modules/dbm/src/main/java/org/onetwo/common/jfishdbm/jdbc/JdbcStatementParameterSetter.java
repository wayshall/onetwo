package org.onetwo.common.jfishdbm.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.SqlParameter;

public interface JdbcStatementParameterSetter {
	
	public void setParameterValue(PreparedStatement psToUse, int paramIndex, SqlParameter declaredParameter, Object inValue) throws SQLException;
	
	public void setParameterValue(PreparedStatement ps, int parameterPosition, Object argValue) throws SQLException;
	
	public void setParameterValue(PreparedStatement ps, int parameterPosition, int argType, Object argValue) throws SQLException;
}
