package org.onetwo.common.db.parser;

import java.util.List;


public interface SqlCondition {

	public void setValue(Object value);
	public void setIgnore(boolean ignore);

	public boolean isAvailable();
	
	public boolean isIgnore();
	
	public boolean isMutiValue();

	public String toSqlString();

	public String getVarname();

	public Object getValue();
	public <T> T getValue(Class<T> type);
	public Object getActualValue();
	public List<?> getActualValueAsList();
//	public <T> List<T> getValueAsList();

}