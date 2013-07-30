package org.onetwo.common.utils;

public interface TreeModel<T>{

	public void addChild(T node);
	public Object getParentId();
	public Object getId();
	public String getName();
	
	public Comparable<?> getSort();

}