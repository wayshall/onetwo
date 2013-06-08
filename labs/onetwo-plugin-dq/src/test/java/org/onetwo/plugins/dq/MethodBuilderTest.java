package org.onetwo.plugins.dq;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.onetwo.common.utils.Page;

public class MethodBuilderTest {

	@Test
	public void testGenericType(){
		String str = MethodBuilder.class2String(List.class, Integer.class);
		Assert.assertEquals("java.util.List<java.lang.Integer>", str);

		str = MethodBuilder.typeAsString(Integer.class);
		Assert.assertEquals("java.lang.Integer", str);
		
		str = MethodBuilder.typeAsString(Integer[].class);
		Assert.assertEquals("java.lang.Integer[]", str);
	}

	@Test
	public void testGenerateMethod(){
		String mstr = MethodBuilder.newPublicMethod()._final()._return(null).name("findPage").arg("page", Page.class).body("return null;");
		System.out.println(mstr);
		Assert.assertEquals("public final void findPage (org.onetwo.common.utils.Page page ){return null;}", mstr);
	}
}
