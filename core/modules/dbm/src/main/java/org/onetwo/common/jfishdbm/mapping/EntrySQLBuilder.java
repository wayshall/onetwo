package org.onetwo.common.jfishdbm.mapping;

import org.onetwo.common.jfishdbm.mapping.SQLBuilderFactory.SqlBuilderType;

public interface EntrySQLBuilder {

	public String build();

	public String getSql();

	public SqlBuilderType getType();
	
	public JFishMappedEntryMeta getEntry();
	
	public Object getVersionValue(Object[] updateValues);

}