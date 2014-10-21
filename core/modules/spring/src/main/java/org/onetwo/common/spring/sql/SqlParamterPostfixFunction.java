package org.onetwo.common.spring.sql;

public interface SqlParamterPostfixFunction {
	
	public Object toSqlString(String paramName, Object value);

}
