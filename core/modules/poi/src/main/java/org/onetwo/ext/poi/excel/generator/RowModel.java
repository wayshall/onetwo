package org.onetwo.ext.poi.excel.generator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

public class RowModel implements PoiModel {
	public static final String DEFAULT_NAME = "entity";
//	public static class Type {
//		public static final String TITLE_KEY = "title";
//		public static final String HEADER_KEY = "header";
//		public static final String ROW_KEY = "row";
//		public static final String ITERATOR_KEY = "iterator";
//	}
	
	private String name;
//	private TemplateRowTypes rowType;
	private String type;
	private String datasource;
	private int space;
	private String span;
	private short height;
	private String index;
	private String condition;
	
	private boolean renderHeader;

	private String fieldFont;
	private String fieldStyle;
	/****
	 * @see {@link org.apache.poi.ss.usermodel.Font Font}
	 */
	private String fieldHeaderFont;
	/***
	 * @see {@link org.apache.poi.ss.usermodel.CellStyle CellStyle}
	 */
	private String fieldHeaderStyle;
	
	private String fieldProcessor;
	
	private TemplateModel template;
	
//	private String executorClass;
	
	private List<FieldModel> fields = new ArrayList<FieldModel>();
	
	public RowModel(){
	}

	public void initModel(){
		short columnIndex = 0;
		for(FieldModel field : fields){
			field.initModel();
			field.setRow(this);
			field.setColumnIndex(columnIndex);
			
			if(field.isAutoSizeColumn()){
				template.setAutoSizeColumn(columnIndex, field.isUseMergedCells());
			}
			columnIndex++;
		}
	}
	
	public int size(){
		return this.fields.size();
	}
	
	public boolean isEmpty(){
		return fields.isEmpty();
	}
	
	public FieldModel getField(int index){
		return this.fields.get(index);
	}
	
	public boolean isTitle(){
		return TemplateRowTypes.TITLE.equals(getRowType());
	}
	
	public boolean isRow(){
		return TemplateRowTypes.ROW.equals(getRowType());
	}
	
	public boolean isIterator(){
		return TemplateRowTypes.ITERATOR.equals(getRowType());
	}

	public String getName() {
		if(name == null) {
			return DEFAULT_NAME;
		}
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

//	public String getType() {
//		String  type = this.type;
//		if (StringUtils.isBlank(type)) {
//			type = this.rowType.lowerName();
//		}
//		return type;
//	}
//
//	public void setType(String type) {
//		this.type = type;
//		this.rowType = TemplateRowTypes.of(type);
//	}

	public TemplateRowTypes getRowType() {
		return TemplateRowTypes.of(getType());
	}

	public void setRowType(TemplateRowTypes type) {
		this.type = type.lowerName();
	}
	
	public String getType() {
		String type = this.type;
		if (StringUtils.isBlank(type)) {
			type = TemplateRowTypes.ROW.lowerName();
		}
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDatasource() {
		return datasource;
	}

	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}

	public List<FieldModel> getFields() {
		return fields;
	}

	public void setFields(List<FieldModel> fields) {
		this.fields = fields;
	}
	
	public RowModel addField(FieldModel field){
		if(fields==null)
			fields = Lists.newArrayList();
		field.setRow(this);
		fields.add(field);
		return this;
	}

	public int getSpace() {
		return space;
	}

	public void setSpace(int space) {
		this.space = space;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public boolean isRenderHeader() {
		return renderHeader;
	}

	public void setRenderHeader(boolean renderHeader) {
		this.renderHeader = renderHeader;
	}

	public String getSpan() {
		if(span==null)
			return String.valueOf(getSpace());
		return span;
	}
	
	public boolean hasSpan(){
		return span!=null;
	}

	public void setSpan(String span) {
		this.span = span;
	}

	public RowModel copy(){
		RowModel clone = new RowModel();
		clone.setDatasource(datasource);
		clone.setFields(fields);
		clone.setIndex(index);
		clone.setIndex(index);
		clone.setRenderHeader(renderHeader);
		clone.setSpace(space);
		clone.setSpan(span);
		clone.type = type;
		return clone;
	}

	public String getFieldFont() {
		return fieldFont;
	}

	public void setFieldFont(String fieldFont) {
		this.fieldFont = fieldFont;
	}

	public String getFieldStyle() {
		return fieldStyle;
	}

	public void setFieldStyle(String fieldStyle) {
		this.fieldStyle = fieldStyle;
	}

	public String getFieldHeaderFont() {
		return fieldHeaderFont;
	}

	public void setFieldHeaderFont(String fieldHeaderFont) {
		this.fieldHeaderFont = fieldHeaderFont;
	}

	public String getFieldHeaderStyle() {
		return fieldHeaderStyle;
	}

	public void setFieldHeaderStyle(String fieldHeaderStyle) {
		this.fieldHeaderStyle = fieldHeaderStyle;
	}

	public String getFieldProcessor() {
		return fieldProcessor;
	}

	public void setFieldProcessor(String fieldProcessor) {
		this.fieldProcessor = fieldProcessor;
	}
	
	public boolean hasFieldProcessor(){
		return StringUtils.isNotBlank(fieldProcessor);
	}

	public short getHeight() {
		return height;
	}

	public void setHeight(short height) {
		this.height = height;
	}

	public TemplateModel getTemplate() {
		return template;
	}

	public void setTemplate(TemplateModel template) {
		this.template = template;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}
	
	
	/*public RowExecutor getExecutorInstance() {
		if(StringUtils.isBlank(executorClass))
			return null;
		
		RowExecutor executor = ReflectUtils.newInstance(executorClass);
		return executor;
	}*/

}
