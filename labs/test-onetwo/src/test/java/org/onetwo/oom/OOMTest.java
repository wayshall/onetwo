package org.onetwo.oom;

import java.util.List;

import org.junit.Test;
import org.onetwo.common.utils.LangUtils;

public class OOMTest {

	@Test
	public void testOOM(){
		int count = 10;
		List<String> strs = LangUtils.newArrayList();
		while(true){
			for (int i = 0; i < count; i++) {
				strs.add("test oom " + i);
			}
	
			LangUtils.await(1);
		}
	}
}
