package org.onetwo.dbm.mapping;

import org.onetwo.dbm.mapping.SQLBuilderFactory.SqlBuilderType;

public interface EntrySQLBuilder {

	public String build();

	public String getSql();

	public SqlBuilderType getType();
	
	public JFishMappedEntryMeta getEntry();
	
	public Object getVersionValue(Object[] updateValues);

}