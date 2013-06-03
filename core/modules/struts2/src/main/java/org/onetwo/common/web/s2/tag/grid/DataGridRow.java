package org.onetwo.common.web.s2.tag.grid;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.views.annotations.StrutsTag;
import org.onetwo.common.exception.WebException;

import com.opensymphony.xwork2.util.ValueStack;

@SuppressWarnings("serial")
@StrutsTag(name = "column", tldTagClass = "com.project.base.tag.StrutsColumnTag", description = "Render HTML Column tag.", allowDynamicAttributes = true)
public class DataGridRow extends AbstractDataGridComponent {
	
	public static enum Type {
		header,
		row,
		iterator
	}

	protected DataGrid dataGrid;
	protected Type type = Type.row;
	
	protected boolean createdByGrid;
	
	protected boolean renderHeader = true;
	
	protected List<DataGridField> fields = new ArrayList<DataGridField>();
	
	public DataGridRow(ValueStack stack, HttpServletRequest request, HttpServletResponse response, boolean push) {
		this(stack, request, response);
		if(!push)
			getComponentStack().pop();
	}
	
	public DataGridRow(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
		super(stack, request, response);
		this.dataGrid = (DataGrid)this.findAncestor(DataGrid.class);
		if (dataGrid == null)
			throw new WebException("column标签必须包含在row标签内！");
	}

	@Override
	protected void evaluateExtraParams() {
		super.evaluateExtraParams();
		this.defaultUITheme = dataGrid.getTheme();
	}
	
	@Override
	public boolean start(Writer writer) {
        evaluateParams();
        return true;
	}
	
	public boolean end(Writer writer, String body) {
		try {
			this.dataGrid.addRow(this);
			this.bodyContent = body;
		} finally{
			this.popComponentStack();
		}
		return false;
	}
	
	public boolean isFieldEmpty(){
		return this.fields.isEmpty();
	}
	
	public void addField(DataGridField field){
		this.fields.add(field);
		this.colspan += field.colspan;
	}
	
	public boolean usesBody(){
		return true;
	}
	

    @Override
	protected String getDefaultTemplate() {
		return null;
	}

	public DataGrid getDataGrid() {
		return dataGrid;
	}
	
	public boolean isHeader(){
		return Type.header.equals(type);
	}
	
	public boolean isRow(){
		return Type.row.equals(type);
	}
	
	public boolean isIterator(){
		return Type.iterator.equals(type);
	}

	public Type getType() {
		return type;
	}

	public void setTypeString(String type) {
		if(StringUtils.isBlank(type))
			return ;
		this.type = Type.valueOf(type);
	}

	public void setType(Type type) {
		this.type = type;
	}

	public List<DataGridField> getFields() {
		return fields;
	}

	public boolean isCreatedByGrid() {
		return createdByGrid;
	}

	public void setCreatedByGrid(boolean createdByGrid) {
		this.createdByGrid = createdByGrid;
	}

	public boolean isRenderHeader() {
		return renderHeader;
	}

	public void setRenderHeader(boolean renderHeader) {
		this.renderHeader = renderHeader;
	}

}
