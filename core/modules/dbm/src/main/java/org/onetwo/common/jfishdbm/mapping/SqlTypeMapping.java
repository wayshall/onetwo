package org.onetwo.common.jfishdbm.mapping;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Types;
import java.util.Date;
import java.util.Map;

import org.onetwo.common.jfishdbm.utils.DBUtils;

import com.google.common.collect.Maps;


public class SqlTypeMapping {
	
	private  final Map<Class<?>, Integer> basicTypes = Maps.newHashMap();
	
	private final Map<Integer, Class<?>> sqlTypeMapping = Maps.newHashMap() ;
	
	public SqlTypeMapping() {
		sqlToJava(Types.INTEGER, Integer.class);
		sqlToJava(Types.BIGINT, Long.class);
		sqlToJava(Types.CHAR, String.class);
		sqlToJava(Types.DECIMAL, Long.class);
		sqlToJava(Types.NUMERIC, Number.class);
		sqlToJava(Types.VARCHAR, String.class);
		sqlToJava(Types.LONGVARCHAR, String.class);
		sqlToJava(Types.CLOB, String.class);
		sqlToJava(Types.FLOAT, Float.class);
		sqlToJava(Types.REAL, Float.class);
		sqlToJava(Types.DOUBLE, Double.class);
		sqlToJava(Types.BOOLEAN, Boolean.class);
		sqlToJava(Types.SMALLINT, Short.class);
		sqlToJava(Types.TINYINT, Byte.class);
		sqlToJava(Types.DATE, Date.class); 
		sqlToJava(Types.TIME, Date.class);
		sqlToJava(Types.TIMESTAMP, Date.class);
		
		
		
		javaToSql(int.class, Types.INTEGER);
		javaToSql(long.class, Types.BIGINT);
		javaToSql(short.class, Types.SMALLINT);
		javaToSql(byte.class, Types.TINYINT);
		javaToSql(float.class, Types.FLOAT);
		javaToSql(double.class, Types.DOUBLE);
		javaToSql(boolean.class, Types.BOOLEAN);
		
		javaToSql(Integer.class, Types.INTEGER);
		javaToSql(Long.class, Types.BIGINT);
		javaToSql(Short.class, Types.SMALLINT);
		javaToSql(Byte.class, Types.TINYINT);
		javaToSql(Float.class, Types.FLOAT);
		javaToSql(Double.class, Types.DOUBLE);
		javaToSql(Boolean.class, Types.BOOLEAN);
		
		javaToSql(String.class, Types.VARCHAR);
		javaToSql(BigDecimal.class, Types.NUMERIC);
		javaToSql(BigInteger.class, Types.NUMERIC);
		javaToSql(Number.class, Types.NUMERIC);
		javaToSql(java.util.Date.class, Types.TIMESTAMP);
		javaToSql(java.util.Calendar.class, Types.TIMESTAMP);
		javaToSql(java.sql.Date.class, Types.DATE);
		javaToSql(java.sql.Time.class, Types.TIME);
		javaToSql(java.sql.Timestamp.class, Types.TIMESTAMP);
		javaToSql(byte[].class, Types.BINARY);
		
	}

	final protected SqlTypeMapping javaToSql(Class<?> javaType, int sqlType){
		basicTypes.put(javaType, sqlType);
		return this;
	}
	final protected SqlTypeMapping sqlToJava(int sqlType, Class<?> javaType){
		sqlTypeMapping.put(sqlType, javaType);
		return this;
	}
	
	public int getType(Class<?> cls){
		Integer type = basicTypes.get(cls);
		if(type==null){
			type = new Integer(DBUtils.TYPE_UNKNOW);
		}
		return type.intValue();
	}
	
	public int getType(Object value){
		if(value==null){
			return DBUtils.TYPE_UNKNOW;
		}
		return getType(value.getClass());
	}
	
	public Class<?> getJavaType(int sqlType){
		Class<?> clz = sqlTypeMapping.get(sqlType);
		/*if(clz==null)
			return String.class;*/
		return clz;
	}
	
	public static class OracleSqlTypeMapping  extends SqlTypeMapping {

		public OracleSqlTypeMapping() {
			super();
			javaToSql(Boolean.class, Types.NUMERIC);
			javaToSql(boolean.class, Types.NUMERIC);
		}
		
	}
	
}
