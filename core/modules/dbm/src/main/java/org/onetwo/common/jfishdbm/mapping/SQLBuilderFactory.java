package org.onetwo.common.jfishdbm.mapping;


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

	public EntrySQLBuilderImpl createQMark(JFishMappedEntryMeta entry, String alias, SqlBuilderType type);
	
//	@Deprecated
//	public TableSQLBuilder createNamed(String table, String alias, SqlBuilderType type);

//	@Deprecated
//	public TableSQLBuilder createQMark(String table, String alias, SqlBuilderType type);

}