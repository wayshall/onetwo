package org.onetwo.plugins.zkclient;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.junit.BeforeClass;
import org.junit.Test;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.plugins.zkclient.curator.CuratorClient;

public class CuratorTest {
	
	static protected CuratorClient client;
	
	@BeforeClass
	static public void setup() throws Exception{
		String connectString = "127.0.0.1:2181";
		client = new CuratorClient("/jfish", connectString);
		client.afterPropertiesSet();
	}
	
	@Test
	public void testNodeCache() throws Exception{
		//path /test 可以不存在
		//无法坚持子节点的修改
		NodeCache nodeCache = new NodeCache(client.getCurator(), "/test");
		nodeCache.start();
		nodeCache.getListenable().addListener(new NodeCacheListener() {
			
			@Override
			public void nodeChanged() throws Exception {
				System.out.println("NodeCacheListener: node data update, new date: " + new String(nodeCache.getCurrentData().getData()));
			}
		});

		LangUtils.CONSOLE.exitIf("exit");
	}
	
	@Test
	public void testPathChildrenCache() throws Exception{

		NodeCache nodeCache = new NodeCache(client.getCurator(), "/pathChildren");
		nodeCache.start();
		nodeCache.getListenable().addListener(new NodeCacheListener() {
			
			@Override
			public void nodeChanged() throws Exception {
				System.out.println("NodeCacheListener: node data update, new date: " + new String(nodeCache.getCurrentData().getData()));
			}
		});
		
		PathChildrenCache pathChildrenNode = new PathChildrenCache(client.getCurator(), "/pathChildren", true);
		pathChildrenNode.start();
		pathChildrenNode.getListenable().addListener(new PathChildrenCacheListener() {
			
			@Override
			public void childEvent(CuratorFramework client,
					PathChildrenCacheEvent event) throws Exception {
				//pathChildren current event: CHILD_ADDED
				//pathChildren current event: CHILD_REMOVED
				System.out.println("pathChildren current event: " + event.getType());
				pathChildrenNode.getCurrentData().forEach((child)->{
					System.out.println("child path : " + child.getPath() + ", data: " + new String(child.getData()));
				});
			}

		});

		LangUtils.CONSOLE.exitIf("exit");
	}
	

	
	@Test
	public void testTreeCache() throws Exception{
		
		final String path = "/pathChildren";

		TreeCache treeCache = new TreeCache(client.getCurator(), path);
		treeCache.start();
		treeCache.getListenable().addListener(new TreeCacheListener() {
			
			
			
			@Override
			public void childEvent(CuratorFramework client, TreeCacheEvent event)
					throws Exception {
				System.out.println("treeCache current event: " + event.getType());
				System.out.println(treeCache.getCurrentData(path).getPath() + ":" + new String(treeCache.getCurrentData(path).getData()));
				treeCache.getCurrentChildren(path).forEach((childPath, data)->{
					System.out.println("child path : " + childPath + ", data: " + new String(data.getData()));
				});
			}

		});

		LangUtils.CONSOLE.exitIf("exit");
	}

}
