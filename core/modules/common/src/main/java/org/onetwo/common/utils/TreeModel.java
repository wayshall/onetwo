package org.onetwo.common.utils;

import java.util.List;

public interface TreeModel<T>{

	public void addChild(T node);
	public List<T> getChildren();
	public Object getParentId();
	public Object getId();
	public String getName();
	
	public Comparable<?> getSort();

}