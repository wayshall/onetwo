package org.onetwo.plugins.groovy;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(loader=GroovyContextLoaderForTest.class )
//for database test
//public class GroovyPluginTest extends SpringBaseJUnitTestCase {
public class GroovyPluginTest extends AbstractJUnit4SpringContextTests {


	@Test
	public void test(){
		System.out.println("Test");
	}
}
