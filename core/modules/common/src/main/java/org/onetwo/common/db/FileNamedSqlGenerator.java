package org.onetwo.common.db;

import org.onetwo.common.utils.propconf.NamespaceProperty;

public interface FileNamedSqlGenerator<T extends NamespaceProperty> {

	public ParsedSqlContext generatSql();

}