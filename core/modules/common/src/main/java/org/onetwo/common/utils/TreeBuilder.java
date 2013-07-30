package org.onetwo.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.log.MyLoggerFactory;
import org.slf4j.Logger;

@SuppressWarnings("unchecked")
public class TreeBuilder<TM extends TreeModel<TM>, T> {
	
	public static <TM1 extends TreeModel<TM1>, T1> TreeBuilder<TM1, T1> newBuilder(List<T1> datas, TreeModelCreator<TM1, T1> treeNodeCreator){
		return new TreeBuilder<TM1, T1>(datas, treeNodeCreator);
	}

	private final Logger logger = MyLoggerFactory.getLogger(this.getClass());

	private Map<Object, TM> leafages = new LinkedHashMap<Object, TM>();
	private List<TM> tree = new ArrayList<TM>();
	private Comparator<T> comparator = null;
	private List<?> rootIds;

	public TreeBuilder(List<T> datas, TreeModelCreator<TM, T> treeNodeCreator) {
		this(datas, treeNodeCreator, null);
	}

	public List<?> getRootIds() {
		return rootIds;
	}

	public void setRootIds(Object...objects) {
		this.rootIds = Arrays.asList(objects);
	}

	public TreeBuilder(List<T> datas, TreeModelCreator<TM, T> treeNodeCreator, Comparator<T> comparator) {
		if (datas == null || datas.isEmpty())
			return;

		final TreeModelCreator<TM, T> tnc = treeNodeCreator;

		this.comparator = comparator != null ? comparator : new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				TM t1 = tnc.createTreeModel(o1);
				TM t2 = tnc.createTreeModel(o2);
				Comparable<Object> s1 = (Comparable<Object>)t1.getSort();
				Comparable<Object> s2 = (Comparable<Object>)t2.getSort();
				return s1.compareTo(s2);
			}
		};
		Collections.sort(datas, this.comparator);

		for (T data : datas) {
			if (data == null)
				continue;
			TM node = tnc.createTreeModel(data);
			leafages.put(node.getId(), node);
		}
	}

	public List<TM> buidTree() {
		return this.buidTree(false);
	}

	public List<TM> buidTree(boolean ignoreNoParentLeafe) {
		if (leafages.isEmpty())
			return Collections.EMPTY_LIST;

		Collection<TM> treeModels = (Collection<TM>) leafages.values();

		for (TM node : treeModels) {
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
			TreeModel<TreeModel<?>> p = (TreeModel<TreeModel<?>>) leafages.get(node.getParentId());
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
	
	protected void addRoot(TM node){
		tree.add(node);
//		this.tree.put(node.getId()!=null?node.getId():node.getName(), node);
	}
	
	protected boolean isRoot(TreeModel<?> node){
		return tree.contains(node);
	}
	
	public TreeModel<?> getBranch(Object rootId){
		for(TreeModel<?> node : this.tree){
			if(rootId.equals(node.getId()))
				return node;
		}
		return null;
	}

	public static void main(String[] args) {
		DefaultTreeModel t1 = new DefaultTreeModel(1, "t1");

		DefaultTreeModel t2 = new DefaultTreeModel(2, "t2", 1);

		DefaultTreeModel t3 = new DefaultTreeModel(3, "t3", 1);

		DefaultTreeModel t4 = new DefaultTreeModel(4, "t4", 2);

		DefaultTreeModel t5 = new DefaultTreeModel(5, "t5", 4);

		List<DefaultTreeModel> list = Arrays.asList(t1, t2, t3, t4, t5);

		TreeBuilder<DefaultTreeModel, DefaultTreeModel> tb = new TreeBuilder<DefaultTreeModel, DefaultTreeModel>(list, new SimpleTreeModelCreator());
		tb.setRootIds(1, 4);
		List<DefaultTreeModel> t = tb.buidTree(true);
		System.out.println(t.get(0));
		System.out.println(tb.getBranch(4));
	}

}