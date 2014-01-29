package org.onetwo.plugins.permission.entity;

import java.util.List;


public interface IMenu<T extends IMenu<T, F>, F extends IFunction<T>> extends IPermission {

	String getUrl();
	void setUrl(String url);

	String getMethod();
	public void setMethod(String method);

	T getParent();
	
	public void setParent(T parent);

	void addFunction(IFunction<T> func);

	void addChild(IMenu<T, F> menu);
	public List<T> getChildren();
	public List<F> getFunctions();

}