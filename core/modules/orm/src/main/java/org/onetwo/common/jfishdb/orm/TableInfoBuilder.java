package org.onetwo.common.jfishdb.orm;

import org.onetwo.common.jfishdb.orm.TableInfo;

public interface TableInfoBuilder {

	/*String getTableName(Class<?> entityClass);
	
	List<String> getFieldNames(Class<?> entityClass);*/
	
//	TableInfo buildTableInfo(Class<?> entityClass);
	TableInfo buildTableInfo(Object object);
}
