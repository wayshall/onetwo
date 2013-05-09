package org.onetwo.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.onetwo.common.exception.ServiceException;

@SuppressWarnings("unchecked")
public class TreeBuilder<T> {

	Logger logger = Logger.getLogger(this.getClass());

	private Map leafages = new LinkedHashMap();
	private List<TreeModel> tree = new ArrayList<TreeModel>();
	private Comparator comparator = null;
	private List rootIds;

	public TreeBuilder(List<T> datas) {
		this(datas, null, null);
	}

	public TreeBuilder(List<T> datas, TreeModelCreator treeNodeCreator) {
		this(datas, treeNodeCreator, null);
	}

	public List getRootIds() {
		return rootIds;
	}

	public void setRootIds(Object...objects) {
		this.rootIds = Arrays.asList(objects);
	}

	public TreeBuilder(List<T> datas, TreeModelCreator<T> treeNodeCreator, Comparator comparator) {
		if (datas == null || datas.isEmpty())
			return;

		final TreeModelCreator tnc;
		if (treeNodeCreator == null)
			tnc = new SimpleTreeModelCreator();
		else
			tnc = treeNodeCreator;

		this.comparator = comparator != null ? comparator : new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				TreeModel t1 = tnc.createTreeModel(o1);
				TreeModel t2 = tnc.createTreeModel(o2);
				return t1.compareTo(t2);
			}
		};
		Collections.sort(datas, this.comparator);

		for (T data : datas) {
			if (data == null)
				continue;
			TreeModel node = tnc.createTreeModel(data);
			leafages.put(node.getId(), node);
		}
	}

	public List<TreeModel> buidTree() {
		return this.buidTree(false);
	}

	public List<TreeModel> buidTree(boolean ignoreNoParentLeafe) {
		if (leafages.isEmpty())
			return Collections.EMPTY_LIST;

		Collection<TreeModel> treeModels = (Collection<TreeModel>) leafages.values();

		for (TreeModel node : treeModels) {
			if (getRootIds() != null) {
				if(getRootIds().contains(node.getId())){
					addRoot(node);
					continue;
				}
			}
			if (node.getParentId() == null || ((node.getParentId() instanceof Number) && ((Number) node.getParentId()).longValue() <= 0)) {
				addRoot(node);
				continue;
			}
			if (isRoot(node))
				continue;
			TreeModel p = (TreeModel) leafages.get(node.getParentId());
			if (p == null) {
				if (ignoreNoParentLeafe)
					logger.error("build tree error: can't not find the node[" + node.getId() + ", " + node.getName() + "]'s parent node[" + node.getParentId() + "]");
				else
					throw new ServiceException("build tree error: can't not find the node[" + node.getId() + ", " + node.getName() + "]'s parent node[" + node.getParentId() + "]");
			} else {
				p.addChild(node);
//				node.setParent(p);
			}
		}

		return tree;
	}
	
	protected void addRoot(TreeModel node){
		tree.add(node);
//		this.tree.put(node.getId()!=null?node.getId():node.getName(), node);
	}
	
	protected boolean isRoot(TreeModel node){
		return tree.contains(node);
	}
	
	public TreeModel getBranch(Object rootId){
		for(TreeModel node : this.tree){
			if(rootId.equals(node.getId()))
				return node;
		}
		return null;
	}

	public static void main(String[] args) {
		TreeModel t1 = new TreeModel(1, "t1");

		TreeModel t2 = new TreeModel(2, "t2", 1);

		TreeModel t3 = new TreeModel(3, "t3", 1);

		TreeModel t4 = new TreeModel(4, "t4", 2);

		TreeModel t5 = new TreeModel(5, "t5", 4);

		List list = Arrays.asList(t1, t2, t3, t4, t5);

		TreeBuilder tb = new TreeBuilder(list);
		tb.setRootIds(1, 4);
		List<TreeModel> t = tb.buidTree(true);
		System.out.println(t.get(0));
		System.out.println(tb.getBranch(4));
	}

}