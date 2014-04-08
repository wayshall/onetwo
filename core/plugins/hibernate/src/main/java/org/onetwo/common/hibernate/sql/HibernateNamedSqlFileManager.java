package org.onetwo.common.hibernate.sql;

import org.onetwo.common.jdbc.DataBase;
import org.onetwo.common.spring.sql.JFishNamedSqlFileManager;

public class HibernateNamedSqlFileManager extends JFishNamedSqlFileManager<HibernateNamedInfo> {

	public HibernateNamedSqlFileManager(DataBase databaseType, boolean watchSqlFile, Class<HibernateNamedInfo> propertyBeanClass) {
		super(databaseType, watchSqlFile, propertyBeanClass);
	}

}
