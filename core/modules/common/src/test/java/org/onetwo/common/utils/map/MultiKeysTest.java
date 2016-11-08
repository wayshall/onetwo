package org.onetwo.common.utils.map;

import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.junit.Assert;
import org.junit.Test;

public class MultiKeysTest {

	@Test
	public void test(){
		MultiKey key1 = new MultiKey(32, "test");
		MultiKey key2 = new MultiKey("test", 32);
		Assert.assertEquals(key1.hashCode(), key2.hashCode());
		Assert.assertNotEquals(key1, key2);
		
		MultiKeyMap map = new MultiKeyMap();
		map.put(key1, "test");
		String value = (String)map.get(key1);
		String value2 = (String)map.get(key1);
		Assert.assertEquals(value, value2);
		
	}
}
