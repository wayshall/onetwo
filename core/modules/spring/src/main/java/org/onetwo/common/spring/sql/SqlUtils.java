package org.onetwo.common.spring.sql;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.jdbc.core.namedparam.NamedParameterUtils;
import org.springframework.jdbc.core.namedparam.ParsedSql;

final public class SqlUtils {

	public static class ParsedSqlWrapper {
//		final private ParsedSql parsedSql;
		final private Collection<SqlParamterMeta> parameterNames;
		public ParsedSqlWrapper(ParsedSql parsedSql) {
			super();
			List<String> parameterNames = (List<String>) ReflectUtils.getFieldValue(parsedSql, "parameterNames");;
			this.parameterNames = new HashSet<SqlParamterMeta>();
			for(String pname : parameterNames){
				this.parameterNames.add(new SqlParamterMeta(pname));
			}
		}
		public Collection<SqlParamterMeta> getParameters() {
			return parameterNames;
		}
		
		public class SqlParamterMeta {
			final private String name;
			private String property;
			private String function;
			
			public SqlParamterMeta(String pname) {
				super();
				this.name = pname;
				int mark = pname.indexOf('?');
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
					value = SqlParamterPostfixFunctions.getInstance().getFunc(function).toSqlString(property, value);
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
	
	public static ParsedSqlWrapper parseSql(String sql){
		ParsedSql parsedSql = NamedParameterUtils.parseSqlStatement(sql);
		return new ParsedSqlWrapper(parsedSql);
	}
	private SqlUtils(){
	}

}
