package org.onetwo.dbm.mapping;

import org.onetwo.dbm.mapping.TableInfo;

public interface TableInfoBuilder {

	/*String getTableName(Class<?> entityClass);
	
	List<String> getFieldNames(Class<?> entityClass);*/
	
//	TableInfo buildTableInfo(Class<?> entityClass);
	TableInfo buildTableInfo(Object object);
}
