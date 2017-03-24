package org.onetwo.common.db.filequery.spi;

import org.onetwo.common.db.filequery.DbmNamedQueryFile;
import org.onetwo.common.propconf.ResourceAdapter;

/****
 * 解释sql文件，构建为对象
 * @author way
 *
 * @param <T>
 */
public interface SqlFileParser {

	String POSTFIX = SqlFileScanner.SQL_POSTFIX;
	
	void parseToNamespaceProperty(DbmNamedQueryFile np, ResourceAdapter<?> file);
	
}
