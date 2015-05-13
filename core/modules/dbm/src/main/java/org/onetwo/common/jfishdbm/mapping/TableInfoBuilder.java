package org.onetwo.common.jfishdbm.mapping;

import org.onetwo.common.jfishdbm.mapping.TableInfo;

public interface TableInfoBuilder {

	/*String getTableName(Class<?> entityClass);
	
	List<String> getFieldNames(Class<?> entityClass);*/
	
//	TableInfo buildTableInfo(Class<?> entityClass);
	TableInfo buildTableInfo(Object object);
}
