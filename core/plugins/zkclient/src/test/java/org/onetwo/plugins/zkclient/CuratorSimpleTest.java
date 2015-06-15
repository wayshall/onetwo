package org.onetwo.plugins.zkclient;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.onetwo.plugins.zkclient.curator.CuratorClient;

public class CuratorSimpleTest {
	
	static protected CuratorClient client;
	
	@BeforeClass
	static public void setup() throws Exception{
		String connectString = "127.0.0.1:2181";
		client = new CuratorClient("/test", connectString);
		client.afterPropertiesSet();
	}
	
	
	@Test
	public void test() throws Exception{
		List<String> paths = client.getChildren("");
		for(String path : paths){
			System.out.println("path: " + path);
		}
		paths = client.getChildren("", true);
		for(String path : paths){
			System.out.println("path: " + path + ", data: " + client.getData(path, String.class));
		}
	}

}
