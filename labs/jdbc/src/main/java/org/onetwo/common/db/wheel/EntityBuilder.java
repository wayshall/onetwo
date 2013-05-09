package org.onetwo.common.db.wheel;

public interface EntityBuilder {

	public String makeDynamicInsertSQLBy(Object object);

	public String makeStaticInsertSQL();

	public String makeDynamicUpdateSQLBy(Object object);

	public String makeStaticUpdateSQL();

	public String makeStaticDeleteSQL();

	public String makeDynamicDeleteSQL(Object object);

	public String makeStaticQuerySQL();

	public String makeDynamicQuerySQL(Object object);

	public String makeStaticFetchPKSQL();

	public Class<?> getEntityClass();

	public TableInfo getTableInfo();

//	public PrimaryKey getPrimaryKeyInfo();
	
	public SQLBuilder createSQLBuilder();
	public void setSQLBuilderFactory(SQLBuilderFactory sqlBuilder);

}