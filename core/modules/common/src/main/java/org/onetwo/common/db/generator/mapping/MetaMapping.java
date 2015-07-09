package org.onetwo.common.db.generator.mapping;

import java.util.Collection;

public interface MetaMapping extends SqlTypeMapping {
	public Collection<ColumnMapping> getColumnMappings();
	public ColumnMapping getColumnMapping(int sqlType);
	public ColumnMapping getRequiredColumnMapping(int sqlType);
}
