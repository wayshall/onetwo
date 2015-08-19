package org.onetwo.common.db.filequery;

public interface SqlParamterPostfixFunction {
	
	public Object toSqlString(String paramName, Object value);

}
