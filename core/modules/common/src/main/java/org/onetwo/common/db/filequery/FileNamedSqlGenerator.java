package org.onetwo.common.db.filequery;

import org.onetwo.common.db.ParsedSqlContext;

public interface FileNamedSqlGenerator<T extends NamespaceProperty> {

	public ParsedSqlContext generatSql();

}