package org.onetwo.common.web.view.jsp.grid;

import java.util.ArrayList;
import java.util.List;

import org.onetwo.common.web.view.HtmlElement;

public class RowTagBean extends HtmlElement{

	public static enum RowType {
		header,
		row,
		iterator
	}
	
	
	public RowTagBean(RowType type) {
		super();
		this.type = type;
	}
	List<FieldTagBean> fields = new ArrayList<FieldTagBean>();
	RowType type;
	boolean renderHeader;

	public void addField(FieldTagBean field){
		fields.add(field);
	}
	public List<FieldTagBean> getFields() {
		return fields;
	}
	public FieldTagBean getField(String name) {
		for(FieldTagBean df : fields){
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
