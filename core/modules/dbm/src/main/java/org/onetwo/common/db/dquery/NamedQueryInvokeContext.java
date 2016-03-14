package org.onetwo.common.db.dquery;

import java.util.Map;

public interface NamedQueryInvokeContext {

	public String getQueryName();
	
	public Map<Object, Object> getParsedParams();
	
}