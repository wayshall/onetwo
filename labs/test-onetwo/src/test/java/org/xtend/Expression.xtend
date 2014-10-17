package org.xtend

import org.junit.Test

import static org.junit.Assert.*

class Expression {
	
	@Test
	def void test1(){
		assertEquals(42.km/h, (40_000.m + 2.km)/60.min);
	}
	
}