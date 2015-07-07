package org.onetwo.common.db.generator.mapping;

import java.util.Map;

import com.google.common.collect.Maps;

public class ColumnMapping {
	private int sqlType;
	private Class<?> javaType;

	private Map<String, Object> attrs = Maps.newHashMap();

	public ColumnMapping(int sqlType) {
		super();
		this.sqlType = sqlType;
	}

	public int getSqlType() {
		return sqlType;
	}

	public Class<?> getJavaType() {
		return javaType;
	}

	public ColumnMapping javaType(Class<?> javaType) {
		this.javaType = javaType;
		return this;
	}

	public Map<String, Object> getAttrs() {
		return attrs;
	}

	public ColumnMapping attr(String name, Object value) {
		this.attrs.put(name, value);
		return this;
	}

	@Override
	public String toString() {
		return "ColumnMapping [sqlType=" + sqlType + ", javaType=" + javaType
				+ ", attrs=" + attrs + "]";
	}
	
	
}
