package org.onetwo.common.hibernate.sql;

import org.onetwo.common.jdbc.DataBase;
import org.onetwo.common.spring.sql.JFishNamedSqlFileManager;
import org.onetwo.common.utils.propconf.PropertiesNamespaceInfoListener;

public class HibernateNamedSqlFileManager extends JFishNamedSqlFileManager<HibernateNamedInfo> {

	public HibernateNamedSqlFileManager(DataBase databaseType, boolean watchSqlFile, Class<HibernateNamedInfo> propertyBeanClass, PropertiesNamespaceInfoListener<HibernateNamedInfo> listener) {
		super(databaseType, watchSqlFile, propertyBeanClass, listener);
	}

}
