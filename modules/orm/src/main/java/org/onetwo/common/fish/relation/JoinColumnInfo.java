package org.onetwo.common.fish.relation;

import org.onetwo.common.fish.orm.ColumnInfo;
import org.onetwo.common.fish.orm.TableInfo;

public class JoinColumnInfo extends ColumnInfo {

	private String joinTableName;
	private String referencedColumnName;
//	private JFishMappedFieldType joinType;
	
	
	public JoinColumnInfo(TableInfo tableInfo, String joinTable, String name, String referencedColumnName) {
		super(tableInfo, name);
		this.joinTableName = joinTable;
		this.referencedColumnName = referencedColumnName;
	}
	
	
	public String getReferencedColumnName() {
		return referencedColumnName;
	}
	public String getJoinTableName() {
		return joinTableName;
	}
	public void setJoinTableName(String joinTableName) {
		this.joinTableName = joinTableName;
	}
	public void setReferencedColumnName(String referencedColumnName) {
		this.referencedColumnName = referencedColumnName;
	}
	public boolean isPrimaryKey() {
		return false;
	}

}
