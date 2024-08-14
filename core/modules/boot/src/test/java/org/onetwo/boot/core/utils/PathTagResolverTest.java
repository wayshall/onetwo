package org.onetwo.boot.core.utils;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.boot.core.config.BootSiteConfig.ImageServer;
import org.onetwo.boot.core.web.utils.PathTagResolver;

import com.google.common.collect.Maps;

public class PathTagResolverTest {
	PathTagResolver pathTagResolver;
	@Before
	public void setup() {
		pathTagResolver = new PathTagResolver();
		BootSiteConfig siteConfig = new BootSiteConfig();
		ImageServer imageServer = siteConfig.new ImageServer();

		Map<String, String> pathTags = Maps.newHashMap();
		pathTags.put("sftp", "http://test.com/images");
		imageServer.setPathTags(pathTags);
		siteConfig.setImageServer(imageServer);
		
		pathTagResolver.setSiteConfig(siteConfig);
	}
	
	@Test
	public void test() {
		String url = pathTagResolver.parsePathTag("{sftp}/picture/1ff3e6dc-1481-4652-99a0-38cdff357563.jpg");
		System.out.println("url: " + url);
	}

}
