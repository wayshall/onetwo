package org.onetwo.common.db.wheel;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

public class SimplePreparedStatementSetter implements PreparedStatementSetter{
	
	public void setParameter(PreparedStatement preStatement, int index, Object value) throws SQLException{
//		Assert.notNull(value);

		if(value==null){
			preStatement.setNull(index, Types.NULL);
//			preStatement.setTimestamp(index, null);
			return ;
		}
		if(Short.class.isAssignableFrom(value.getClass())){
			preStatement.setShort(index, (Short)value);
		}
		else if(Integer.class.isAssignableFrom(value.getClass())){
			preStatement.setInt(index, (Integer)value);
		}
		else if(Byte.class.isAssignableFrom(value.getClass())){
			preStatement.setByte(index, (Byte)value);
		}
		else if(Long.class.isAssignableFrom(value.getClass())){
			preStatement.setLong(index, (Long)value);
		}
		else if(String.class.isAssignableFrom(value.getClass())){
			preStatement.setString(index, (String)value);
		}
		else if(Float.class.isAssignableFrom(value.getClass())){
			preStatement.setFloat(index, (Float)value);
		}
		else if(Double.class.isAssignableFrom(value.getClass())){
			preStatement.setDouble(index, (Double)value);
		}
		else if(BigDecimal.class.isAssignableFrom(value.getClass())){
			preStatement.setBigDecimal(index, (BigDecimal)value);
		}
		else if(java.sql.Date.class.isAssignableFrom(value.getClass())){
			preStatement.setDate(index, (java.sql.Date)value);
		}
		else if(java.sql.Time.class.isAssignableFrom(value.getClass())){
			preStatement.setTime(index, (java.sql.Time)value);
		}
		else if(java.sql.Timestamp.class.isAssignableFrom(value.getClass())){
			preStatement.setTimestamp(index, (java.sql.Timestamp)value);
		}
		else if(Date.class.isAssignableFrom(value.getClass())){
			preStatement.setTimestamp(index, new java.sql.Timestamp(((Date)value).getTime()));
		}
		else if(Number.class.isAssignableFrom(value.getClass())){
			preStatement.setObject(index, value, Types.NUMERIC);
		}else{
			preStatement.setObject(index, value);
		}
	}
	
}
