package org.onetwo.common;

import org.junit.Test;

public class SimpleTest {
	
	@Test
	public void test(){
		String str = "当前使用的币数[%s]超过了规则限制使用的币数[%s]！(%s)";
		String res = String.format(str, 100, 100, 100);
		System.out.println("res:"+res);
	}

}
