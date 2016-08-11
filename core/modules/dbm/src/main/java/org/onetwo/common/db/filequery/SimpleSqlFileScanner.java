package org.onetwo.common.db.filequery;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.onetwo.common.file.FileUtils;
import org.onetwo.common.jfishdbm.exception.DbmException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.propconf.ResourceAdapter;
import org.onetwo.common.utils.ClassUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;

import com.google.common.collect.Maps;

public class SimpleSqlFileScanner implements SqlFileScanner {
	protected Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	protected ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
	protected String dir = "sql";
//	protected String postfix = JFISH_SQL_POSTFIX;
	

	public SimpleSqlFileScanner(ClassLoader classLoader) {
		super();
		this.classLoader = classLoader;
	}



	/****
	 * 根据配置扫描文件
	 * @param conf
	 * @return
	 */
	@Override
	public Map<String, ResourceAdapter<?>> scanMatchSqlFiles(String dialectDir){
		try {
			Map<String, ResourceAdapter<?>> allSqlFileMap = Maps.newHashMap();
			
			Map<String, ResourceAdapter<?>> sqlFileMap = scanCommonSqlFiles();
			if(!LangUtils.isEmpty(sqlFileMap)){
				allSqlFileMap.putAll(sqlFileMap);
			}
			//scan dialectDir
			Map<String, ResourceAdapter<?>> dbsqlfileMap = scanDialetSqlFiles(dialectDir);
			
			if(!LangUtils.isEmpty(dbsqlfileMap)){
				allSqlFileMap.putAll(dbsqlfileMap);
			}
			
			return allSqlFileMap;
		} catch (Exception e) {
			throw new DbmException("scan sql file error!", e);
		}
	}

	protected Map<String, ResourceAdapter<?>> scanCommonSqlFiles() throws Exception {
		String sqldirPath = FileUtils.getResourcePath(classLoader, dir);

		String postfix = JFISH_SQL_POSTFIX;
		ResourceAdapter<?>[] sqlfileArray = scanSqlFilesByDir(sqldirPath, postfix);
		if(logger.isInfoEnabled())
			logger.info("find {} [{}] named file in dir {}", sqlfileArray.length, postfix, sqldirPath);
		return map(Arrays.asList(sqlfileArray), postfix);
	}

	protected Map<String, ResourceAdapter<?>> scanDialetSqlFiles(String dialectDir)  throws Exception {
		if(StringUtils.isBlank(dialectDir)){
			return null;
		}

		String postfix = "."+dialectDir+SQL_POSTFIX;
		String sqldirPath = FileUtils.getResourcePath(classLoader, dir);
/*		String overridePath = sqldirPath+"/"+dialectDir;
		logger.info("scan database dialect dir : " + overridePath);
		ResourceAdapter<?>[] dbsqlfiles = scanSqlFilesByDir(overridePath, postfix);
		if(logger.isInfoEnabled())
			logger.info("find {} sql named file in dir {}", dbsqlfiles.length, overridePath);*/
		ResourceAdapter<?>[] dbsqlfiles = scanSqlFilesByDir(sqldirPath, postfix);
		if(logger.isInfoEnabled())
			logger.info("find {} [{}] named file in dir {}", dbsqlfiles.length, postfix, sqldirPath);
		return  map(Arrays.asList(dbsqlfiles), postfix);
	}
	
	protected ResourceAdapter<?>[] scanSqlFilesByDir(String sqldirPath, String postfix){
		File[] sqlfileArray = FileUtils.listFiles(sqldirPath, postfix);
		return FileUtils.adapterResources(sqlfileArray, postfix);
	}
	
	protected Function<ResourceAdapter<?>, String> keyfunc(String postfix){
		return r->{
			String fileName = r.getName();
			fileName = fileName.substring(0, fileName.length()-postfix.length());
			return fileName;
		};
	}
	
	protected Map<String, ResourceAdapter<?>> map(Collection<ResourceAdapter<?>> resources, String postfix){
		return resources.stream().collect(Collectors.toMap(keyfunc(postfix), r->r));
	}
}
