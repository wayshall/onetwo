package org.onetwo.common.db;

import org.onetwo.common.utils.propconf.AbstractPropertiesManager.NamespaceProperty;

public interface FileNamedSqlGenerator<T extends NamespaceProperty> {

	public SqlAndValues generatSql();

}