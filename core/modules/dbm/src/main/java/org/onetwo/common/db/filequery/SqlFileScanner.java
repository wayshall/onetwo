package org.onetwo.common.db.filequery;

import java.util.Map;

import org.onetwo.common.propconf.ResourceAdapter;

public interface SqlFileScanner {
	public String SQL_POSTFIX = ".sql";
	/*public String JFISH_SQL_POSTFIX = ".jfish"+SQL_POSTFIX;*/

	/****
	 * 根据配置扫描文件
	 * @param conf
	 * @return
	 */
	Map<String, ResourceAdapter<?>> scanMatchSqlFiles(String dialectDir);
	
	/***
	 * 根据数据库和类路径加载对应是sql文件，如果dialet为null，默认会查找.jfish.sql后缀
	 * @param dialect
	 * @param classPath
	 * @return
	 */
	public <T extends ResourceAdapter<?>> T getClassPathResource(String dialect, String classPath);
	
	String getSqlPostfix();

	String getJfishPostfix();
}