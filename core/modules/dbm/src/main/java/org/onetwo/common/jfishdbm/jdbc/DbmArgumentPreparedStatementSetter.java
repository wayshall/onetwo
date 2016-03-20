package org.onetwo.common.jfishdbm.jdbc;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.onetwo.common.date.Dates;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.core.StatementCreatorUtils;

public class DbmArgumentPreparedStatementSetter extends ArgumentPreparedStatementSetter {

	public DbmArgumentPreparedStatementSetter(Object[] args) {
		super(args);
	}

	protected void doSetValue(PreparedStatement ps, int parameterPosition, Object argValue) throws SQLException {
		if (argValue instanceof SqlParameterValue) {
			SqlParameterValue paramValue = (SqlParameterValue) argValue;
			StatementCreatorUtils.setParameterValue(ps, parameterPosition, paramValue, getActualValue(paramValue.getValue()));
		}
		else {
			StatementCreatorUtils.setParameterValue(ps, parameterPosition, SqlTypeValue.TYPE_UNKNOWN, getActualValue(argValue));
		}
	}
	
	public static Object getActualValue(Object value){
		if(SqlParameterValue.class.isInstance(value)){
			return ((SqlParameterValue)value).getValue();
		}else if(Enum.class.isInstance(value)){
			return ((Enum<?>)value).name();
		}else if(value instanceof LocalDate){
			final LocalDate localDate = (LocalDate) value;
			return new SqlTypeValue(){

				@Override
				public void setTypeValue(PreparedStatement ps, int paramIndex, int sqlType, String typeName) throws SQLException {
					ps.setDate(paramIndex, new Date(Dates.toDate(localDate).getTime()));
				}
				
			};
		}else if(value instanceof LocalDateTime){
			final LocalDateTime localDateTime = (LocalDateTime) value;
			return new SqlTypeValue(){

				@Override
				public void setTypeValue(PreparedStatement ps, int paramIndex, int sqlType, String typeName) throws SQLException {
					ps.setTimestamp(paramIndex, new Timestamp(Dates.toDate(localDateTime).getTime()));
				}
				
			};
			
		}
		return value;
	}
}
