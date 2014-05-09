package org.onetwo.common.fish.utils;

import org.onetwo.common.log.DataChangedContext;

public interface ContextHolder {
	
//	public <T> T getContext(Class<T> clazz);
	
//	public <T> void setContext(T context);

	public DataChangedContext getDataChangedContext();
	public void setDataChangedContext(DataChangedContext context);

	public <T> void setContextAttribute(String attrName, T attr);
	public <T> T getContextAttribute(String attrName);
}
