package org.onetwo.plugins.permission;

import org.junit.Test;
import org.onetwo.plugins.permission.JresourceManagerImpl.RescourceTreeModel;

public class RescourceTreeModelTest {
	
	@Test
	public void test(){
		JResourceInfo parent = new JResourceInfo("module", "会员模块");
		JResourceInfo info = new JResourceInfo("member", "会员管理");
		info.setParent(parent);
		JResourceInfo info1 = new JResourceInfo("member.new", "创建会员");
		info1.setParent(info);
		JResourceInfo info2 = new JResourceInfo("member.edit", "编辑会员");
		info2.setParent(info);

		RescourceTreeModel pnode = new RescourceTreeModel(parent);
		RescourceTreeModel node = new RescourceTreeModel(info);
		RescourceTreeModel node2 = new RescourceTreeModel(info1);
		RescourceTreeModel node3 = new RescourceTreeModel(info2);
		node.addChild(node2);
		node.addChild(node3);
		pnode.addChild(node);
		
		System.out.println("node:\n" + pnode);
	}

}
