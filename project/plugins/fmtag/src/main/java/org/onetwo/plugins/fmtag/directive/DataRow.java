package org.onetwo.plugins.fmtag.directive;

import java.util.ArrayList;
import java.util.List;

import org.onetwo.common.ftl.directive.HtmlElement;

public class DataRow extends HtmlElement{

	public static enum RowType {
		header,
		row,
		iterator
	}
	
	
	public DataRow(RowType type) {
		super();
		this.type = type;
	}
	List<DataField> fields = new ArrayList<DataField>();
	RowType type;
	boolean renderHeader;

	public void addField(DataField field){
		fields.add(field);
	}
	public List<DataField> getFields() {
		return fields;
	}
	public DataField getField(String name) {
		for(DataField df : fields){
			if(df.getName().equals(name))
				return df;
		}
		return null;
	}
	
	public boolean isIterator(){
		return RowType.iterator==type;
	}
	
	public boolean isRow(){
		return RowType.row==type;
	}
	
	public boolean isHeader(){
		return RowType.header==type;
	}
	public boolean isRenderHeader() {
		return renderHeader;
	}
	public boolean isFieldEmpty(){
		return this.fields.isEmpty();
	}
}
