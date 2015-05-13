package org.onetwo.common.jfishdbm.dialet;

import org.onetwo.common.jfishdbm.dialet.AbstractDBDialect.DBMeta;


public interface InnerDBDialet extends DBDialect {

	public void setDbmeta(DBMeta dbmeta);
//	public void setPrintSql(boolean printSql); 
	
}
