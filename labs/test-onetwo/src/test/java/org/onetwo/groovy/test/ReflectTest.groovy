package org.onetwo.groovy.test

import org.junit.Test

class ReflectTest {
	
	@Test
	def void testGetStaticFieldValue(){
		Class menuClass = MenuTest.class;
		println menuClass.appCode
		println menuClass.name
	}

}
