package org.onetwo.common.utils;

import java.util.BitSet;

import org.junit.Assert;
import org.junit.Test;

public class BitSetTest {
	
	@Test
	public void test(){
		int[] numbs = new int[]{3, 7, 16, 8};
		BitSet bs = new BitSet(64);
		for (int i = 0; i < numbs.length; i++) {
			bs.set(numbs[i]);
		}
		Assert.assertFalse(bs.get(0));
		Assert.assertFalse(bs.get(1));
		Assert.assertTrue(bs.get(3));
		Assert.assertTrue(bs.get(16));
	}

}
