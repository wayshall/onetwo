package org.onetwo.boot.permission.utils;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.boot.plugins.permission.utils.UrlResourceInfo;
import org.onetwo.boot.plugins.permission.utils.UrlResourceInfoParser;

public class UrlResourceInfoParserTest {
	
	UrlResourceInfoParser parser = new UrlResourceInfoParser();
	
	@Test
	public void testParser(){
		List<UrlResourceInfo> urlResourceInfo = parser.parseToUrlResourceInfos(null);
		Assert.assertTrue(urlResourceInfo.size()==0);
		
		urlResourceInfo = parser.parseToUrlResourceInfos("  ");
		Assert.assertTrue(urlResourceInfo.size()==0);
		
		urlResourceInfo = new ArrayList<UrlResourceInfo>();
		urlResourceInfo.add(new UrlResourceInfo("/user/list"));
		String pasedString = parser.parseToString(urlResourceInfo);
		System.out.println("parsedstirng: " + pasedString);
		
		List<UrlResourceInfo> parsedResourceInfo = parser.parseToUrlResourceInfos(pasedString);
		Assert.assertEquals(urlResourceInfo, parsedResourceInfo);
		
		urlResourceInfo.add(new UrlResourceInfo("/user/create", "post"));
		pasedString = parser.parseToString(urlResourceInfo);
		System.out.println("parsedstirng: " + pasedString);
		
		parsedResourceInfo = parser.parseToUrlResourceInfos(pasedString);
		Assert.assertEquals(urlResourceInfo, parsedResourceInfo);
	}

}
