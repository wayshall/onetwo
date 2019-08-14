package org.onetwo.common.spring.resource;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.propconf.ResourceAdapter;
import org.onetwo.common.spring.utils.SpringResourceAdapterImpl;
import org.onetwo.common.utils.ClassUtils;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

public class SpringResourcesScanner {
	protected Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	protected ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
	protected String baseDir;
	protected String postfix;
	
	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	
	public SpringResourcesScanner(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	public List<ResourceAdapter<?>> scan() {
		String locationPattern = null;
		if (StringUtils.isBlank(baseDir)) {
			locationPattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX;
		} else {
			locationPattern = FileUtils.convertDir(baseDir);
		}
		
		String sqldirPath = locationPattern+"/**/*"+postfix;
		Resource[] sqlfileArray;
		try {
			sqlfileArray = resourcePatternResolver.getResources(sqldirPath);
		} catch (IOException e) {
			throw new BaseException("scan resource error, dir: " + baseDir + ", postfix: " + postfix, e);
		}
		return Stream.of(sqlfileArray)
						.map(f->new SpringResourceAdapterImpl(f, postfix))
						.collect(Collectors.toList());
	}

	
}
