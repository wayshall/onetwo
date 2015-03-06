package org.onetwo.common.spring.utils;

import java.io.IOException;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.list.JFishList;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class ResourceUtils {
	static final private PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();

	public static JFishList<Resource> scanResources(String pluginPath){
		JFishList<Resource> pluginFiles = null;
		try {
			Resource[] resources = patternResolver.getResources(pluginPath);
			pluginFiles = new JFishList<Resource>(resources.length);
			pluginFiles.addArray(resources);
		} catch (IOException e1) {
			throw new BaseException("scan resource error: " + e1.getMessage(), e1);
		}
		return pluginFiles;
	}
	public static Resource getResource(String pluginPath){
		return patternResolver.getResource(pluginPath);
	}
}
