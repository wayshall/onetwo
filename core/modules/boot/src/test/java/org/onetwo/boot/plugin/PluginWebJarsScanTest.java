package org.onetwo.boot.plugin;

import java.util.List;

import org.junit.Test;
import org.onetwo.common.spring.utils.ResourceUtils;
import org.springframework.core.io.Resource;
import org.springframework.ui.freemarker.SpringTemplateLoader;

import freemarker.cache.TemplateLoader;

public class PluginWebJarsScanTest {
	
	@Test
	public void testScan() throws Exception{
		List<Resource> reslist = ResourceUtils.scanResources("classpath*:META-INF/resources/webftls");
		System.out.println("reslist:"+reslist);
		reslist.forEach(res->{
			System.out.println("res:"+res);
		});
		TemplateLoader loader = new SpringTemplateLoader(ResourceUtils.getResourceLoader(), reslist.get(0).getURI().getPath());
		Object ftlRes = loader.findTemplateSource("test.ftl");
		System.out.println("ftlRes:"+ftlRes);
		
		loader = new SpringTemplateLoader(ResourceUtils.getResourceLoader(), "META-INF/resources/webftls");
		ftlRes = loader.findTemplateSource("test2.ftl");
		System.out.println("ftlRes:"+ftlRes);
	}

}
