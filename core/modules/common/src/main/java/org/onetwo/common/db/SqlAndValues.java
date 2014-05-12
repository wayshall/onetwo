package org.onetwo.common.db;

import java.util.List;
import java.util.Map;

public class SqlAndValues {
	private String parsedSql;
	private Object values;
	private boolean namedValue;
	
	public SqlAndValues(boolean namedValue, String parsedSql, Object values) {
		super();
		this.namedValue = namedValue;
		this.parsedSql = parsedSql;
		this.values = values;
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
	
}
