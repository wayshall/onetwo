package org.onetwo.common.db.filequery;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.onetwo.common.db.QueryConfigData;
import org.onetwo.common.db.filequery.spi.SqlParamterPostfixFunctionRegistry;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.jdbc.core.namedparam.NamedParameterUtils;
import org.springframework.jdbc.core.namedparam.ParsedSql;

final public class ParsedSqlUtils {
	public static final String SQL_POST_FIX_FUNC_MARK = "?";

	public static final QueryConfigData EMPTY_CONFIG = new QueryConfigData(true){
		
		{
			setVariables(ParserContextFunctionSet.getInstance());
		}

		public void setLikeQueryFields(List<String> likeQueryFields) {
			throw new UnsupportedOperationException();
		}
	};

	@SuppressWarnings("unchecked")
	public static class ParsedSqlWrapper {
//		final private ParsedSql parsedSql;
		final private Collection<SqlParamterMeta> parameterNames;
		final private SqlParamterPostfixFunctionRegistry sqlParamterPostfixFunctions;
		public ParsedSqlWrapper(ParsedSql parsedSql, SqlParamterPostfixFunctionRegistry sqlParamterPostfixFunctions) {
			super();
			this.sqlParamterPostfixFunctions = sqlParamterPostfixFunctions;
			List<String> parameterNames = (List<String>) ReflectUtils.getFieldValue(parsedSql, "parameterNames");;
			this.parameterNames = new HashSet<SqlParamterMeta>();
			for(String pname : parameterNames){
				this.parameterNames.add(new SqlParamterMeta(pname));
			}
		}
		public Collection<SqlParamterMeta> getParameters() {
			return parameterNames;
		}
		
		public boolean hasNamedParameter(){
			return LangUtils.isNotEmpty(parameterNames);
		}
		
		public class SqlParamterMeta {
			final private String name;
			private String property;
			private String function;
			
			public SqlParamterMeta(String pname) {
				super();
				this.name = pname;
				int mark = pname.indexOf(sqlParamterPostfixFunctions.getFuncPostfixMark());
				if(mark==-1){
					property = name;
				}else{
					property = pname.substring(0, mark);
					function = pname.substring(mark+1);
				}
			}
			public String getName() {
				return name;
			}
			public String getFunction() {
				return function;
			}
			public boolean hasFunction(){
				return StringUtils.isNotBlank(function);
			}
			public Object getParamterValue(BeanWrapper paramBean){
				Object value = paramBean.getPropertyValue(property);
				if(hasFunction()){
//					value = ReflectUtils.invokeMethod(function, SqlParamterPostfixFunctions.getInstance(), value);
//					value = SqlParamterPostfixFunctions.getInstance().getFunc(function).toSqlString(property, value);
					value = sqlParamterPostfixFunctions.getFunc(function).toSqlString(property, value);
				}
				return value;
			}
			public String getProperty() {
				return property;
			}
			@Override
			public String toString() {
				return "SqlParamterMeta [name=" + name + ", property="
						+ property + ", function=" + function + "]";
			}
			
		}
	}
	
	public static ParsedSqlWrapper parseSql(String sql, SqlParamterPostfixFunctionRegistry sqlParamterPostfixFunctions){
		ParsedSql parsedSql = NamedParameterUtils.parseSqlStatement(sql);
		return new ParsedSqlWrapper(parsedSql, sqlParamterPostfixFunctions);
	}
	private ParsedSqlUtils(){
	}

}
