package org.onetwo.common.excel;

import java.util.List;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;

import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter(ExcelUtils.JSON_FILTER_TEMPLATE)
public class TemplateModel implements PoiModel{
	private static final String DEFAULLT_VARNAME = "_sheet";

	private String name;
	/****
	 * 放到上下中的变量名
	 */
	private String varname;
	
	private String condition;
	
	private List<RowModel> rows;
	
//	private final Freezer freezer = new Freezer(TemplateModel.class.getSimpleName());
	
	//new
	private String label;
	private boolean multiSheet;
	private String datasource;
	private Integer sizePerSheet = 60000;
	
	private String columnWidth;
	
//	private Map<Integer, Short> columnWidthMap = LangUtils.newHashMap();
	
	
	public TemplateModel(){
	}
	
	public void initModel(){
		for(RowModel row : rows){
			row.setTemplate(this);
			row.initModel();
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.varname = name;
	}

	public List<RowModel> getRows() {
		return rows;
	}

	public void setRows(List<RowModel> rows) {
//		this.freezer.checkOperation("setRows");
		this.rows = rows;
	}
	
	public TemplateModel addRow(RowModel row){
		if(this.rows==null)
			this.rows = LangUtils.newArrayList();
		this.rows.add(row);
		return this;
	}

	public String getLabel() {
		if(StringUtils.isBlank(label))
			return getName();
		return label;
	}
	
	public String getVarName(){
		if(StringUtils.isBlank(varname))
			varname = name;
		if(StringUtils.isBlank(varname) || !LangUtils.isWord(varname))
			varname = DEFAULLT_VARNAME;
		return varname;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDatasource() {
		return datasource;
	}

	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}

	public Integer getSizePerSheet() {
		return sizePerSheet;
	}

	public void setSizePerSheet(Integer sizePerSheet) {
		this.sizePerSheet = sizePerSheet;
	}

	public boolean isMultiSheet() {
		return multiSheet;
	}

	public void setMultiSheet(boolean multiSheet) {
		this.multiSheet = multiSheet;
	}

	public String getColumnWidth() {
		return columnWidth;
	}

	public void setColumnWidth(String columnWidth) {
		this.columnWidth = columnWidth;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}
	
	
	/*public boolean isMultiSheet(){
		return this.sizePerSheet!=null && this.sizePerSheet>0 && StringUtils.isNotBlank(datasource);
	}*/

}
