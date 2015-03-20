package org.onetwo.common.db;

import java.util.List;
import java.util.Map;

public interface ParsedSqlContext {

	public Map<String, Object> asMap();

	public List<Object> asList();

	public boolean isListValue();

	public boolean isMapValue();

	/*********
	 * List Or Map
	 * @return
	 */
	public <T> T getValues();

	public String getParsedSql();

	public QueryConfigData getQueryConfig();

}