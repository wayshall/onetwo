package org.onetwo.common.excel;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.interfaces.excel.ExcelValueParser;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;

public class DefaultRowProcessor implements RowProcessor {

	protected PoiExcelGenerator generator;
	private ConcurrentHashMap<String, CellStyle> styleCache = new ConcurrentHashMap<String, CellStyle>();
	
	public DefaultRowProcessor(PoiExcelGenerator excelGenerator){
		this.generator = excelGenerator;
	}

	public PoiExcelGenerator getGenerator() {
		return generator;
	}

	public void setGenerator(PoiExcelGenerator generator) {
		this.generator = generator;
	}

	public void processRow(RowDataContext rowContext) {
		Sheet sheet = rowContext.getSheet();
		RowModel rowModel = rowContext.getRowModel();
		Row row = createRow(sheet, rowModel, null);
		if(rowModel.getFields()==null)
			return ;
		
//		Cell cell = null;
		for (FieldModel field : rowModel.getFields()) {
			field.setParentRow(rowModel);
			//cell = createCell(sheet, row, field);
			this.processField(getFieldRootValue(null, field), row, field, getDefaultFieldValue(field));
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
			span = parseIntValue(rowModel.getSpan(), obj);
		}else{
			span = rowModel.getSpace();
		}
		
		Row row = createRow(sheet, rowIndex++, rowModel);
		if(span>0){
			for(int i=1; i<span; i++){
//				sheet.createRow(rowIndex++);
				createRow(sheet, rowIndex++, rowModel);
			}
		}
		return row;
	}
	
	protected Row createRow(Sheet sheet, int rowIndex, RowModel rowModel){
		Row row = sheet.createRow(rowIndex++);
		if(rowModel.getHeight()>0)
			row.setHeight(rowModel.getHeight());
		return row;
	}
	
//	protected Cell createCell(Sheet sheet, Row row, FieldModel field, int cellIndex, Object root){
	protected Cell createCell(CellContext cellContext){
		int cellIndex = cellContext.getCellIndex();
		Row row = cellContext.getCurrentRow();
		FieldModel field = cellContext.field;
		Sheet sheet = cellContext.getSheet();
		
		int rowNum = cellContext.rowCount;
		
		/*if(cellIndex < 0)
			cellIndex = row.getLastCellNum();*/
		
		if(cellIndex==-1)
			cellIndex = 0;
		
		Cell cell = row.createCell(cellIndex++);
//		System.out.println("cell width: " +sheet.getColumnWidth(cellIndex)+" w:" + sheet.getDefaultColumnWidth());
		
		int colspan = cellContext.getColSpan();
		
		if(colspan>1){
			for(int i=1; i<colspan;i++)
				row.createCell(cellIndex++);
		}
		if(field.isRange()){
//			CellRangeAddress range = createCellRange(row, cell, field, root);
			CellRangeAddress range = new CellRangeAddress(rowNum, rowNum+cellContext.getRowSpan()-1, cell.getColumnIndex(), cell.getColumnIndex()+colspan-1);
			sheet.addMergedRegion(range);
		}


		CellStyle cstyle = this.buildCellStyle(field);
		if(cstyle!=null){
			cell.setCellStyle(cstyle);
		}

		
		for(FieldListener fl : field.getListeners()){
			fl.afterCreateCell(this.generator.getWorkbook(), sheet, row, cell);
		}
		return cell;
	}
	
	public String getStyle(FieldModel field){
		return field.getStyle();
	}
	
	public String getFont(FieldModel field){
		return field.getFont();
	}

	protected CellStyle buildCellStyle(FieldModel field){
		String styleString = getStyle(field);
		String fontString = getFont(field);
		if(StringUtils.isBlank(styleString) && StringUtils.isBlank(fontString)){
			return null;
		}
		
		String key = styleString + fontString;
		CellStyle cstyle = this.styleCache.get(key);
		if(cstyle!=null){
//			System.out.println("get style from cache");
			return cstyle;
		}
		
		cstyle = this.generator.getWorkbook().createCellStyle();
		/*if(StringUtils.isNotBlank(styleString)){
			String[] attrs = StringUtils.split(styleString, ";");
			for(String attr : attrs){
				String[] av = StringUtils.split(attr.trim(), ":");
				if(av!=null && av.length==2){
					try {
						Object styleValue = ReflectUtils.getStaticFieldValue(CellStyle.class, av[1].trim().toUpperCase());
						ReflectUtils.setProperty(cstyle, av[0], styleValue);
					} catch (Exception e) {
						throw new ServiceException("set ["+StringUtils.join(av, ":")+"] style error", e);
					}
				}
			}
		}*/
		
		Map<String, String> styleMap = this.generator.getPropertyStringParser().parseStyle(styleString);
		for(Entry<String, String> entry : styleMap.entrySet()){
			Object styleValue = ReflectUtils.getStaticFieldValue(CellStyle.class, entry.getValue().trim().toUpperCase());
			ReflectUtils.setProperty(cstyle, entry.getKey(), styleValue);
		}
		
		Font font = buildCellFont(field, fontString);
		if(font!=null){
			cstyle.setFont(font);
		}
		
		this.styleCache.putIfAbsent(key, cstyle);
		
		return cstyle;
	}
	
	protected Font buildCellFont(FieldModel field, String fontString){
		if(StringUtils.isBlank(fontString))
			return null;
		
		Font font = this.generator.getWorkbook().createFont();
		/*String[] attrs = StringUtils.split(fontString, ";");
		for(String attr : attrs){
			String[] av = StringUtils.split(attr.trim(), ":");
			if(av!=null && av.length==2){
				try {
					Object styleValue = ReflectUtils.getStaticFieldValue(Font.class, av[1].trim().toUpperCase());
					ReflectUtils.setProperty(font, av[0], styleValue);
				} catch (Exception e) {
					throw new ServiceException("set ["+StringUtils.join(av, ":")+"] font error", e);
				}
			}
		}*/

		Map<String, String> fontMap = this.generator.getPropertyStringParser().parseStyle(fontString);
		for(Entry<String, String> entry : fontMap.entrySet()){
			Object styleValue = ReflectUtils.getStaticFieldValue(Font.class, entry.getValue().trim().toUpperCase());
			ReflectUtils.setProperty(font, entry.getKey(), styleValue);
		}
		return font;
	}
	
	
	/*protected CellRangeAddress createCellRange(Row row, Cell cell, FieldModel field, Object root){
		int rowspan = parseIntValue(field.getRowspan(), root);
		int colspan = parseIntValue(field.getColspan(), root);
		
		if(rowspan < 1) {
			rowspan = 1;
		}
		
		if(colspan < 1) {
			colspan = 1;
		}
		
		CellRangeAddress range = new CellRangeAddress(row.getRowNum(), row.getRowNum()+rowspan-1, cell.getColumnIndex(), cell.getColumnIndex()+colspan-1);
		return range;
	}*/
	
	protected Object processSymbol(FieldModel field, String value){
		if(value==null || !value.startsWith(":"))
			return null;
		String aValue = value.substring(1);
		Object v = ReflectUtils.getProperty(field, aValue, false);
		return v;
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
	
	
	protected void processField(Object root, Row row, FieldModel field, Object defValue){
//		String pname = "processField";
//		UtilTimerStack.push(pname);
		int cellIndex = row.getLastCellNum();
		if(root==null){
			CellContext cellContext = new CellContext(this.generator.getExcelValueParser(), null, 0, row, field, cellIndex, defValue);
			this.processSingleField(cellContext);
		}else{
			List<Object> rootList = LangUtils.asList(root);
			this.generator.getExcelValueParser().getContext().put("rootValue", rootList);

			int rowCount = row.getRowNum();
//			for(Object val : rootList){
			for (int i = 0; i < rootList.size(); i++) {
				CellContext cellContext = new CellContext(this.generator.getExcelValueParser(), rootList.get(i), rowCount, row, field, cellIndex, defValue);
				this.processSingleField(cellContext);
				rowCount += cellContext.getRowSpanCount();
			}
		}
//		UtilTimerStack.pop(pname);
	}
	
//	protected void processSingleField(Object root, Row row, FieldModel field, Object defValue, int cellIndex){
	protected void processSingleField(CellContext cellContext){
//		Row row = cellContext.row;
		Cell cell = createCell(cellContext);
		FieldModel field = cellContext.field;
		Object v = getFieldValue(cellContext.objectValue, field, cellContext.defFieldValue);

		for(FieldListener fl : field.getListeners()){
			v = fl.getCellValue(cell, v);
		}
		
		setCellValue(cell, v);

		cellContext.addRowSpanCount(cellContext.getRowSpan());
	}

	protected void setCellValue(Cell cell, Object value) {
		ExcelUtils.setCellValue(cell, value);
	}
	
	protected int parseIntValue(String expr, Object root) {
		/*Object v = this.generator.getExcelValueParser().parseValue(expr, root, null);
		
		return (v == null ? 0 : Integer.valueOf(v.toString()));	*/
		
		return this.generator.getExcelValueParser().parseIntValue(expr, root);
	}
	
	public class CellContext {
		public final ExcelValueParser parser;
		public final Object objectValue;
		public final int rowCount;
		public final Row row;
		public final FieldModel field;
		public final Object defFieldValue;
		private final int cellIndex;

		private int rowSpanCount = 0;
//		private Object fieldValue;
		
		public CellContext(ExcelValueParser parser, Object objectValue, int objectValueIndex, Row row, FieldModel field, int cellIndex, Object defValue) {
			super();
			this.parser = parser;
			this.objectValue = objectValue;
			if(objectValueIndex<1){
				objectValueIndex = row.getRowNum();
			}
			this.rowCount = objectValueIndex;
			this.row = row;
			this.field = field;
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
			return field.getColspanValue(this);
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
			int rowspan = field.getRowpanValue(this);
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
		
	}
}
