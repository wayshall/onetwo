package org.onetwo.common.fish.orm;

public class DefaultSQLBuilderFactory implements SQLBuilderFactory {


	@Override
	public EntrySQLBuilder createNamed(JFishMappedEntryMeta entry, String alias, SqlBuilderType type){
		return new EntrySQLBuilder(entry, alias, true, type);
	}
	
	@Override
	public EntrySQLBuilder createQMark(JFishMappedEntryMeta entry, String alias, SqlBuilderType type){
		return new EntrySQLBuilder(entry, alias, false, type);
	}
	@Override
	public TableSQLBuilder createNamed(String tableName, String alias, SqlBuilderType type){
		return new TableSQLBuilder(tableName, alias, true, type);
	}
	
	@Override
	public TableSQLBuilder createQMark(String tableName, String alias, SqlBuilderType type){
		return new TableSQLBuilder(tableName, alias, false, type);
	}

}
