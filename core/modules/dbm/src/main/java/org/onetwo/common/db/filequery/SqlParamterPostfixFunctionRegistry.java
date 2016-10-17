package org.onetwo.common.db.filequery;

public interface SqlParamterPostfixFunctionRegistry {

	String getFuncPostfixMark();
	SqlParamterPostfixFunction getFunc(String postfix);

}