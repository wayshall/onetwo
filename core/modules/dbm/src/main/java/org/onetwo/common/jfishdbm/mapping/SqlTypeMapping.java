package org.onetwo.common.jfishdbm.mapping;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Types;

import org.apache.commons.collections.keyvalue.MultiKey;
import org.onetwo.common.jfishdbm.jdbc.type.TypeHandler;
import org.onetwo.common.jfishdbm.utils.DBUtils;
import org.onetwo.common.utils.map.TableMap;


public class SqlTypeMapping {
	
	/*private  final Map<Class<?>, SqlJavaTypeMapper<?>> basicTypes = Maps.newHashMap();
	private final Map<Integer, SqlJavaTypeMapper<?>> sqlJavaTypeMapper = Maps.newHashMap() ;*/
	private final TableMap<Class<?>, Integer, SqlJavaTypeMapper<?>> table = new TableMap<>();
	
	private final SqlJavaTypeMapper<Object> defaultSqlJavaTypeMapper = new SqlJavaTypeMapper<?>();
	
	public SqlTypeMapping() {
		map(int.class, Types.INTEGER);
		map(long.class, Types.BIGINT);
		map(short.class, Types.SMALLINT);
		map(byte.class, Types.TINYINT);
		map(float.class, Types.FLOAT);
		map(double.class, Types.DOUBLE);
		map(boolean.class, Types.BOOLEAN);
		
		map(Integer.class, Types.INTEGER);
		map(Long.class, Types.BIGINT);
		map(Short.class, Types.SMALLINT);
		map(Byte.class, Types.TINYINT);
		map(Float.class, Types.FLOAT);
		map(Double.class, Types.DOUBLE);
		map(Boolean.class, Types.BOOLEAN);
		
		map(String.class, Types.VARCHAR);
		map(BigDecimal.class, Types.NUMERIC);
		map(BigInteger.class, Types.NUMERIC);
		map(Number.class, Types.NUMERIC);
		map(java.util.Date.class, Types.TIMESTAMP);
		map(java.util.Calendar.class, Types.TIMESTAMP);
		map(java.sql.Date.class, Types.DATE);
		map(java.sql.Time.class, Types.TIME);
		map(java.sql.Timestamp.class, Types.TIMESTAMP);
		map(byte[].class, Types.BINARY);
		
	}

	final protected SqlTypeMapping map(Class<?> javaType, int sqlType, TypeHandler<?> typeHandler){
		MultiKey key = new MultiKey(javaType, sqlType);
		SqlJavaTypeMapper<?> mapper = new SqlJavaTypeMapper<>(sqlType, javaType, typeHandler);
		table.put(javaType, sqlType, mapper);
		return this;
	}
	
	public SqlJavaTypeMapper<?> getSqlJavaTypeMapper(Class<?> javaType, int sqlType){
		SqlJavaTypeMapper<?> mapper = table.get(javaType, sqlType);
		if(mapper==null){
			mapper = defaultSqlJavaTypeMapper;
		}
		return mapper;
	}
	
	public TypeHandler<?> getTypeHander(Class<?> javaType, int sqlType){
		return getSqlJavaTypeMapper(javaType, sqlType).getTypeHandler();
	}
	
	public SqlJavaTypeMapper<?> getSqlJavaTypeMapper(int sqlType){
		SqlJavaTypeMapper<?> mapper = sqlJavaTypeMapper.get(sqlType);
		if(mapper==null){
			mapper = defaultSqlJavaTypeMapper;
		}
		return mapper;
	}
	
	public int getType(Class<?> cls){
		Integer type = getSqlJavaTypeMapper(cls).getSqlType();
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
		Class<?> clz = getSqlJavaTypeMapper(sqlType).getJavaType();
		return clz;
	}
	
	public static class OracleSqlTypeMapping  extends SqlTypeMapping {

		public OracleSqlTypeMapping() {
			super();
			map(Boolean.class, Types.NUMERIC);
			map(boolean.class, Types.NUMERIC);
		}
		
	}
	
	public static class SqlJavaTypeMapper<T> {
		final private int sqlType;
		final private Class<?> javaType;
		final private TypeHandler<T> typeHandler;
		public SqlJavaTypeMapper(int sqlType, Class<?> javaType, TypeHandler<T> typeHandler) {
			super();
			this.sqlType = sqlType;
			this.javaType = javaType;
			this.typeHandler = typeHandler;
		}
		public int getSqlType() {
			return sqlType;
		}
		public Class<?> getJavaType() {
			return javaType;
		}
		public TypeHandler<T> getTypeHandler() {
			return typeHandler;
		}
		
	}
	
}
