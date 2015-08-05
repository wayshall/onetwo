package org.onetwo.common.spring.sql;

import java.util.Date;
import java.util.Map;

import org.onetwo.common.db.ExtQueryUtils;
import org.onetwo.common.db.filequery.SqlParamterPostfixFunction;
import org.onetwo.common.db.filequery.SqlParamterPostfixFunctionRegistry;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.JodatimeUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.convert.Types;

public class SqlParamterPostfixFunctions implements SqlParamterPostfixFunctionRegistry {
	
	/*final private static SqlParamterPostfixFunctionRegistry instance = new SqlParamterPostfixFunctions();
	public static SqlParamterPostfixFunctionRegistry getInstance() {
		return instance;
	}*/
	
	private Map<String, SqlParamterPostfixFunction> funcMap = LangUtils.newHashMap();

	public SqlParamterPostfixFunctions(){
		register(new String[]{"like", "likeString"}, new SqlParamterPostfixFunction(){
			@Override
			public Object toSqlString(String paramName, Object value) {
				return ExtQueryUtils.getLikeString(value.toString());
			}
		});


		register(new String[]{"prelike", "prelikeString"}, new SqlParamterPostfixFunction(){
			@Override
			public Object toSqlString(String paramName, Object value) {
				return StringUtils.appendStartWith(value.toString(), "%");
			}
		});

		register(new String[]{"postlike", "postlikeString"}, new SqlParamterPostfixFunction(){
			@Override
			public Object toSqlString(String paramName, Object value) {
				return StringUtils.appendEndWith(value.toString(), "%");
			}
		});

		register(new String[]{"atStartOfDate"}, new SqlParamterPostfixFunction(){
			@Override
			public Object toSqlString(String paramName, Object value) {
				if(Date.class.isInstance(value)){
					throw new BaseException(paramName+" is not a date, can not invoke startOfDate");
				}
				Date date = Types.convertValue(value, Date.class);
				return JodatimeUtils.atStartOfDate(date);
			}
		});

		register(new String[]{"atEndOfDate"}, new SqlParamterPostfixFunction(){
			@Override
			public Object toSqlString(String paramName, Object value) {
				if(Date.class.isInstance(value)){
					throw new BaseException(paramName+" is not a date, can not invoke endOfDate");
				}
				Date date = Types.convertValue(value, Date.class);
				return JodatimeUtils.atEndOfDate(date);
			}
		});

		/*register("inlist", new SqlParamterPostfixFunction(){
			@Override
			public Object toSqlString(String paramName, Object value) {
				return ParserContextFunctionSet.getInstance().inValue(paramName, value);
			}
		});*/
		
	}

	private SqlParamterPostfixFunctionRegistry register(String postfix, SqlParamterPostfixFunction func){
		funcMap.put(postfix, func);
		return this;
	}
	private SqlParamterPostfixFunctionRegistry register(String[] postfixs, SqlParamterPostfixFunction func){
		for(String postfix : postfixs){
			register(postfix, func);
		}
		return this;
	}
	
	/* (non-Javadoc)
	 * @see org.onetwo.common.spring.sql.SqlParamterPostfixFunctionRegistry#getFunc(java.lang.String)
	 */
	@Override
	public SqlParamterPostfixFunction getFunc(String postfix){
		if(!funcMap.containsKey(postfix)){
			throw new BaseException("no postfix func fund: " + postfix);
		}
		return funcMap.get(postfix);
	}

}
