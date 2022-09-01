package org.onetwo.common.tree;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

import org.onetwo.common.utils.LangUtils;


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
	
	/****
	 * 
	 * @author weishao zeng
	 * @param <TM>
	 * @param treeNode
	 * @param action
	 * @param recursion 是否递归
	 */
	public static <TM extends TreeModel<TM>> void doIfChildrenIsEmpty(TM treeNode, Consumer<TM> action, boolean recursion) {
		List<TM> children = treeNode.getChildren();
		if (LangUtils.isEmpty(children)) {
			action.accept(treeNode);
		} else if (recursion) {
			for (TM node : children) {
				doIfChildrenIsEmpty(node, action, recursion);
			}
		}
	}
	
	private TreeUtils(){
	}
}
