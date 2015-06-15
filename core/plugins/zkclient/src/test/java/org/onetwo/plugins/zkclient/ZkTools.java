package org.onetwo.plugins.zkclient;

import org.apache.curator.utils.ZKPaths;
import org.apache.curator.utils.ZKPaths.PathAndNode;
import org.apache.zookeeper.data.Stat;
import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.plugins.zkclient.curator.CuratorClient;

public class ZkTools {
	
	@Test
	public void testFixForNamespace(){
		String path = ZKPaths.fixForNamespace("test", "/child1");
		System.out.println("path: " + path);
		Assert.assertEquals("/test/child1", path);
		
		try {
			path = ZKPaths.fixForNamespace("/test", "child1");
			Assert.fail("must be fail here");
		} catch (Exception e) {
			Assert.assertEquals(IllegalArgumentException.class, e.getClass());
		}

		path = ZKPaths.fixForNamespace(null, "/child1");
		Assert.assertEquals("/child1", path);
	}
	
	@Test
	public void testMakePath(){
		String path = ZKPaths.makePath("test", "/child1");
		System.out.println("path: " + path);
		Assert.assertEquals("/test/child1", path);
		
		path = ZKPaths.makePath("/test", "child1");
		Assert.assertEquals("/test/child1", path);

		path = ZKPaths.makePath(null, "/child1");
		Assert.assertEquals("/child1", path);
		
		path = ZKPaths.makePath("test/testNode", "/child1");
		Assert.assertEquals("/test/testNode/child1", path);
		
		path = ZKPaths.makePath("test/testNode", "/child1/child12");
		Assert.assertEquals("/test/testNode/child1/child12", path);
		
		PathAndNode pn = ZKPaths.getPathAndNode(path);
		Assert.assertEquals("/test/testNode/child1", pn.getPath());
		Assert.assertEquals("child12", pn.getNode());
	}
	

	protected CuratorClient client;
	public void setup() throws Exception{
		String connectString = "127.0.0.1:2181";
		client = new CuratorClient("", connectString);
		client.afterPropertiesSet();
		System.out.println("ns: " + client.getCurator().getNamespace());
	}
	@Test
	public void testMakeDir() throws Exception{
		setup();
		String path = ZKPaths.makePath("test/makeDir", "/child1");
		ZKPaths.deleteChildren(client.getCurator().getZookeeperClient().getZooKeeper(), path, true);
		Stat stat = client.checkExists(path);
		Assert.assertNull(stat);
		ZKPaths.mkdirs(client.getCurator().getZookeeperClient().getZooKeeper(), path);
		stat = client.checkExists(path);
		Assert.assertNotNull(stat);
		/*client.getCurator().usingNamespace("/test/makeDir");
		stat = client.getCurator().checkExists().forPath("/child1");
		Assert.assertNotNull(stat);*/
		/*EnsurePath ensurePath = new EnsurePath(path);
		ensurePath.ensure(client.getCurator().getZookeeperClient());*/
	}

}
