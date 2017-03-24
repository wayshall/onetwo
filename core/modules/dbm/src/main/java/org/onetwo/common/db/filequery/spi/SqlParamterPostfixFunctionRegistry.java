package org.onetwo.common.db.filequery.spi;

import org.onetwo.common.db.filequery.SqlParamterPostfixFunction;

public interface SqlParamterPostfixFunctionRegistry {

	String getFuncPostfixMark();
	SqlParamterPostfixFunction getFunc(String postfix);

}