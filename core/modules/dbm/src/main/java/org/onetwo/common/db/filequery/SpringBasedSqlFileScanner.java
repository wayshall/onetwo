package org.onetwo.common.db.filequery;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.propconf.ResourceAdapter;
import org.onetwo.common.spring.utils.SpringResourceAdapterImpl;
import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

public class SpringBasedSqlFileScanner extends SimpleSqlFileScanner {
	
	public SpringBasedSqlFileScanner(ClassLoader classLoader) {
		super(classLoader);
	}

	@Override
	public ResourceAdapter<?>[] scanMatchSqlFiles(String dialectDir){
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		
		String locationPattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + dir;
		String sqldirPath = locationPattern+"/**/*"+postfix;
		
		ResourceAdapter<?>[] allSqlFiles = null;
		try {
			Resource[] sqlfileArray = resourcePatternResolver.getResources(sqldirPath);
			
			//scan dialectDir
			if(StringUtils.isNotBlank(dialectDir)){
				sqldirPath = locationPattern+"/"+dialectDir+"/**/*"+postfix;
				logger.info("scan database dialect dir : " + sqldirPath);
				Resource[] dbsqlfiles = resourcePatternResolver.getResources(sqldirPath);
				if(!LangUtils.isEmpty(dbsqlfiles)){
					sqlfileArray = (Resource[]) ArrayUtils.addAll(sqlfileArray, dbsqlfiles);
				}
			}
			
			allSqlFiles = new ResourceAdapter[sqlfileArray.length];
			int index = 0;
			for(Resource rs : sqlfileArray){
				allSqlFiles[index++] = new SpringResourceAdapterImpl(rs);
			}
		} catch (Exception e) {
			throw new BaseException("scan sql file error: " + e.getMessage());
		}
		
		return allSqlFiles;
	}
}
