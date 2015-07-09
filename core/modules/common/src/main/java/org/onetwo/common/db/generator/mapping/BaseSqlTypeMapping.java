package org.onetwo.common.db.generator.mapping;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.onetwo.common.db.generator.DBUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.Assert;


public class BaseSqlTypeMapping implements MetaMapping {

	public static final int TYPE_UNKNOW = DBUtils.TYPE_UNKNOW;
	
//	private final Map<Class<?>, Integer> javaToSqlTypeMappings = new HashMap<>();
	
	private final Map<Integer, ColumnMapping> sqlToJavaTypeMappings = new HashMap<>();
	
	final public BaseSqlTypeMapping mapping(int sqlType, Class<?> javaType){
		this.sqlToJavaTypeMappings.put(sqlType, new ColumnMapping(sqlType).javaType(javaType));
		return this;
	}
	
	final public BaseSqlTypeMapping mapping(Map<Integer, Class<?>> sqlToJavaTypeMappings){
		sqlToJavaTypeMappings.forEach((k, v)->{
			mapping(k, v);
		});
		return this;
	}

	/*@Override
	public int getSqlType(Class<?> cls){
		Integer type = javaToSqlTypeMappings.get(cls);
		if(type==null){
			type = new Integer(TYPE_UNKNOW);
		}
		return type.intValue();
	}*/

	/*@Override
	public int getSqlType(Object value){
		if(value==null){
			return TYPE_UNKNOW;
		}
		return getSqlType(value.getClass());
	}*/
	
	@Override
	public ColumnMapping getColumnMapping(int sqlType){
		ColumnMapping mapping = sqlToJavaTypeMappings.get(sqlType);
		return mapping;
	}
	
	@Override
	public Collection<ColumnMapping> getColumnMappings(){
		return sqlToJavaTypeMappings.values();
	}
	public ColumnMapping getRequiredColumnMapping(int sqlType){
		ColumnMapping mapping = sqlToJavaTypeMappings.get(sqlType);
		if(mapping==null){
			throw new BaseException("no mapping for sql type: " + sqlType);
		}
		return mapping;
	}
	
	@Override
	public Class<?> getJavaType(int sqlType){
		ColumnMapping mapping = getColumnMapping(sqlType);
		Assert.notNull(mapping);
		Class<?> clz = mapping.getJavaType();
		return clz;
	}
	
}
;