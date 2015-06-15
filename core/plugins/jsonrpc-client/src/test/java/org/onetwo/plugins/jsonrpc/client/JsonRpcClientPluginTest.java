package org.onetwo.plugins.jsonrpc.client;


import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(loader=JsonRpcClientContextLoaderForTest.class )
//for database test
//public class GroovyPluginTest extends SpringBaseJUnitTestCase {
public class JsonRpcClientPluginTest extends AbstractJUnit4SpringContextTests {


	@Test
	public void test(){
		System.out.println("Test");
	}
}
