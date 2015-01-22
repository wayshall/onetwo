package org.onetwo.common.web.view.jsp.grid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.ajaxanywhere.AAUtils;
import org.onetwo.common.web.utils.WebHolder;
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
	
	private GridTagBean gridTagBean;
//	protected List<FieldTagBean> fields = new ArrayList<FieldTagBean>();
	private Map<String, FieldTagBean> fields = new LinkedHashMap<String, FieldTagBean>();
	private RowType type;
	private boolean renderHeader;

	public void addField(FieldTagBean field){
		field.setRowTagBean(this);
		fields.put(field.getName(), field);
	}
	public List<FieldTagBean> getFields() {
		return new ArrayList<FieldTagBean>(fields.values());
	}
	public FieldTagBean getField(String name) {
		for(FieldTagBean df : fields.values()){
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
		if(AAUtils.isAjaxRequest(WebHolder.getRequest()))
			return false;
		return renderHeader;
	}
	public boolean isFieldEmpty(){
		return this.fields.isEmpty();
	}
	public GridTagBean getGridTagBean() {
		return gridTagBean;
	}
	public void setGridTagBean(GridTagBean gridTagBean) {
		this.gridTagBean = gridTagBean;
	}
	public void setRenderHeader(boolean renderHeader) {
		this.renderHeader = renderHeader;
	}
	public RowType getType() {
		return type;
	}
	
	public boolean containsField(String name){
		return this.fields.containsKey(name);
	}
}
