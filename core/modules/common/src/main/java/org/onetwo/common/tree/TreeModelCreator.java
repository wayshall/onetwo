package org.onetwo.common.tree;


public interface TreeModelCreator<TM extends TreeModel<TM>, T>{
	
	public TM createTreeModel(T obj);

}
