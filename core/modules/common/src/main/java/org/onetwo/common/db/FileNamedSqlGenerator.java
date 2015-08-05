package org.onetwo.common.db;

import org.onetwo.common.db.filequery.NamespaceProperty;

public interface FileNamedSqlGenerator<T extends NamespaceProperty> {

	public ParsedSqlContext generatSql();

}