package org.onetwo.plugins.groovy.model

import javax.annotation.Resource

/****
 * copy to classpath
 * @author wayshall
 *
 */
class TestGroovyBeanImpl implements TestGroovyBean {
	
	@Resource
	def JavaServiceImpl javaServiceImpl;
	
	void sayHello(String name){
		println("hello ${name}, this is " + javaServiceImpl.getName());
	}

}
