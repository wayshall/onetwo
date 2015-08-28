package org.onetwo.common.db.filequery;

import org.onetwo.common.propconf.ResourceAdapter;

public interface SqlFileScanner {
	public static final String JFISH_SQL_POSTFIX = ".jfish.sql";

	/****
	 * 根据配置扫描文件
	 * @param conf
	 * @return
	 */
	ResourceAdapter<?>[] scanMatchSqlFiles(String dialectDir);

}