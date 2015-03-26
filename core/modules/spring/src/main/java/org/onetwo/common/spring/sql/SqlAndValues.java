package org.onetwo.common.spring.sql;

import java.util.List;
import java.util.Map;

import org.onetwo.common.db.ParsedSqlContext;
import org.onetwo.common.db.QueryConfigData;

public class SqlAndValues implements ParsedSqlContext {
	private String parsedSql;
	final private Object values;
	final private boolean namedValue;
	final private QueryConfigData queryConfig;
	
	public SqlAndValues(boolean namedValue, String parsedSql, Object values) {
		this(namedValue, parsedSql, values, null);
	}
	
	public SqlAndValues(boolean namedValue, String parsedSql, Object values, QueryConfigData queryConfig) {
		super();
		this.namedValue = namedValue;
		this.parsedSql = parsedSql;
		this.values = values;
		this.queryConfig = queryConfig;
	}
	

	@Override
	public Map<String, Object> asMap() {
		return getValues();
	}

	@Override
	public List<Object> asList() {
		return getValues();
	}
	
	@Override
	public boolean isListValue(){
		return !this.namedValue;
	}
	
	@Override
	public boolean isMapValue(){
		return this.namedValue;
	}
	
	/*********
	 * List Or Map
	 * @return
	 */
	@Override
	public <T> T getValues(){
		return (T) values;
	}

	@Override
	public String getParsedSql() {
		return parsedSql;
	}

	public void setParsedSql(String parsedSql) {
		this.parsedSql = parsedSql;
	}

	@Override
	public QueryConfigData getQueryConfig() {
		return queryConfig==null?ParsedSqlUtils.EMPTY_CONFIG:queryConfig;
	}
	
}
