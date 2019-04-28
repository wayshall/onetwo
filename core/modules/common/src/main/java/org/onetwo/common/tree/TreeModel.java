package org.onetwo.common.tree;

import java.util.List;

public interface TreeModel<T>{

	public void addChild(T node);
	public List<T> getChildren();
	public Object getParentId();
	public Object getId();
	public String getName();
	
	default public Comparable<?> getSort() {
		return (Comparable<?>)getId();
	}

}