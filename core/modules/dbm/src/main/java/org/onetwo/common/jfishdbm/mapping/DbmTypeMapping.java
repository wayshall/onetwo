package org.onetwo.common.jfishdbm.mapping;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.JDBCType;
import java.sql.Types;
import java.util.Map;

import org.onetwo.common.jfishdbm.jdbc.type.TypeHandler;
import org.onetwo.common.jfishdbm.jdbc.type.impl.IntegerTypeHandler;
import org.onetwo.common.jfishdbm.jdbc.type.impl.LongTypeHandler;
import org.onetwo.common.jfishdbm.utils.DBUtils;
import org.onetwo.common.utils.map.TableMap;

import com.google.common.collect.Maps;


@SuppressWarnings("unchecked")
public class DbmTypeMapping {
	
	/*private  final Map<Class<?>, SqlJavaTypeMapper<?>> basicTypes = Maps.newHashMap();
	private final Map<Integer, SqlJavaTypeMapper<?>> sqlJavaTypeMapper = Maps.newHashMap() ;*/
	private final TableMap<Class<?>, JDBCType, TypeHandler<?>> typeHandlerMapping = new TableMap<>();
//	private final Map<Class<?>, JDBCType> typeMapping = Maps.newHashMap();
	private final Map<Class<?>, TypeHandler<?>> javaTypeHandlerMapping = Maps.newHashMap();
	
//	private final JdbcJavaTypeMapper<Object> defaultSqlJavaTypeMapper = new JdbcJavaTypeMapper<?>();
	private final TypeHandler<Object> defaultTypeHandler = new DefaultTypeHandler();
	
	public DbmTypeMapping() {
		map(int.class, JDBCType.INTEGER, new IntegerTypeHandler());
		map(Integer.class, JDBCType.INTEGER, new IntegerTypeHandler());
		map(long.class, JDBCType.BIGINT, new LongTypeHandler());
		map(Long.class, JDBCType.BIGINT, new LongTypeHandler());
		map(short.class, Types.SMALLINT);
		map(byte.class, Types.TINYINT);
		map(float.class, Types.FLOAT);
		map(double.class, Types.DOUBLE);
		map(boolean.class, Types.BOOLEAN);
		
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

	final protected DbmTypeMapping map(Class<?> javaType, JDBCType sqlType, TypeHandler<?> typeHandler){
		typeHandlerMapping.put(javaType, sqlType, typeHandler);
//		typeMapping.put(javaType, sqlType);
		return this;
	}
	
	/*public JdbcJavaTypeMapper<?> getJdbcJavaTypeMapper(Class<?> javaType, JDBCType sqlType){
		JdbcJavaTypeMapper<?> mapper = table.get(javaType, sqlType);
		if(mapper==null){
			mapper = defaultSqlJavaTypeMapper;
		}
		return mapper;
	}*/
	
	public <T> TypeHandler<T> getTypeHander(Class<T> javaType, JDBCType sqlType){
		TypeHandler<?> typeHandler =  typeHandlerMapping.get(javaType, sqlType);
		if(typeHandler==null){
			typeHandler = this.defaultTypeHandler;
		}
		return (TypeHandler<T>)typeHandler;
	}
	
	public <T> TypeHandler<T> getTypeHander(Class<T> javaType){
		TypeHandler<?> typeHandler = javaTypeHandlerMapping.get(javaType);
		if(typeHandler==null){
			typeHandler = this.defaultTypeHandler;
		}
		return (TypeHandler<T>)typeHandler;
	}
	
	/*public <T> TypeHandler<T> getTypeHanderByObject(Object value){
		if(value==null){
			return (TypeHandler<T>)this.defaultTypeHandler;
		}
		TypeHandler<?> typeHandler = javaTypeHandlerMapping.get(value.getClass());
		if(typeHandler==null){
			typeHandler = this.defaultTypeHandler;
		}
		return (TypeHandler<T>)typeHandler;
	}*/
	
	public int getType(Object value){
		if(value==null){
			return DBUtils.TYPE_UNKNOW;
		}
		return getType(value.getClass());
	}
	
	/*public Class<?> getJavaType(JDBCType jdbcType){
		return this.typeMapping.get(jdbcType);
	}*/
	
	
	public static class OracleSqlTypeMapping  extends DbmTypeMapping {

		public OracleSqlTypeMapping() {
			super();
			map(Boolean.class, Types.NUMERIC);
			map(boolean.class, Types.NUMERIC);
		}
		
	}
	
	/*public static class JdbcJavaTypeMapper<T> {
		final private JDBCType jdbcType;
		final private Class<?> javaType;
		final private TypeHandler<T> typeHandler;
		public JdbcJavaTypeMapper(JDBCType sqlType, Class<?> javaType, TypeHandler<T> typeHandler) {
			super();
			this.jdbcType = sqlType;
			this.javaType = javaType;
			this.typeHandler = typeHandler;
		}
		public JDBCType getJdbcType() {
			return jdbcType;
		}
		public Class<?> getJavaType() {
			return javaType;
		}
		public TypeHandler<T> getTypeHandler() {
			return typeHandler;
		}
		
	}*/
	
}
