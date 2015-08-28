package org.onetwo.common.db.filequery;

import java.io.File;

import org.onetwo.common.file.FileUtils;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.ArrayUtils;
import org.onetwo.common.utils.ClassUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.propconf.ResourceAdapter;
import org.slf4j.Logger;

public class SimpleSqlFileScanner implements SqlFileScanner {
	protected Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	protected ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
	protected String dir = "sql";
	protected String postfix = JFISH_SQL_POSTFIX;
	

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
	public ResourceAdapter<?>[] scanMatchSqlFiles(String dialectDir){
		String sqldirPath = FileUtils.getResourcePath(classLoader, dir);

		File[] sqlfileArray = FileUtils.listFiles(sqldirPath, postfix);
		if(logger.isInfoEnabled())
			logger.info("find {} sql named file in dir {}", sqlfileArray.length, sqldirPath);
		
		//scan dialectDir
		if(StringUtils.isNotBlank(dialectDir)){
			String overridePath = sqldirPath+"/"+dialectDir;
			logger.info("scan database dialect dir : " + overridePath);
			File[] dbsqlfiles = FileUtils.listFiles(overridePath, postfix);

			if(logger.isInfoEnabled())
				logger.info("find {} sql named file in dir {}", dbsqlfiles.length, overridePath);
			
			if(!LangUtils.isEmpty(dbsqlfiles)){
				sqlfileArray = (File[]) ArrayUtils.addAll(sqlfileArray, dbsqlfiles);
			}
		}
		
		return FileUtils.adapterResources(sqlfileArray);
	}
}
