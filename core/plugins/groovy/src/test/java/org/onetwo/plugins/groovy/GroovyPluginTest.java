package org.onetwo.plugins.groovy;

import javax.annotation.Resource;

import org.junit.Test;
import org.onetwo.plugins.groovy.model.TestGroovyBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(value="classpath:/groovy-test.xml")
public class GroovyPluginTest extends AbstractJUnit4SpringContextTests {

	@Resource
	private TestGroovyBean testGroovyBean;
	
	@Test
	public void testGroovyBean(){
		this.testGroovyBean.sayHello("groovy");
	}
}
