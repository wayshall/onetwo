package org.onetwo.common.jfishdb.orm;


public interface SQLBuilderFactory {

	public static enum SqlBuilderType {
		insert,
		update,
		query,
		delete,
		primaryKey,
//		createTable,
//		dropTable,
		seq
	}
	
	public EntrySQLBuilder createNamed(JFishMappedEntryMeta entry, String alias, SqlBuilderType type);

	public EntrySQLBuilder createQMark(JFishMappedEntryMeta entry, String alias, SqlBuilderType type);
	
	public TableSQLBuilder createNamed(String table, String alias, SqlBuilderType type);

	public TableSQLBuilder createQMark(String table, String alias, SqlBuilderType type);

}