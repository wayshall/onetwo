package org.onetwo.common.spring;

import org.junit.Test;
import org.onetwo.common.spring.plugin.SpringContextPluginManager;
import org.onetwo.common.spring.utils.ResourceUtils;
import org.onetwo.common.spring.utils.SpringResourceAdapterImpl;
import org.onetwo.common.utils.list.JFishList;
import org.onetwo.common.utils.list.NoIndexIt;
import org.onetwo.common.utils.propconf.ResourceAdapter;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class ScanResourceTest {

	private PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();

	@Test
	public void testScan(){
		JFishList<Resource> pluginFiles = ResourceUtils.scanResources(SpringContextPluginManager.PLUGIN_PATH);
		
		System.out.println("pluginFiles: " + pluginFiles.size());
		pluginFiles.each(new NoIndexIt<Resource>(){

			@Override
			public void doIt(Resource pluginFile) throws Exception {
				ResourceAdapter ra = new SpringResourceAdapterImpl(pluginFile);
				System.out.println("getURI().toString(): " + pluginFile.getURI().toString());
				System.out.println("getURI().getPath(): " + pluginFile.getURI().getPath());
				System.out.println("getURL().toString(): " + pluginFile.getURL().toString());
				System.out.println("getURL().getPath(): " + pluginFile.getURL().getPath());
			}
			
		});
	}
}
