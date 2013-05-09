package org.onetwo.common.db.wheel;


public class DefaultSQLBuilderFactory implements SQLBuilderFactory {

	public SQLBuilder getSQLBuilder(TableInfo tableInfo){
		SQLBuilder sqlBuilder;
		sqlBuilder = createSQLBuilder(tableInfo.getName(), tableInfo.getAlias());
		return sqlBuilder;
	}
	
	protected SQLBuilder createSQLBuilder(String tableName, String alias){
		SQLBuilder sqlBuilder = SQLBuilder.Create(tableName, alias);
		return sqlBuilder;
	}

}
