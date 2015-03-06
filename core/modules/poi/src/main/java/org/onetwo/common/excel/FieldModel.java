package org.onetwo.common.excel;

import java.util.Collections;
import java.util.List;

import org.onetwo.common.excel.data.CellContextData;
import org.onetwo.common.utils.StringUtils;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.google.common.collect.Lists;

@JsonFilter(ExcelUtils.JSON_FILTER_FIELD)
public class FieldModel implements PoiModel {

	private String label;
	private String name;
	private String value;
	private String dataFormat;
	private String condition;
	
	private boolean columnTotal;
	private boolean rowTotal;
	
//	private RowModel parentRow;
	
	private RowModel row;
	
	
	private String defaultValue;

	private String colspan;
	private Integer colspanValue;
	private Integer rowspanValue;
	private String rowspan;
	private String style;
	private String font;
	private String headerFont;
	private String headerStyle;
	
	private String rootValue;

	private String sumValueAs;
	private String sumValueField;
	private String sumValueCondition;
	
//	private List<FieldListener> listeners;
	private List<ExecutorModel> valueExecutors;
	
	private short columnIndex;
	private boolean autoSizeColumn;
	private boolean useMergedCells;
	
	public FieldModel() {
	}

	public void initModel(){
		if(StringUtils.isNotBlank(sumValueAs)){
			ExecutorModel model = new ExecutorModel();
			model.setName(sumValueAs);
			model.setExecutor(sumValueAs);
			model.setInstance(new SumFieldValueExecutor());
			model.addAttribute(SumFieldValueExecutor.SUM_VALUE_FIELD, sumValueField);
			model.addAttribute(SumFieldValueExecutor.SUM_VALUE_CONDITION, sumValueCondition);
			addExecutorModel(model);
		}
	}
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		/*if(StringUtils.isBlank(value)){
			value = getName();
		}*/
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String getVar(){
		String var = "";
//		if(StringUtils.isNotBlank(getValue())){
		if(getValue()!=null){
			var = getValue();
		}else{
			var = name;
		}
		return var;
	}

	public Boolean isColumnTotal() {
		return columnTotal;
	}

	public void setColumnTotal(Boolean columnTotal) {
		this.columnTotal = columnTotal;
	}

	public Boolean isRowTotal() {
		return rowTotal;
	}

	public void setRowTotal(Boolean rowTotal) {
		this.rowTotal = rowTotal;
	}

	public RowModel getParentRow() {
		return row;
	}

	/*public void setParentRow(RowModel parentRow) {
		this.parentRow = parentRow;
	}*/

	public RowModel getRow() {
		return row;
	}

	public void setRow(RowModel row) {
		this.row = row;
	}
	
	public boolean isRowField(){
		return this.getRow()!=null;
	}


	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getLabel() {
		if(StringUtils.isBlank(label))
			this.label = name==null?"":name;
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getColspan() {
		return colspan==null?"1":colspan;
	}

	
	public int getColspanValue(CellContextData context) {
		/*if(colspanValue!=null)
			return colspanValue;*/
		
//		int value = 1;
		if(StringUtils.isNotBlank(colspan)){
			if(ExcelUtils.IS_DIGIT.matcher(colspan).matches()){
				colspanValue = colspanValue!=null?colspanValue:Integer.parseInt(colspan);
			}else if(context!=null){
				int value = context.parseIntValue(colspan);
				return value<1?1:value;
			}else{
				colspanValue = 1;
			}
		}else{
			colspanValue = 1;
		}
		return colspanValue;
	}

	public void setColspan(String colspan) {
		this.colspan = colspan;
	}


	public int getRowpanValue(CellContextData context) {
		/*if(rowspanValue!=null)
			return rowspanValue;*/
		
//		int value = 1;
		if(StringUtils.isNotBlank(rowspan)){
			/*if(IS_DIGIT.matcher(rowspan).matches())
				rowspanValue = Integer.parseInt(rowspan);
			else */
			if(ExcelUtils.IS_DIGIT.matcher(rowspan).matches()){
				rowspanValue = rowspanValue!=null?rowspanValue:Integer.parseInt(rowspan);
			}else if(context!=null){
				int value = context.parseIntValue(rowspan);
				return value<1?1:value;
			}else{
				rowspanValue = 1;
			}
		}else{
			rowspanValue = 1;
		}
		return rowspanValue;
	}
	
	public String getRowspan() {
		return rowspan == null ? "1" : rowspan;
	}
	
	public boolean hasRowspan(){
		return rowspan!=null;
	}

	public void setRowspan(String rowspan) {
		this.rowspan = rowspan;
	}

	public boolean isRange(){
		//性能关键点。。。。。。。
//		boolean rs = this.rowspan != null   || this.colspan != null;
		return (rowspanValue!=null && rowspanValue>1) || (colspanValue!=null && colspanValue>1);
	}

	public String getStyle() {
		return StringUtils.defaultValues(style, getParentRow()==null?"":getParentRow().getFieldStyle(), "");
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getFont() {
		return StringUtils.defaultValues(font, getParentRow()==null?"":getParentRow().getFieldFont(), "");
	}

	public void setFont(String font) {
		this.font = font;
	}

	public String getHeaderFont() {
		return StringUtils.defaultValues(headerFont, getParentRow()==null?"":getParentRow().getFieldHeaderFont(), "");
	}

	public void setHeaderFont(String headerFont) {
		this.headerFont = headerFont;
	}

	public String getHeaderStyle() {
		return StringUtils.defaultValues(headerStyle, getParentRow()==null?"":getParentRow().getFieldHeaderStyle(), "");
	}

	public void setHeaderStyle(String headerStyle) {
		this.headerStyle = headerStyle;
	}

	public String getRootValue() {
		return rootValue;
	}

	public void setRootValue(String rootValue) {
		this.rootValue = rootValue;
	}

	public String getDataFormat() {
		return dataFormat;
	}

	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}

	public List<ExecutorModel> getValueExecutors() {
		return valueExecutors==null?Collections.EMPTY_LIST:valueExecutors;
	}
	
	public void addExecutorModel(ExecutorModel model){
		if(this.valueExecutors==null)
			this.valueExecutors = Lists.newArrayList();
		this.valueExecutors.add(model);
	}

	public void setValueExecutors(List<ExecutorModel> valueExecutors) {
		this.valueExecutors = valueExecutors;
	}

	public String getSumValueAs() {
		return sumValueAs;
	}

	public void setSumValueAs(String sumValueAs) {
		this.sumValueAs = sumValueAs;
	}

	public String getSumValueField() {
		return sumValueField;
	}

	public void setSumValueField(String sumValueField) {
		this.sumValueField = sumValueField;
	}

	public String getSumValueCondition() {
		return sumValueCondition;
	}

	public void setSumValueCondition(String sumValueCondition) {
		this.sumValueCondition = sumValueCondition;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public short getColumnIndex() {
		return columnIndex;
	}

	public void setColumnIndex(short columnIndex) {
		this.columnIndex = columnIndex;
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

	
}
