package org.onetwo.dbm.mapping;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.JDBCType;
import java.util.Map;

import org.onetwo.dbm.utils.DBUtils;

import com.google.common.collect.Maps;


@SuppressWarnings("unchecked")
public class DbmTypeMapping {
	
	/*private  final Map<Class<?>, SqlJavaTypeMapper<?>> basicTypes = Maps.newHashMap();
	private final Map<Integer, SqlJavaTypeMapper<?>> sqlJavaTypeMapper = Maps.newHashMap() ;*/
//	private final TableMap<Class<?>, JDBCType, TypeHandler<?>> typeHandlerMapping = new TableMap<>();
	private final Map<Class<?>, JDBCType> typeMapping = Maps.newHashMap();
//	private final Map<Class<?>, TypeHandler<?>> javaTypeHandlerMapping = Maps.newHashMap();
	
//	private final JdbcJavaTypeMapper<Object> defaultSqlJavaTypeMapper = new JdbcJavaTypeMapper<?>();
//	private final TypeHandler<Object> defaultTypeHandler = new DefaultTypeHandler();
	
	public DbmTypeMapping() {
		map(int.class, JDBCType.INTEGER);
		map(Integer.class, JDBCType.INTEGER);
		map(long.class, JDBCType.BIGINT);
		map(Long.class, JDBCType.BIGINT);
		map(short.class, JDBCType.SMALLINT);
		map(byte.class, JDBCType.TINYINT);
		map(float.class, JDBCType.FLOAT);
		map(double.class, JDBCType.DOUBLE);
		map(boolean.class, JDBCType.BOOLEAN);
		
		map(Short.class, JDBCType.SMALLINT);
		map(Byte.class, JDBCType.TINYINT);
		map(Float.class, JDBCType.FLOAT);
		map(Double.class, JDBCType.DOUBLE);
		map(Boolean.class, JDBCType.BOOLEAN);
		
		map(String.class, JDBCType.VARCHAR);
		map(BigDecimal.class, JDBCType.NUMERIC);
		map(BigInteger.class, JDBCType.NUMERIC);
		map(Number.class, JDBCType.NUMERIC);
		map(java.util.Date.class, JDBCType.TIMESTAMP);
		map(java.util.Calendar.class, JDBCType.TIMESTAMP);
		map(java.sql.Date.class, JDBCType.DATE);
		map(java.sql.Time.class, JDBCType.TIME);
		map(java.sql.Timestamp.class, JDBCType.TIMESTAMP);
		map(byte[].class, JDBCType.BINARY);
		
	}

	final protected DbmTypeMapping map(Class<?> javaType, JDBCType sqlType){
		typeMapping.put(javaType, sqlType);
		return this;
	}
	
	public int getType(Object value){
		if(value==null){
			return DBUtils.TYPE_UNKNOW;
		}
		JDBCType jdbcType = typeMapping.get(value.getClass());
		if(jdbcType==null){
			return DBUtils.TYPE_UNKNOW;
		}
		return jdbcType.getVendorTypeNumber();
	}
	
	/*public Class<?> getJavaType(JDBCType jdbcType){
		return typeMapping.get(jdbcType);
	}*/
	
	
	public static class OracleSqlTypeMapping  extends DbmTypeMapping {

		public OracleSqlTypeMapping() {
			super();
			map(Boolean.class, JDBCType.NUMERIC);
			map(boolean.class, JDBCType.NUMERIC);
		}
		
	}
	
}
