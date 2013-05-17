package org.onetwo.common.utils;

import org.junit.Assert;
import org.junit.Test;

public class ExpressionTest {

	@Test
	public void testExpression(){
		String path = "/${path}/${id}";
		path = Expression.DOLOR.parse(path, "path", "test");
		Assert.assertEquals(path, "/test/null");
	}
}
