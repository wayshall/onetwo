package org.onetwo.common.utils;

import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class CollectionsTest {

	@Test
	public void testSort(){
		List<Long> list = LangUtils.newArrayList(11L, 2L, 33L, 0L, -1L, 4L, 5L, 66L, 100L, 23L);
		Collections.sort(list);
		for(Long l :list){
			System.out.println(l);
		}
	}
}
