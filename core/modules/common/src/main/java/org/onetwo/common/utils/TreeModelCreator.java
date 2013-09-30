package org.onetwo.common.utils;


public interface TreeModelCreator<TM extends TreeModel<TM>, T>{
	
	public TM createTreeModel(T obj);

}
