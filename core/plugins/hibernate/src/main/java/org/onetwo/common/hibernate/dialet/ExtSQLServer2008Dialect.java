package org.onetwo.common.hibernate.dialet;

import java.sql.Types;

import org.hibernate.dialect.SQLServer2008Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

public class ExtSQLServer2008Dialect extends SQLServer2008Dialect {

	public ExtSQLServer2008Dialect(){
		super();
		registerHibernateType(Types.NVARCHAR, StandardBasicTypes.STRING.getName());
		registerHibernateType(Types.NCHAR, StandardBasicTypes.STRING.getName());
		registerFunction("substring", new SQLFunctionTemplate(StandardBasicTypes.STRING, "SUBSTRING(?1, ?2, ?3)"));
	}
}
