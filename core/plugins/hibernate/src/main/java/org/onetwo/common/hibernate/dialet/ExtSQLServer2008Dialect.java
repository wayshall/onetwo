package org.onetwo.common.hibernate.dialet;

import java.sql.Types;

import org.hibernate.dialect.SQLServer2008Dialect;
import org.hibernate.type.StandardBasicTypes;

public class ExtSQLServer2008Dialect extends SQLServer2008Dialect {

	public ExtSQLServer2008Dialect(){
		super();
		registerHibernateType(Types.NVARCHAR, StandardBasicTypes.STRING.getName());
	}
}
