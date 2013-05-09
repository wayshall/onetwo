package org.onetwo.common.ioc.inject;

@SuppressWarnings("unchecked")
public interface ComponentMetaTransfor {

	public String getBeanName(String name);

	public Class[] getInterceptorClasses();

	public Class[] getBusinessInterfaces();
	
	public Object getBean();
	
	public boolean needProxy();

}