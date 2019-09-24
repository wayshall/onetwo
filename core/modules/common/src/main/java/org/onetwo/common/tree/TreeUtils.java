package org.onetwo.common.tree;

import java.util.Collection;
import java.util.Comparator;


final public class TreeUtils {
	
	

	public static <TM extends TreeModel<TM>, T> TreeBuilder<TM> newBuilder(Collection<T> datas, TreeModelCreator<TM, T> treeNodeCreator){
		return new TreeBuilder<TM>(datas, treeNodeCreator);
	}

	public static <TM extends TreeModel<TM>, T> TreeBuilder<TM> newBuilder(Collection<T> datas, TreeModelCreator<TM, T> treeNodeCreator, Comparator<TM> comparator){
		return new TreeBuilder<TM>(datas, treeNodeCreator, comparator);
	}
	
	public static <T extends TreeModel<T>> void buildString(StringBuilder str, TreeModel<T> node, final String sp){
		str.append(node.getName()).append("(").append(node.getId()).append(")\n");
		if(node.getChildren()==null) {
			return ;
		}
		
		String spTemp = sp;
		for(T cnode : node.getChildren()){
			str.append(spTemp);
			spTemp += sp;
			buildString(str, cnode, spTemp);
			spTemp = sp;
		}
	}
	
	private TreeUtils(){
	}
}
