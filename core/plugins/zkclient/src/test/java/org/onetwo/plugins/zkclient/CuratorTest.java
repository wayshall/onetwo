package org.onetwo.plugins.zkclient;

import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
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
	public void test() throws Exception{
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

}
