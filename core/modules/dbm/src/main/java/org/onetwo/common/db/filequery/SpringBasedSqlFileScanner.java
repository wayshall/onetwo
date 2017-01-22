package org.onetwo.common.db.filequery;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.onetwo.common.propconf.ResourceAdapter;
import org.onetwo.common.spring.utils.SpringResourceAdapterImpl;
import org.onetwo.common.utils.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

public class SpringBasedSqlFileScanner extends SimpleSqlFileScanner {
	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	
	public SpringBasedSqlFileScanner(ClassLoader classLoader) {
		super(classLoader);
	}

	protected Map<String, ResourceAdapter<?>> scanCommonSqlFiles() throws Exception{
		String locationPattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + dir;
		String postfix = this.jfishPostfix;
		
		String sqldirPath = locationPattern+"/**/*"+postfix;
		Resource[] sqlfileArray = resourcePatternResolver.getResources(sqldirPath);
		return Stream.of(sqlfileArray)
						.map(f->new SpringResourceAdapterImpl(f, postfix))
						.collect(Collectors.toMap(keyfunc(postfix), r->r));
	}

	protected Map<String, ResourceAdapter<?>> scanDialetSqlFiles(String dialectDir) throws Exception{
		if(StringUtils.isBlank(dialectDir)){
			return null;
		}
		String locationPattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + dir;
//		String postfix = "."+dialectDir+SQL_POSTFIX;
		String postfix = getDialetSqlPostfix(dialectDir);
		
		String sqldirPath = locationPattern+"/**/*"+postfix;
		logger.info("scan database dialect dir : " + sqldirPath);
		Resource[] dbsqlfiles = resourcePatternResolver.getResources(sqldirPath);
		return Stream.of(dbsqlfiles)
				.map(f->new SpringResourceAdapterImpl(f, postfix))
				.collect(Collectors.toMap(keyfunc(postfix), r->r));
	}
	
}
