package org.onetwo.common.db.filequery;

import java.util.Map;

import org.onetwo.common.propconf.ResourceAdapter;

public interface SqlFileScanner {
	public String SQL_POSTFIX = ".sql";
	public String JFISH_SQL_POSTFIX = ".jfish"+SQL_POSTFIX;

	/****
	 * 根据配置扫描文件
	 * @param conf
	 * @return
	 */
	Map<String, ResourceAdapter<?>> scanMatchSqlFiles(String dialectDir);

}