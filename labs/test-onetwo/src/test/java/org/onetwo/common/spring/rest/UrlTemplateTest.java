package org.onetwo.common.spring.rest;

import java.net.URI;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.onetwo.common.utils.LangUtils;
import org.springframework.web.util.UriTemplate;

public class UrlTemplateTest {
	
	@Test
	public void testUrl(){
		String url = "http://localhost:8080/test/{id}";
		Map<String, Object> urlVariables = LangUtils.asMap("id", 1);
		URI expanded = new UriTemplate(url).expand(urlVariables);
		System.out.println(expanded);
		Assert.assertEquals("http://localhost:8080/test/1", expanded.toString());
		
		url = "http://localhost:8080/test/{id}.json?userName={userName}";
		urlVariables = LangUtils.asMap("id", 1, "userName", "test");
		expanded = new UriTemplate(url).expand(urlVariables);
		System.out.println(expanded);
		Assert.assertEquals("http://localhost:8080/test/1.json?userName=test", expanded.toString());
	}

}
