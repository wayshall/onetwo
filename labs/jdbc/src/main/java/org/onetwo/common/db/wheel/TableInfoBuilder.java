package org.onetwo.common.db.wheel;

import org.onetwo.common.db.wheel.TableInfo;

public interface TableInfoBuilder {

	/*String getTableName(Class<?> entityClass);
	
	List<String> getFieldNames(Class<?> entityClass);*/
	
//	TableInfo buildTableInfo(Class<?> entityClass);
	TableInfo buildTableInfo(Object object);
}
