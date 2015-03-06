package org.onetwo.common.db;

import java.util.List;
import java.util.Map;

public class SqlAndValues {
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
	

	public Map<String, Object> asMap() {
		return getValues();
	}

	public List<Object> asList() {
		return getValues();
	}
	
	public boolean isListValue(){
		return !this.namedValue;
	}
	
	public boolean isMapValue(){
		return this.namedValue;
	}
	
	/*********
	 * List Or Map
	 * @return
	 */
	public <T> T getValues(){
		return (T) values;
	}

	public String getParsedSql() {
		return parsedSql;
	}

	public void setParsedSql(String parsedSql) {
		this.parsedSql = parsedSql;
	}

	public QueryConfigData getQueryConfig() {
		return queryConfig==null?QueryConfigData.EMPTY_CONFIG:queryConfig;
	}
	
}
