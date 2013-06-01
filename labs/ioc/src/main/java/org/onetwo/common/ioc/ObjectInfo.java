package org.onetwo.common.ioc;

import java.lang.reflect.Type;

public interface ObjectInfo {

	public boolean isCollection();

	public boolean isMap();

	public boolean isClazzValue();

	public boolean isMatchType(Type type);

	public String getName();

	public Object getBean();
	
	public Object getActualBean();

	public boolean isProxy();

	public void destroy();

}