package org.onetwo.ext.poi.excel.etemplate;

import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.onetwo.ext.poi.excel.etemplate.directive.ForeachRowDirectiveModel.ForeachRowInfo;

import com.google.common.collect.Lists;

public class AbstractDirectiveModel {

	final private String directiveStart;
	private Row startRow;
	private Row endRow;
	private String directiveEnd;
	private List<ForeachRowInfo> matchRows = Lists.newArrayList();
	
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
	public List<ForeachRowInfo> getMatchRows() {
		return matchRows;
	}
	public void addMatchRow(Row row){
		this.matchRows.add(new ForeachRowInfo(row));
	}
	
}
