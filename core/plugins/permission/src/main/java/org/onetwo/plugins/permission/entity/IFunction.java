package org.onetwo.plugins.permission.entity;


public interface IFunction<T> extends IPermission{

	T getMenu();

	void setMenu(T menu);

}