package org.onetwo.common.spring.utils;

import java.net.URL;
import java.util.List;

import org.junit.Test;
import org.onetwo.common.utils.ClassUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.list.NoIndexIt;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

public class JFishResourcesScannerTest {

	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	
	@Test
	public void testScan(){
		JFishResourcesScanner scanner = ResourcesScanner.CLASS_CANNER;
		List<Class<?>> cls = scanner.scan(JaxbClassFilter.Instance,Page.class.getPackage().getName());
		for(Class<?> c : cls){
			System.out.println(c);
		}
	}
	@Test
	public void testScan2() throws Exception{
		Resource[] res = resourcePatternResolver.getResources("classpath*:/**/*.class");

		System.out.println("res: " + res);
		for(Resource path : res){
			System.out.println("path: " + path);
		}
	}
	@Test
	public void testScan3() throws Exception{
		ClassUtils.getResources("META-INF/notice.txt").each(new NoIndexIt<URL>(){

			@Override
			public void doIt(URL element) {
				System.out.println("url: " + element.getPath());
			}
			
		});
	}
}
