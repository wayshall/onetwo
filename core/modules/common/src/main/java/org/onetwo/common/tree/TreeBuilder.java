package org.onetwo.common.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.onetwo.common.utils.CUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@SuppressWarnings("unchecked")
public class TreeBuilder<TM extends TreeModel<TM>> {
	
	public static interface ParentNodeNotFoundAction<Node> {
		Node onParentNodeNotFound(Node currentNode);
	}
	
	public static interface RootNodeFunc<T extends TreeModel<T>> {
		
		public boolean isRootNode(T node);
	}
	
	public static class RootIdsFunc<T extends TreeModel<T>> implements RootNodeFunc<T> {
		final private List<Object> rootIds;
		
		public RootIdsFunc(Object... rootIds) {
			super();
			this.rootIds = Arrays.asList(rootIds);
		}

		@Override
		public boolean isRootNode(T node) {
			return rootIds.contains(node.getId()) || node.getParentId()==null;
		}
		
	}
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final Comparator<TM> comparator = new Comparator<TM>() {
		@Override
		public int compare(TM o1, TM o2) {
			Comparable<Object> s1 = (Comparable<Object>)o1.getSort();
			Comparable<Object> s2 = (Comparable<Object>)o2.getSort();
			if(s1==null && s2==null){
				return 0;
			}else if(s1==null){
				return -1;
			}else if(s2==null){
				return 1;
			}else{
				return s1.compareTo(s2);
			}
		}
	};
	

	public final ParentNodeNotFoundAction<TM> THROW_ERROR = node->{
		throw new RuntimeException("build tree error: can't not find the node[" + node.getId() + ", " + node.getName() + "]'s parent node[" + node.getParentId() + "]");
	};
	public final ParentNodeNotFoundAction<TM> IGNORE = node->{
		logger.error("build tree error: can't not find the node[" + node.getId() + ", " + node.getName() + "]'s parent node[" + node.getParentId() + "]");
		return null;
	};
	
	final private Map<Object, TM> nodeMap = new LinkedHashMap<Object, TM>();
	private List<TM> rootNodes = new ArrayList<TM>();
//	private Comparator<Object> comparator = null;
//	private List<?> rootIds;
	private RootNodeFunc<TM> rootNodeFunc;
	
	private Set<Object> notFoundParentIds = Sets.newHashSet();
	public final ParentNodeNotFoundAction<TM> STORE_NOT_FOUND_PARENTS = node->{
		logger.info("add node[{}]'s parent node id: {}", node, node.getParentId());
		notFoundParentIds.add(node.getParentId());
		return null;
	};

	public TreeBuilder(List<TM> datas) {
		Collections.sort(datas, comparator);
		for(TM tm : datas){
			this.nodeMap.put(tm.getId(), tm);
		}
	}
	public <T> TreeBuilder(List<T> datas, TreeModelCreator<TM, T> treeNodeCreator) {
		this(datas, treeNodeCreator, null);
	}

	public <T> TreeBuilder(List<T> datas, TreeModelCreator<TM, T> treeNodeCreator, Comparator<T> comparator) {
//		Assert.notEmpty(datas);

		final TreeModelCreator<TM, T> tnc = treeNodeCreator;

		Comparator<T> comp = comparator != null ? comparator : new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				TM t1 = tnc.createTreeModel(o1);
				TM t2 = tnc.createTreeModel(o2);
				Comparable<Object> s1 = (Comparable<Object>)t1.getSort();
				Comparable<Object> s2 = (Comparable<Object>)t2.getSort();
				return s1.compareTo(s2);
			}
		};

		if(datas!=null && !datas.isEmpty())
			Collections.sort(datas, comp);

		for (T data : datas) {
			if (data == null)
				continue;
			TM node = tnc.createTreeModel(data);
			nodeMap.put(node.getId(), node);
		}
	}

	/*public List<?> getRootIds() {
		return rootIds;
	}*/

	public TreeBuilder<TM> rootIds(Object...objects) {
//		this.rootIds = Arrays.asList(objects);
		Collection<?> ids = CUtils.stripNull(Lists.newArrayList(objects));
		if(ids.isEmpty())
			return this;
		this.rootNodeFunc = new RootIdsFunc<TM>(ids);
		return this;
	}
	
	public TreeBuilder<TM> rootNodeFunc(RootNodeFunc<TM> rootNodeFunc) {
		this.rootNodeFunc = rootNodeFunc;
		return this;
	}
	
	public List<TM> buidTree() {
		return this.buidTree(THROW_ERROR);
	}
	
	public List<TM> buidTree(ParentNodeNotFoundAction<TM> notFoundAction) {
		if (nodeMap.isEmpty())
			return Collections.EMPTY_LIST;

		List<TM> treeModels = (List<TM>) new ArrayList<>(nodeMap.values());

		
//		for (TM node : treeModels) {
		//这个循环比较特殊，会动态修改list，所以采用这种循环方式
		for (int i = 0; i < treeModels.size(); i++) {
			TM node = treeModels.get(i);
			if (this.rootNodeFunc!=null && this.rootNodeFunc.isRootNode(node)) {
				addRoot(node);
				continue;
			}else if(node.getParentId() == null){
				addRoot(node);
				continue;
			}
//			if (node.getParentId() == null || ((node.getParentId() instanceof Number) && ((Number) node.getParentId()).longValue() <= 0)) {
			/*if (node.getParentId() == null) {
				addRoot(node);
				continue;
			}*/
			if (isExistsRootNode(node))
				continue;
			TM p = nodeMap.get(node.getParentId());
			/*if (p == null) {
				if (ignoreNoParentLeafe)
					logger.error("build tree error: can't not find the node[" + node.getId() + ", " + node.getName() + "]'s parent node[" + node.getParentId() + "]");
				else
					throw new RuntimeException("build tree error: can't not find the node[" + node.getId() + ", " + node.getName() + "]'s parent node[" + node.getParentId() + "]");
			} else {
				p.addChild(node);
//				node.setParent(p);
			}*/
			if(p==null){
				p = notFoundAction.onParentNodeNotFound(node);
				if(p!=null){
					nodeMap.put(p.getId(), p);
					treeModels.add(p);
				}
			}
			if(p!=null){
				p.addChild(node);
			}
		}

//		Collections.sort(tree, this.comparator);
		return rootNodes;
	}
	
	protected void addRoot(TM node){
		rootNodes.add(node);
//		this.tree.put(node.getId()!=null?node.getId():node.getName(), node);
	}
	
	protected boolean isExistsRootNode(TreeModel<?> node){
		return rootNodes.contains(node);
	}
	
	public TreeModel<?> getBranch(Object rootId){
		for(TreeModel<?> node : this.rootNodes){
			if(rootId.equals(node.getId()))
				return node;
		}
		return null;
	}

	public Set<Object> getNotFoundParentIds() {
		return notFoundParentIds;
	}
	
	public List<TM> getRootNodes() {
		return rootNodes;
	}
	
	
	public Map<Object, TM> getNodeMap() {
		return nodeMap;
	}
	public static void main(String[] args) {
		DefaultTreeModel t1 = new DefaultTreeModel(1, "t1");

		DefaultTreeModel t2 = new DefaultTreeModel(2, "t2", 1);

		DefaultTreeModel t3 = new DefaultTreeModel(3, "t3", 1);

		DefaultTreeModel t4 = new DefaultTreeModel(4, "t4", 2);

		DefaultTreeModel t5 = new DefaultTreeModel(5, "t5", 4);

		List<DefaultTreeModel> list = Arrays.asList(t1, t2, t3, t4, t5);

		TreeBuilder<DefaultTreeModel> tb = new TreeBuilder<DefaultTreeModel>(list, new SimpleTreeModelCreator());
		tb.rootIds(1, 4);
		List<DefaultTreeModel> t = tb.buidTree(tb.IGNORE);
		System.out.println(t.get(0));
		System.out.println(tb.getBranch(4));
	}

}