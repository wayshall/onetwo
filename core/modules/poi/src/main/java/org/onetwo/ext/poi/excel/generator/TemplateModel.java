package org.onetwo.ext.poi.excel.generator;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.ext.poi.utils.ExcelUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

//@JsonFilter(ExcelUtils.JSON_FILTER_TEMPLATE)
public class TemplateModel implements PoiModel{
	public static final int DEFAULT_SIZE_PER_SHEET = 60000;
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
	private Integer sizePerSheet;
	
	private String columnWidth;
	

	private boolean autoSizeColumn;
	private boolean useMergedCells;
	private Map<Short, Boolean> autoSizeColumnMap;
	
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
	
	public boolean isEmpty(){
		return ExcelUtils.isEmpty(rows);
	}
	
	public TemplateModel addRow(RowModel row){
		if(this.rows==null)
			this.rows = Lists.newArrayList();
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
		if(StringUtils.isBlank(varname) || !ExcelUtils.isWord(varname))
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
		return sizePerSheet==null?DEFAULT_SIZE_PER_SHEET:sizePerSheet;
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
	
	public void setAutoSizeColumn(short col, boolean useMerged){
		if(autoSizeColumnMap==null){
			autoSizeColumnMap = Maps.newHashMap();
		}
		this.autoSizeColumnMap.put(col, useMerged);
	}

	public Map<Short, Boolean> getAutoSizeColumnMap() {
		return autoSizeColumnMap;
	}

	public boolean isAutoSizeColumn() {
		return autoSizeColumn;
	}

	public void setAutoSizeColumn(boolean autoSizeColumn) {
		this.autoSizeColumn = autoSizeColumn;
	}

	public boolean isUseMergedCells() {
		return useMergedCells;
	}

	public void setUseMergedCells(boolean useMergedCells) {
		this.useMergedCells = useMergedCells;
	}
	
	/*public boolean isMultiSheet(){
		return this.sizePerSheet!=null && this.sizePerSheet>0 && StringUtils.isNotBlank(datasource);
	}*/

}
