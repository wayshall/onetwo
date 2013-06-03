package org.onetwo.plugins.jdoc.jlexer;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.plugins.jdoc.Lexer.defined.MethodDefinedImpl;

public class MethodDefinedTest {
	
	@Test
	public void testMethodEq(){
		MethodDefinedImpl m1 = new MethodDefinedImpl(null, "test");
		m1.addParameter("p1", "String");
		MethodDefinedImpl m2 = new MethodDefinedImpl(null, "test");
		m2.addParameter("p2", "String");
		
		Assert.assertEquals(m1, m2);
	}

}
