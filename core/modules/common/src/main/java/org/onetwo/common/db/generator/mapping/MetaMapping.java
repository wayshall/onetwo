package org.onetwo.common.db.generator.mapping;

public interface MetaMapping extends SqlTypeMapping {
	public ColumnMapping getColumnMapping(int sqlType);
	public ColumnMapping getRequiredColumnMapping(int sqlType);
}
