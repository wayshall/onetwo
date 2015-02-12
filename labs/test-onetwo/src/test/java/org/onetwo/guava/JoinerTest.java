package org.onetwo.guava;

import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;

public class JoinerTest {
	
	@Test
	public void testJoinMap(){
		Map<String, String> map = ImmutableMap.of("key1", "value1", "key2", "value2");
		String str = Joiner.on(", ").withKeyValueSeparator(";").join(map);
		System.out.println("str: " + str);
		Assert.assertEquals(str, "key1;value1, key2;value2");
	}

}
