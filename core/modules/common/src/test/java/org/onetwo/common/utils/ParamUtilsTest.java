package org.onetwo.common.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.Maps;

public class ParamUtilsTest {
	
	@Test
	public void test() {
		Map<String, Object> params = Maps.newHashMap();
		params.put("aa", 222);
		params.put("bb", Arrays.asList(33, 44));
		String res = ParamUtils.toParamString(params);
		System.out.println("res: " + res);
		assertThat(res).isEqualTo("aa=222&bb=33&bb=44");
		
		res = ParamUtils.toParamStringWithBrace(params);
		System.out.println("res: " + res);
		assertThat(res).isEqualTo("aa={aa}&bb={bb}&bb={bb}");
	}

}
