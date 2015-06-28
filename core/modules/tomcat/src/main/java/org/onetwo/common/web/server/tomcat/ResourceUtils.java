package org.onetwo.common.web.server.tomcat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class ResourceUtils {
	static final private PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();

	public static List<Resource> scanResources(String pluginPath){
		List<Resource> pluginFiles = new ArrayList<Resource>();
		try {
			Resource[] resources = patternResolver.getResources(pluginPath);
			pluginFiles.addAll(Arrays.asList(resources));
		} catch (IOException e1) {
			throw new RuntimeException("scan resource error: " + e1.getMessage(), e1);
		}
		return pluginFiles;
	}
	public static Resource getResource(String pluginPath){
		return patternResolver.getResource(pluginPath);
	}
}
