package org.onetwo.plugins.codegen.db;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Types;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public final class SqlTypeFactory {
	public static enum DataBase {
		MySQL("mysql"),
		Oracle("oracle"),
		Sqlserver("sqlserver"),
		Access("access"),
		H2("h2"),
		Unknow("unknow");
		
		private String name;
		
		DataBase(String name){
			this.name = name;
		}

		public String getName() {
			return name;
		}
		
		public boolean isOracle(){
			return this.equals(Oracle);
		}
		
		public boolean isMySQL(){
			return this.equals(MySQL);
		}
		
		public static DataBase of(String name){
			name = name.toLowerCase();
			for(DataBase db : values()){
				if(name.indexOf(db.name)!=-1){
					return db;
				}
			}
			return Unknow;
		}
		
	}

	public static final int TYPE_UNKNOW = DBUtils.TYPE_UNKNOW;
	
	public static final Map<Class<?>, Integer> BASIC_TYPES;
	
	public static final Map<Integer, Class<?>> SQL_TYPE_TO_JAVA_TYPE ;
	
	static {
		Map<Integer, Class<?>> types = new HashMap<Integer, Class<?>>();
		types.put(Types.INTEGER, Integer.class);
		types.put(Types.BIGINT, Long.class);
		types.put(Types.CHAR, String.class);
		types.put(Types.DECIMAL, Long.class);
		types.put(Types.NUMERIC, Number.class);
		types.put(Types.VARCHAR, String.class);
		types.put(Types.LONGVARCHAR, String.class);
		types.put(Types.CLOB, String.class);
		types.put(Types.FLOAT, Float.class);
		types.put(Types.REAL, Float.class);
		types.put(Types.DOUBLE, Double.class);
		types.put(Types.BOOLEAN, Boolean.class);
		types.put(Types.SMALLINT, Short.class);
		types.put(Types.TINYINT, Byte.class);
		types.put(Types.DATE, Date.class); 
		types.put(Types.TIME, Date.class);
		types.put(Types.TIMESTAMP, Date.class);
		types.put(Types.BIT, boolean.class);
		SQL_TYPE_TO_JAVA_TYPE = Collections.unmodifiableMap(types);
		
		
		Map<Class<?>, Integer> basicTypes = new HashMap<Class<?>, Integer>();
		basicTypes.put(int.class, Types.INTEGER);
		basicTypes.put(long.class, Types.BIGINT);
		basicTypes.put(short.class, Types.SMALLINT);
		basicTypes.put(byte.class, Types.TINYINT);
		basicTypes.put(float.class, Types.FLOAT);
		basicTypes.put(double.class, Types.DOUBLE);
		basicTypes.put(boolean.class, Types.BOOLEAN);
		
		basicTypes.put(Integer.class, Types.INTEGER);
		basicTypes.put(Long.class, Types.BIGINT);
		basicTypes.put(Short.class, Types.SMALLINT);
		basicTypes.put(Byte.class, Types.TINYINT);
		basicTypes.put(Float.class, Types.FLOAT);
		basicTypes.put(Double.class, Types.DOUBLE);
		basicTypes.put(Boolean.class, Types.BOOLEAN);
		
		basicTypes.put(String.class, Types.VARCHAR);
		basicTypes.put(BigDecimal.class, Types.NUMERIC);
		basicTypes.put(BigInteger.class, Types.NUMERIC);
		basicTypes.put(Number.class, Types.NUMERIC);
		basicTypes.put(java.util.Date.class, Types.TIMESTAMP);
		basicTypes.put(java.util.Calendar.class, Types.TIMESTAMP);
		basicTypes.put(java.sql.Date.class, Types.DATE);
		basicTypes.put(java.sql.Time.class, Types.TIME);
		basicTypes.put(java.sql.Timestamp.class, Types.TIMESTAMP);
		basicTypes.put(byte[].class, Types.BINARY);
		
		BASIC_TYPES = Collections.unmodifiableMap(basicTypes);
	}
	
	public static int getType(Class<?> cls){
		Integer type = BASIC_TYPES.get(cls);
		if(type==null){
			type = new Integer(TYPE_UNKNOW);
		}
		return type.intValue();
	}
	
	public static int getType(Object value){
		if(value==null){
			return TYPE_UNKNOW;
		}
		return getType(value.getClass());
	}
	
	public static Class<?> getJavaType(int sqlType){
		Class<?> clz = SQL_TYPE_TO_JAVA_TYPE.get(sqlType);
		/*if(clz==null)
			return String.class;*/
		return clz;
	}
	
}
;