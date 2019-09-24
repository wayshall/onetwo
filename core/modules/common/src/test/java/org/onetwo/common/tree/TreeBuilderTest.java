package org.onetwo.common.tree;
/**
 * @author weishao zeng
 * <br/>
 */

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import com.google.common.collect.Lists;

public class TreeBuilderTest {
	
	@Test
	public void testBuildTree() {
		TreeNodeDataTest data1 = new TreeNodeDataTest(1L, null, "node1");
		TreeNodeDataTest data2 = new TreeNodeDataTest(2L, 1L, "node2");
		TreeNodeDataTest data3 = new TreeNodeDataTest(3L, 2L, "node3");
		TreeNodeDataTest data4 = new TreeNodeDataTest(4L, 1L, "node4");
		TreeNodeDataTest data5 = new TreeNodeDataTest(5L, 4L, "node5");
		List<TreeNodeDataTest> datas = Lists.newArrayList(data1, data2, data3, data4, data5);
		
		List<DefaultTreeModel> models = datas.stream().map(d->{
			return new DefaultTreeModel(d.getId(), d.getName(), d.getParentId());
		}).collect(Collectors.toList());
		TreeBuilder<DefaultTreeModel> treeBuilder = new TreeBuilder<>(models);
		List<DefaultTreeModel> roots = treeBuilder.buidTree();
		System.out.println("roots: \n" + roots.get(0));
		
		StringBuilder str = new StringBuilder();
		TreeUtils.buildString(str, roots.get(0), "--");
		System.out.println("roots: \n" + str);
	}
	
	public static class TreeNodeDataTest {
		private Long id;
		private Long parentId;
		private String name;
		
		public TreeNodeDataTest(Long id, Long parentId, String name) {
			super();
			this.id = id;
			this.parentId = parentId;
			this.name = name;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public Long getParentId() {
			return parentId;
		}
		public void setParentId(Long parentId) {
			this.parentId = parentId;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}

}

