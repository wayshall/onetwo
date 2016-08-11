package org.onetwo.common.spring.utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class ResourceUtils {
	static final private PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
	private final static Logger logger = JFishLoggerFactory.getLogger(ResourceUtils.class);

	public static List<Resource> scanResources(String scanPath){
		List<Resource> pluginFiles = null;
		try {
			Resource[] resources = patternResolver.getResources(scanPath);
			pluginFiles = Arrays.asList(resources);
		} catch (IOException e1) {
			logger.error("scan resource error: " + e1.getMessage());
			return Collections.EMPTY_LIST;
		}
		return pluginFiles;
	}
	public static Resource getResource(String pluginPath){
		return patternResolver.getResource(pluginPath);
	}
	public static ResourceLoader getResourceLoader() {
		return patternResolver.getResourceLoader();
	}
	
	
}
