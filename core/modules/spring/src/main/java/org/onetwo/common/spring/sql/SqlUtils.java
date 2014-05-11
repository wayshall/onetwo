package org.onetwo.common.spring.sql;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.onetwo.common.utils.ReflectUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterUtils;
import org.springframework.jdbc.core.namedparam.ParsedSql;

final public class SqlUtils {

	public static class ParsedSqlWrapper {
//		final private ParsedSql parsedSql;
		final private Collection<String> parameterNames;
		public ParsedSqlWrapper(ParsedSql parsedSql) {
			super();
			List<String> parameterNames = (List<String>) ReflectUtils.getFieldValue(parsedSql, "parameterNames");;
			this.parameterNames = new HashSet<String>(parameterNames);
		}
		public Collection<String> getParameterNames() {
			return parameterNames;
		}
	}
	
	public static ParsedSqlWrapper parseSql(String sql){
		ParsedSql parsedSql = NamedParameterUtils.parseSqlStatement(sql);
		return new ParsedSqlWrapper(parsedSql);
	}
	private SqlUtils(){
	}

}
