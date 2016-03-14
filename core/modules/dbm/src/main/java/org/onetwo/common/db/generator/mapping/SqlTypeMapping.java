package org.onetwo.common.db.generator.mapping;

public interface SqlTypeMapping {

	int TYPE_UNKNOW = Integer.MIN_VALUE;
	/*int getSqlType(Class<?> cls);
	
	int getSqlType(Object value);*/
	
	Class<?> getJavaType(int sqlType);
}
