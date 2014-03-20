package org.onetwo.common.excel;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.interfaces.excel.ExcelValueParser;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

public class DefaultRowProcessor implements RowProcessor {

	protected PoiExcelGenerator generator;
	private ConcurrentHashMap<String, CellStyle> styleCache = new ConcurrentHashMap<String, CellStyle>();
	
	private CellListener cellListener;
	
	public DefaultRowProcessor(PoiExcelGenerator excelGenerator){
		this.generator = excelGenerator;
//		this.cellListener = new CellListenerAdapter();
	}

	public PoiExcelGenerator getGenerator() {
		return generator;
	}

	public void setGenerator(PoiExcelGenerator generator) {
		this.generator = generator;
	}

	public CellListener getCellListener() {
		return cellListener;
	}

	public void setCellListener(CellListener cellListener) {
		this.cellListener = cellListener;
	}

	public void processRow(RowDataContext rowContext) {
		Sheet sheet = rowContext.getSheet();
		RowModel rowModel = rowContext.getRowModel();
		Row row = createRow(sheet, rowModel, null);
		if(rowModel.getFields()==null)
			return ;
		
//		Cell cell = null;
		for (FieldModel field : rowModel.getFields()) {
//			field.setParentRow(rowModel);
			//cell = createCell(sheet, row, field);
			rowContext.setCurrentRow(row);
			this.processField(getFieldRootValue(null, field), rowContext, field);
			rowContext.setCurrentRow(null);
		}
	}
	
	/******
	 * 待用
	 * @param iterator
	 * @param context
	 * @return
	 */
	protected FieldProcessor getFieldProcessor(RowModel iterator, Map<?, ?> context){
		FieldProcessor fieldProcessor = null;
		if(iterator.hasFieldProcessor()){
			Object fv = ExcelUtils.getValue(iterator.getFieldProcessor(), context, null);
			if(!(fv instanceof FieldProcessor)){
				throw new BaseException(iterator.getName()+" is not a FieldProcessor");
			}
			fieldProcessor = (FieldProcessor) fv;
		}
		return fieldProcessor;
	}
	
	protected Object getFieldRootValue(Object rowRoot, FieldModel field){
//		String name = "getFieldRootValue";//+field.getName();
//		UtilTimerStack.push(name);
		Object fieldRoot = rowRoot;
		if(StringUtils.isNotBlank(field.getRootValue())){
			fieldRoot = this.generator.getExcelValueParser().parseValue(field.getRootValue(), rowRoot, field);
		}
//		UtilTimerStack.pop(name);
		return fieldRoot;
	}
	
	public Object getDefaultFieldValue(FieldModel field){
		return "";
	}

	public Row createRow(Sheet sheet, RowModel rowModel, Object obj) {
//		int rowIndex = sheet.getLastRowNum();
		int rowIndex = sheet.getPhysicalNumberOfRows();
//		System.out.println("createRow:"+rowIndex);
		
		int span =0;
		if(rowModel.hasSpan()){
			span = this.generator.getExcelValueParser().parseIntValue(rowModel.getSpan(), obj);
		}else{
			span = rowModel.getSpace();
		}
		
		Row row = createRow(sheet, rowIndex, rowModel);
		if(span>0){
			for(int i=1; i<span; i++){
//				sheet.createRow(rowIndex++);
				createRow(sheet, ++rowIndex, rowModel);
			}
		}
		return row;
	}
	
	protected Row createRow(Sheet sheet, int rowIndex, RowModel rowModel){
		Row row = sheet.createRow(rowIndex);
		if(rowModel.getHeight()>0)
			row.setHeight(rowModel.getHeight());
		
		this.generator.getWorkbookData().getWorkbookListener().afterCreateRow(row, rowIndex);
		return row;
	}
	
	protected void doFieldValueExecutors(CellContext cellContext){
		FieldModel field = cellContext.getFieldModel();
		ExcelValueParser parser = cellContext.getParser();
		if(!field.getValueExecutors().isEmpty()){
			for(ExecutorModel model : field.getValueExecutors()){
				FieldValueExecutor executor = cellContext.getRowContext().getSheetData().getFieldValueExecutor(model);
				if(executor==null)
					throw new BaseException("not found executor: " + model.getExecutor());
				if(!executor.apply(cellContext, model))
					continue;
				Object preValue = parser.getContext().get(model.getName());
				preValue = executor.execute(cellContext, model, preValue);
				parser.getContext().put(model.getName(), preValue);
			}
		}
	}
	
//	protected Cell createCell(Sheet sheet, Row row, FieldModel field, int cellIndex, Object root){
	protected Cell createCell(CellContext cellContext){
		int cellIndex = cellContext.getCellIndex();
		Row row = cellContext.getCurrentRow();
		FieldModel field = cellContext.fieldModel;
		Sheet sheet = cellContext.getSheet();
		
		int rowNum = cellContext.rowCount;
		
		/*if(cellIndex < 0)
			cellIndex = row.getLastCellNum();*/
		
//		Cell cell = row.createCell(cellIndex);
		Cell cell = createCell(row, cellIndex);
//		System.out.println("cell width: " +sheet.getColumnWidth(cellIndex)+" w:" + sheet.getDefaultColumnWidth());
		
		CellStyle cstyle = this.buildCellStyle(cellContext);
		if(cstyle!=null){
			cell.setCellStyle(cstyle);
		}

//		this.generator.getWorkbookData().getWorkbookListener().afterCreateCell(cell, cellIndex);
		
		int colspan = cellContext.getColSpan();
		if(colspan>1){
			for(int i=1; i<colspan;i++){
				Cell colspanCell = createCell(row, ++cellIndex);
				if(cstyle!=null){
					colspanCell.setCellStyle(cstyle);
				}
//				row.createCell(++cellIndex);
			}
		}
		if(field.isRange()){
//			CellRangeAddress range = createCellRange(row, cell, field, root);
			CellRangeAddress range = new CellRangeAddress(rowNum, rowNum+cellContext.getRowSpan()-1, cell.getColumnIndex(), cell.getColumnIndex()+colspan-1);
			sheet.addMergedRegion(range);
		}
		return cell;
	}
	
	private Cell createCell(Row row, int cellIndex){
		Cell cell = row.createCell(cellIndex);
		this.generator.getWorkbookData().getWorkbookListener().afterCreateCell(cell, cellIndex);
		return cell;
	}
	
	public String getStyle(FieldModel field){
		return field.getStyle();
	}
	
	public String getFont(FieldModel field){
		return field.getFont();
	}

	protected CellStyle buildCellStyle(CellContext cellContext){
		FieldModel field = cellContext.getFieldModel();
		
		String styleString = getStyle(field);
		String fontString = getFont(field);
		if(StringUtils.isBlank(styleString) && StringUtils.isBlank(fontString)){
			return null;
		}
		
		if(StringUtils.isNotBlank(styleString))
			styleString = (String)cellContext.getParser().parseValue(styleString);
		
		if(StringUtils.isNotBlank(fontString))
			fontString = (String)cellContext.getParser().parseValue(fontString);
		
		String key = styleString + fontString;
		CellStyle cstyle = this.styleCache.get(key);
		if(cstyle!=null){
//			System.out.println("get style from cache");
			return cstyle;
		}
		
		cstyle = this.generator.getWorkbook().createCellStyle();
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(cstyle);
		bw.setAutoGrowNestedPaths(true);
		
		Map<String, String> styleMap = this.generator.getPropertyStringParser().parseStyle(styleString);
		try {
			for(Entry<String, String> entry : styleMap.entrySet()){
				if(isStaticField(CellStyle.class, entry.getValue())){
					Object styleValue = ReflectUtils.getStaticFieldValue(CellStyle.class, getStaticField(entry.getValue()));
					ReflectUtils.setProperty(cstyle, entry.getKey(), styleValue);
				}else{
					bw.setPropertyValue(entry.getKey(), entry.getValue());
				}
			}
		} catch (Exception e) {
			throw new BaseException("" + cellContext.getLocation()+" buildCellStyle error: " + e.getMessage(), e);
		}
		
		Font font = buildCellFont(cellContext, fontString);
		if(font!=null){
			cstyle.setFont(font);
		}
		
		this.styleCache.putIfAbsent(key, cstyle);
		
		return cstyle;
	}

	protected boolean isStaticField(Class<?> clazz, String value){
		if(value.startsWith("@")){
			return true;
		}
		return ReflectUtils.getIntro(clazz).containsField(value, true);
	}
	protected String getStaticField(String value){
		if(value.startsWith("@"))
			return value.substring(1);
		return value;
	}
	
	protected Font buildCellFont(CellContext cellContext, String fontString){
		if(StringUtils.isBlank(fontString))
			return null;
		
		Font font = this.generator.getWorkbook().createFont();
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(font);
		bw.setAutoGrowNestedPaths(true);

		Map<String, String> fontMap = this.generator.getPropertyStringParser().parseStyle(fontString);
		for(Entry<String, String> entry : fontMap.entrySet()){
			if(isStaticField(Font.class, entry.getValue())){
				Object styleValue = ReflectUtils.getStaticFieldValue(Font.class, getStaticField(entry.getValue()));
				ReflectUtils.setProperty(font, entry.getKey(), styleValue);
			}else{
				bw.setPropertyValue(entry.getKey(), entry.getValue());
			}
		}
		return font;
	}
	
	protected Object getFieldValue(Object root, FieldModel field, Object defValue){
		ExcelValueParser parser = this.generator.getExcelValueParser();
		Object fieldValue = null;
		if(StringUtils.isNotBlank(field.getVar())){
			fieldValue = parser.parseValue(field.getVar(), root, field);
		}
		if(fieldValue==null && StringUtils.isNotBlank(field.getDefaultValue())){
			fieldValue = parser.parseValue(field.getDefaultValue(), root, field);
		}
		if(fieldValue==null){
			fieldValue = defValue;
		}
		
		return fieldValue;
	}
	
	protected CellContext createCellContext(ExcelValueParser parser, Object objectValue, int objectValueIndex, RowDataContext rowContext, FieldModel field, int cellIndex){
		CellContext cellContext = new CellContext(parser, objectValue, objectValueIndex, rowContext, field, cellIndex, getDefaultFieldValue(field));
		rowContext.putCellContext(field.getName(), cellContext);
		return cellContext;
	}
	
	protected void processField(Object root, RowDataContext rowContext, FieldModel field){
		Row row = rowContext.getCurrentRow();
//		String pname = "processField";
//		UtilTimerStack.push(pname);
		int cellIndex = row.getLastCellNum();
		if(root==null){
			CellContext cellContext = createCellContext(this.generator.getExcelValueParser(), root, 0,  rowContext, field, cellIndex);
			this.processSingleField(cellContext);
		}else{
			List<Object> rootList = LangUtils.asList(root);

			int rowCount = row.getRowNum();
//			for(Object val : rootList){
			this.generator.getExcelValueParser().getContext().put("rootValue", rootList);
			try{
				for (int i = 0; i < rootList.size(); i++) {
					CellContext cellContext = createCellContext(this.generator.getExcelValueParser(), rootList.get(i), rowCount,  rowContext, field, cellIndex);
					this.processSingleField(cellContext);
					rowCount += cellContext.getRowSpanCount();
				}
			}finally{
				this.generator.getExcelValueParser().getContext().remove("rootValue");
			}
		}
//		UtilTimerStack.pop(pname);
	}
	
//	protected void processSingleField(Object root, Row row, FieldModel field, Object defValue, int cellIndex){
	protected void processSingleField(CellContext cellContext){
//		Row row = cellContext.row;
		Cell cell = createCell(cellContext);
		FieldModel field = cellContext.fieldModel;
		Object v = cellContext.getFieldValue();
		if(v==null)
			v = getFieldValue(cellContext.objectValue, field, cellContext.defFieldValue);

		/*for(FieldListener fl : field.getListeners()){
			v = fl.getCellValue(cell, v);
		}*/

		cellContext.setFieldValue(v);
		this.doFieldValueExecutors(cellContext);
		
//		v = formatValue(v, field.getDataFormat());
		setCellValue(field, cell, v);
		
		
		cellContext.addRowSpanCount(cellContext.getRowSpan());
	}
	

	protected Object formatValue(Object value, String dataFormat){
		Object actualValue;
		if(value instanceof Date){
			if(StringUtils.isBlank(dataFormat)){
				dataFormat = DateUtil.Date_Time;
				actualValue = DateUtil.format(dataFormat, (Date)value);
//				actualValue = value;
			}else{
				actualValue = DateUtil.format(dataFormat, (Date)value);
			}
		}else if(value instanceof Number && StringUtils.isNotBlank(dataFormat)) {
			NumberFormat nf = new DecimalFormat(dataFormat);
			nf.setRoundingMode(RoundingMode.HALF_UP);
			actualValue = nf.format(value);
		}else{
			actualValue = value;
		}
		return actualValue;
	}

	protected void processCellTypeByValue(FieldModel field, Cell cell, Object value){
		if(value==null){
			cell.setCellType(Cell.CELL_TYPE_BLANK);
			return ;
		}
		value = formatValue(value, field.getDataFormat());
		if(Number.class.isInstance(value)){
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellValue(((Number)value).doubleValue());
		}else if(String.class.isInstance(value)){
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(value.toString());
		}else if(Boolean.class.isInstance(value)){
			cell.setCellType(Cell.CELL_TYPE_BOOLEAN);
			cell.setCellValue((Boolean)value);
		}else if(Date.class.isInstance(value)){
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cell.setCellValue((Date)value);
		}else if(Calendar.class.isInstance(value)){
			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cell.setCellValue((Calendar)value);
		}else{
			HSSFRichTextString cellValue = new HSSFRichTextString(value.toString());
			cell.setCellValue(cellValue);
		}
	}
	protected void setCellValue(FieldModel field, Cell cell, Object value) {
		if(this.cellListener!=null)
			this.cellListener.beforeSetValue(cell, value);
//		ExcelUtils.setCellValue(cell, value);;
		this.processCellTypeByValue(field, cell, value);
	}
	
	/*protected int parseIntValue(String expr, Object root) {
		Object v = this.generator.getExcelValueParser().parseValue(expr, root, null);
		
		return (v == null ? 0 : Integer.valueOf(v.toString()));	
		
		return this.generator.getExcelValueParser().parseIntValue(expr, root);
	}*/
	
	public class CellContext {
		private final ExcelValueParser parser;
		private final Object objectValue;
		private final int rowCount;
		private final RowDataContext rowContext;
		private final Row row;
		private final FieldModel fieldModel;
		private final Object defFieldValue;
		private Object fieldValue;
		private final int cellIndex;

		private int rowSpanCount = 0;
//		private Object fieldValue;
		
		public CellContext(ExcelValueParser parser, Object objectValue, int objectValueIndex, RowDataContext rowContext, FieldModel field, int cellIndex, Object defValue) {
			super();
			this.rowContext = rowContext;
			this.row = rowContext.getCurrentRow();
			this.parser = parser;
			this.objectValue = objectValue;
			if(objectValueIndex<1){
				objectValueIndex = row.getRowNum();
			}
			this.rowCount = objectValueIndex;
			this.fieldModel = field;
			//row.getLastCellNum()
			this.cellIndex = cellIndex;
			this.defFieldValue = defValue;
		}

		public Row getCurrentRow(){
			Row currentRow = row.getSheet().getRow(rowCount);
			return currentRow;
		}
		
		/*public Row getCurrentRow(){
			if(rowCount==0)
				return row;
			int rowCount = rowCount + getRowSpan() -1;
			Row currentRow = row.getSheet().getRow(row.getRowNum()+rowCount);
			return currentRow;
		}*/
		
		public int getCellIndex(){
			/*int cellIndex = row.getSheet().getRow(row.getRowNum()+objectValueIndex).getLastCellNum();
			if(cellIndex==-1)
				cellIndex = 0;*/
			if(cellIndex==-1)
				return 0;
			return cellIndex;
		}
		
		public Sheet getSheet(){
			return row.getSheet();
		}
		
		public int getColSpan(){
			/*int colspan = 1;
			if(field.hasColspan()){
				colspan = parser.parseIntValue(field.getColspan(), objectValue);
			}
			if(colspan < 1) {
				colspan = 1;
			}*/
			return fieldModel.getColspanValue(this);
		}
		
		public int getRowSpan(){
			/*int rowspan = 1;
			if(field.hasRowspan()){
				rowspan = parser.parseIntValue(field.getRowspan(), objectValue);
			}
			if(rowspan < 1) {
				rowspan = 1;
			}*/
//			String name = field.getName()+"-rowspan";
//			UtilTimerStack.push(name);
			int rowspan = fieldModel.getRowpanValue(this);
//			UtilTimerStack.pop(name);
			return rowspan;
		}

		public int getRowCount() {
			return rowCount;
		}

		public void addRowSpanCount(int rowSpanCount) {
			this.rowSpanCount += rowSpanCount;
		}

		public int getRowSpanCount() {
			return rowSpanCount;
		}

		public Object getFieldValue() {
			return fieldValue;
		}

		public void setFieldValue(Object fieldValue) {
			this.fieldValue = fieldValue;
		}

		public FieldModel getFieldModel() {
			return fieldModel;
		}

		public ExcelValueParser getParser() {
			return parser;
		}

		public Object getObjectValue() {
			return objectValue;
		}

		public Row getRow() {
			return row;
		}

		public Object getDefFieldValue() {
			return defFieldValue;
		}

		public RowDataContext getRowContext() {
			return rowContext;
		}
		
		public String getLocation(){
			return rowContext.getLocation()+" -> " + fieldModel.getLabel();
		}
	}
}
