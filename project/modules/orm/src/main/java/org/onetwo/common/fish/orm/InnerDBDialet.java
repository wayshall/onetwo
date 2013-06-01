package org.onetwo.common.fish.orm;

import org.onetwo.common.fish.orm.AbstractDBDialect.DBMeta;

public interface InnerDBDialet extends DBDialect {

	public void setDbmeta(DBMeta dbmeta);
	public void setPrintSql(boolean printSql); 
	
}
