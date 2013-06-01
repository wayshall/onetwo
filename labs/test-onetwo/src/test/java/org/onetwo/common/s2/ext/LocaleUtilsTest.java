package org.onetwo.common.s2.ext;

import java.util.Locale;
import java.util.Map;

import org.junit.Test;
import org.onetwo.common.web.s2.ext.LocaleUtils;

public class LocaleUtilsTest {
	
	@Test
	public void testConfig(){
		Locale pt = new Locale("pt", "pt");
		Map map = LocaleUtils.VALUES;
		System.out.println("map: " + map);
		System.out.println("containsValue: " + map.containsValue(pt));
	}

}
