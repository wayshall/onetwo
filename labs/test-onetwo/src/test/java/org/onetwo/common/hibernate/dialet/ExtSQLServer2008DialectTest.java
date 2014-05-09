package org.onetwo.common.hibernate.dialet;

import java.sql.Types;

import org.junit.Test;

public class ExtSQLServer2008DialectTest {
	
	private ExtSQLServer2008Dialect dialet = new ExtSQLServer2008Dialect();
	
	@Test
	public void test(){
		String typeName = dialet.getTypeName(Types.VARCHAR);
		System.out.println("typeName: " + typeName);
		
		typeName = dialet.getHibernateTypeName(Types.VARCHAR);
		System.out.println("typeName: " + typeName);
		
		typeName = dialet.getCastTypeName(Types.VARCHAR);
		System.out.println("typeName: " + typeName);
	}

}
