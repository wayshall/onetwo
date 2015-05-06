package org.onetwo.common.jfishdb.orm;

import org.onetwo.common.jfishdb.dialet.DBDialect;
import org.onetwo.common.jfishdb.dialet.AbstractDBDialect.DBMeta;

public interface InnerDBDialet extends DBDialect {

	public void setDbmeta(DBMeta dbmeta);
//	public void setPrintSql(boolean printSql); 
	
}
