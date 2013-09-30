package org.onetwo.common.db;

import java.util.Map;

public interface SelectExtQuery extends ExtQuery {

	public boolean needSetRange();

	public Integer getFirstResult();

	public Integer getMaxResults();
	
	public void setMaxResults(Integer maxResults);
	
//	public boolean isIgnoreQuery();
	public Map<String, Object> getQueryConfig();
	public String getCountSql();
	

	public boolean isSubQuery();

	public void setSubQuery(boolean subQuery);
	
}
