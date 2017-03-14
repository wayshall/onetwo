package org.onetwo.dbm.mapping;


public interface SQLBuilderFactory {

	public static enum SqlBuilderType {
		insert,
		update,
		query,
		delete,
		primaryKey,
//		createTable,
//		dropTable,
		seq,
		createSeq
	}
	
	public EntrySQLBuilder createNamed(DbmMappedEntryMeta entry, String alias, SqlBuilderType type);

	public EntrySQLBuilderImpl createQMark(DbmMappedEntryMeta entry, String alias, SqlBuilderType type);
	
//	@Deprecated
//	public TableSQLBuilder createNamed(String table, String alias, SqlBuilderType type);

//	@Deprecated
//	public TableSQLBuilder createQMark(String table, String alias, SqlBuilderType type);

}