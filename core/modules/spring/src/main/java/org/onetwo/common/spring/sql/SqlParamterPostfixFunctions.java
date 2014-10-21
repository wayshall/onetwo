package org.onetwo.common.spring.sql;

import java.util.Map;

import org.onetwo.common.db.ExtQueryUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;

public class SqlParamterPostfixFunctions {
	
	final private static SqlParamterPostfixFunctions instance = new SqlParamterPostfixFunctions();
	public static SqlParamterPostfixFunctions getInstance() {
		return instance;
	}
	
	private Map<String, SqlParamterPostfixFunction> funcMap = LangUtils.newHashMap();

	private SqlParamterPostfixFunctions(){
		register(new String[]{"like", "likeString"}, new SqlParamterPostfixFunction(){
			@Override
			public Object toSqlString(String paramName, Object value) {
				return ExtQueryUtils.getLikeString(value.toString());
			}
		});


		register("prelike", new SqlParamterPostfixFunction(){
			@Override
			public Object toSqlString(String paramName, Object value) {
				return StringUtils.appendStartWith(value.toString(), "%");
			}
		});

		register("postlike", new SqlParamterPostfixFunction(){
			@Override
			public Object toSqlString(String paramName, Object value) {
				return StringUtils.appendEndWith(value.toString(), "%");
			}
		});

		/*register("inlist", new SqlParamterPostfixFunction(){
			@Override
			public Object toSqlString(String paramName, Object value) {
				return ParserContextFunctionSet.getInstance().inValue(paramName, value);
			}
		});*/
		
	}

	private SqlParamterPostfixFunctions register(String postfix, SqlParamterPostfixFunction func){
		funcMap.put(postfix, func);
		return this;
	}
	private SqlParamterPostfixFunctions register(String[] postfixs, SqlParamterPostfixFunction func){
		for(String postfix : postfixs){
			register(postfix, func);
		}
		return this;
	}
	
	public SqlParamterPostfixFunction getFunc(String postfix){
		if(!funcMap.containsKey(postfix)){
			throw new BaseException("no postfix func fund: " + postfix);
		}
		return funcMap.get(postfix);
	}

}
