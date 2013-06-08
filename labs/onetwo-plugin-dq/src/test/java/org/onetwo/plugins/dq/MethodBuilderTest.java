package org.onetwo.plugins.dq;

import java.util.List;

import org.junit.Test;
import org.onetwo.common.utils.Page;

import junit.framework.Assert;

public class MethodBuilderTest {

	@Test
	public void testGenericType(){
		String str = MethodBuilder.typeAsString(List.class, Integer.class);
		Assert.assertEquals("java.util.List<java.lang.Integer>", str);

		str = MethodBuilder.typeAsString(Integer.class);
		Assert.assertEquals("java.lang.Integer", str);
		
		str = MethodBuilder.typeAsString(Integer[].class);
		Assert.assertEquals("java.lang.Integer[]", str);
	}

	@Test
	public void testGenerateMethod(){
		String mstr = MethodBuilder.newPublicMethod()._final().name("findPage").arg("page", Page.class, Object.class);
	}
}
