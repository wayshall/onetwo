package org.onetwo.common.excel;

import java.util.Collections;
import java.util.List;

import org.onetwo.common.excel.DefaultRowProcessor.CellContext;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.StringUtils;

import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter(ExcelUtils.JSON_FILTER_FIELD)
public class FieldModel {

	private String label;
	private String name;
	private String value;
	private String dataFormat;
	
	private boolean columnTotal;
	private boolean rowTotal;
	
	private RowModel parentRow;
	
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
	
//	private List<FieldListener> listeners;
	private List<ExecutorModel> fieldValueExecutors;
	
	public FieldModel() {
	}

	public void initModel(WorkbookData workbookData){
		if(fieldValueExecutors==null){
			fieldValueExecutors = Collections.EMPTY_LIST;
		}else{
			for(ExecutorModel exe : fieldValueExecutors){
				FieldValueExecutor exeInst = (FieldValueExecutor)workbookData.getExcelValueParser().parseValue(exe.getExecutor(), null, null);
				if(exeInst==null)
					throw new BaseException("no FieldValueExecutor found: " + exe.getExecutor());
				exe.setFieldValueExecutor(exeInst);
			}
		}
	}
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		if(StringUtils.isBlank(value)){
			value = getName();
		}
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String getVar(){
		String var = "";
		if(StringUtils.isNotBlank(value)){
			var = value;
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
		return parentRow;
	}

	public void setParentRow(RowModel parentRow) {
		this.parentRow = parentRow;
	}

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

	
	public int getColspanValue(CellContext context) {
		/*if(colspanValue!=null)
			return colspanValue;*/
		
		int value = 1;
		if(StringUtils.isNotBlank(colspan)){
			/*if(IS_DIGIT.matcher(colspan).matches())
				colspanValue = Integer.parseInt(colspan);
			else */
			if(context!=null)
				value = context.parser.parseIntValue(colspan, context.objectValue);
			else
				colspanValue = 1;
		}else{
			colspanValue = 1;
		}
		if(value < 1) {
			value = 1;
		}
		return value;
	}

	public void setColspan(String colspan) {
		this.colspan = colspan;
	}


	public int getRowpanValue(CellContext context) {
		/*if(rowspanValue!=null)
			return rowspanValue;*/
		
		int value = 1;
		if(StringUtils.isNotBlank(rowspan)){
			/*if(IS_DIGIT.matcher(rowspan).matches())
				rowspanValue = Integer.parseInt(rowspan);
			else */
			if(context!=null)
				value = context.parser.parseIntValue(rowspan, context.objectValue);
			else
				rowspanValue = 1;
		}else{
			rowspanValue = 1;
		}
		if(value < 1) {
			value = 1;
		}
		return value;
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
		return this.rowspan != null || this.colspan != null;
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

	public List<ExecutorModel> getFieldValueExecutors() {
		return fieldValueExecutors;
	}

	public void setFieldValueExecutors(List<ExecutorModel> fieldValueExecutors) {
		this.fieldValueExecutors = fieldValueExecutors;
	}

	
}
