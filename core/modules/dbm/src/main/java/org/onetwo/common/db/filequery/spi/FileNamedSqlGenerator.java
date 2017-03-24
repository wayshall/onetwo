package org.onetwo.common.db.filequery.spi;

import org.onetwo.common.db.ParsedSqlContext;

public interface FileNamedSqlGenerator {

	public ParsedSqlContext generatSql();

}