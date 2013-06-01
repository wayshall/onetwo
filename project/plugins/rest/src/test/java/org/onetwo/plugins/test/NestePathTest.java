package org.onetwo.plugins.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

public class NestePathTest {

	@Test
	public void testNestedPath(){
		String str = "aa[0][bbb_bb][cc_dd]";
		final Pattern NESTED_PATH_PATTER = Pattern.compile("(\\[([a-z]+[\\w]+)\\])");
		Matcher m = NESTED_PATH_PATTER.matcher(str);
		Assert.assertTrue(m.find());
		String newPath = m.replaceAll(".$2");
		Assert.assertEquals("aa[0].bbb_bb.cc_dd", newPath);
		
		str = "bb[0][bbb_bb][cc_dd]";
		m = NESTED_PATH_PATTER.matcher(str);
		Assert.assertTrue(m.find());
		newPath = m.replaceAll(".$2");
		Assert.assertEquals("bb[0].bbb_bb.cc_dd", newPath);

	}

}
