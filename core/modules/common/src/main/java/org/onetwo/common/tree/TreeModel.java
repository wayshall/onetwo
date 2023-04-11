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
	
	/****
	 * 是否有孩子节点
	 * @return
	 */
//	default public boolean isHasChildren() {
//		boolean hasChildren = LangUtils.isNotEmpty(getChildren());
//		return hasChildren;
//	}

}