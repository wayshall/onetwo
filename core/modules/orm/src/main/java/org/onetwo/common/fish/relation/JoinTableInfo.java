package org.onetwo.common.fish.relation;

import java.io.Serializable;

public class JoinTableInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4466595096950277769L;
	private String table;
	private JoinColumnInfo joinColum;//主实体端
	private JoinColumnInfo inverseJoinColumn;//关联实体端

	// private JFishMappedFieldType joinType;

	public JoinTableInfo(String table) {
		this.table = table;
	}

	public JoinTableInfo(String table, JoinColumnInfo joinColum, JoinColumnInfo inverseJoinColumn) {
		super();
		this.table = table;
		this.joinColum = joinColum;
		this.inverseJoinColumn = inverseJoinColumn;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public JoinColumnInfo getJoinColum() {
		return joinColum;
	}

	public void setJoinColum(JoinColumnInfo joinColum) {
		this.joinColum = joinColum;
	}

	public JoinColumnInfo getInverseJoinColumn() {
		return inverseJoinColumn;
	}

	public void setInverseJoinColumn(JoinColumnInfo inverseJoinColumn) {
		this.inverseJoinColumn = inverseJoinColumn;
	}

}
