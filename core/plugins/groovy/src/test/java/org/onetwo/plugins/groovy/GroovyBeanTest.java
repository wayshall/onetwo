package org.onetwo.plugins.groovy;

import javax.annotation.Resource;

import org.junit.Test;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.plugins.groovy.model.TestGroovyBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(loader=GroovyContextLoaderForTest.class )
public class GroovyBeanTest extends AbstractJUnit4SpringContextTests {

	@Resource
	private TestGroovyBean testGroovyBean;
	

	@Test
	public void testGroovyBean(){
		for (int i = 0; i < 100; i++) {
			this.testGroovyBean.sayHello("groovy"+i);
			LangUtils.await(2);
		}
	}
}
