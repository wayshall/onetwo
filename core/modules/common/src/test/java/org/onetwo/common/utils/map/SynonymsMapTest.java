package org.onetwo.common.utils.map;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

public class SynonymsMapTest {

	@Test
	public void test(){
		SynonymsMap map = SynonymsMap.buildFromClassPath("synonyms.txt");
		Collection<String> values = map.get("广州");
		System.out.println("广州values:"+values);
		Assert.assertTrue(values.containsAll(Arrays.asList("广州", "羊城", "花城", "广州市")));
		values = map.get("羊城");
		System.out.println("羊城values:"+values);
		Assert.assertTrue(values.containsAll(Arrays.asList("广州", "羊城", "花城", "广州市")));

		values = map.get("花城");
		System.out.println("花城values:"+values);
		Assert.assertTrue(values.containsAll(Arrays.asList("广州", "羊城", "花城", "广州市")));
	}
	
}
