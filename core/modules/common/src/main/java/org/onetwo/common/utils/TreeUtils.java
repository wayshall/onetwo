package org.onetwo.common.utils;

import java.util.List;


final public class TreeUtils {

	public static <TM extends TreeModel<TM>, T> TreeBuilder<TM> newBuilder(List<T> datas, TreeModelCreator<TM, T> treeNodeCreator){
		return new TreeBuilder<TM>(datas, treeNodeCreator);
	}
	
	public static <T extends TreeModel<T>> void buildString(StringBuilder str, TreeModel<T> node, String sp){
		str.append(node.getId()).append("\n");
		if(node.getChildren()==null)
			return ;
		
		for(T cnode : node.getChildren()){
			str.append(sp);
			sp += sp;
			buildString(str, cnode, sp);
		}
	}
	
	private TreeUtils(){
	}
}
