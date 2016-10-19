package org.onetwo.web.utils;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.MediaType;

public class MediaTypeTest {
	
	@Test
	public void test(){
		MediaType mtype = MediaType.valueOf("application/json");
		MediaType jsonUtf8 = MediaType.valueOf("application/json;charset=UTF-8");
		Assert.assertTrue(jsonUtf8.isCompatibleWith(mtype));
		Assert.assertTrue(mtype.isCompatibleWith(jsonUtf8));
	}

}
