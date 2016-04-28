package org.onetwo.common.excel.etemplate;

import org.apache.poi.ss.usermodel.Row;

public class AbstractDirectiveModel {

	final private String directiveStart;
	private Row startRow;
	private Row endRow;
	private String directiveEnd;
	
	public AbstractDirectiveModel(String directive) {
		super();
		this.directiveStart = directive;
	}
	
	public String getDirectiveStart() {
		return directiveStart;
	}

	public Row getEndRow() {
		return endRow;
	}
	public void setEndRow(Row endRow) {
		this.endRow = endRow;
	}
	public Row getStartRow() {
		return startRow;
	}
	public void setStartRow(Row startRow) {
		this.startRow = startRow;
	}

	public String getDirectiveEnd() {
		return directiveEnd;
	}

	public void setDirectiveEnd(String directiveEnd) {
		this.directiveEnd = directiveEnd;
	}
	
}
