package org.onetwo.common.db.wheel;


public interface SQLBuilderFactory {

	public SQLBuilder getSQLBuilder(TableInfo tableInfo);

}
