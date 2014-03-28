package org.onetwo.common.utils;

import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Test;

public class ToStringBuilderTest {
	
	@Test
	public void test(){
		Map<String, Object> maps = LangUtils.asMap("aa", new String[]{"aa1", "aa2"}, "bb", new Integer[]{1,2});
		ToStringBuilder tsb = new ToStringBuilder(maps, ToStringStyle.MULTI_LINE_STYLE);
		tsb.append(LangUtils.toString(maps));
		String str = tsb.build();
		System.out.println("maps: " + str);
	}

}
