package org.onetwo.common.excel;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.onetwo.common.excel.data.CellContextData;
import org.onetwo.common.excel.data.RowContextData;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;

public class DefaultRowProcessor implements RowProcessor {

	protected PoiExcelGenerator generator;
	private DefaultCellStyleBuilder cellStyleBuilder;
//	private ConcurrentHashMap<String, CellStyle> styleCache = new ConcurrentHashMap<String, CellStyle>();
	
	private CellListener cellListener;
	
	public DefaultRowProcessor(PoiExcelGenerator excelGenerator){
		this.generator = excelGenerator;
//		this.cellListener = new CellListenerAdapter();
		this.cellStyleBuilder = new DefaultCellStyleBuilder(generator);
	}
	
	public DefaultRowProcessor(PoiExcelGenerator excelGenerator, DefaultCellStyleBuilder cellStyleBuilder){
		this.generator = excelGenerator;
//		this.cellListener = new CellListenerAdapter();
		this.cellStyleBuilder = cellStyleBuilder;
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

	public void processRow(RowContextData rowContext) {
//		Sheet sheet = rowContext.getSheet();
		RowModel rowModel = rowContext.getRowModel();
		Row row = createRow(rowContext);
		if(rowModel.getFields()==null)
			return ;
		
//		Cell cell = null;
		for (FieldModel field : rowModel.getFields()) {
//			field.setParentRow(rowModel);
			//cell = createCell(sheet, row, field);
			rowContext.setCurrentRow(row);
//			this.processField(getFieldRootValue(null, field), rowContext, field);
			this.processField(getFieldRootValue(rowContext, field), rowContext, field);
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
	
	protected Object getFieldRootValue(RowContextData rowContext, FieldModel field){
//		String name = "getFieldRootValue";//+field.getName();
//		UtilTimerStack.push(name);
		if(StringUtils.isNotBlank(field.getRootValue())){
			return rowContext.parseValue(field.getRootValue());
		}
		return null;
	}
	
	public Object getDefaultFieldValue(FieldModel field){
		return "";
	}

	public Row createRow(RowContextData rowContext) {
		Sheet sheet = rowContext.getSheet();
		RowModel rowModel = rowContext.getRowModel();
//		int rowIndex = sheet.getLastRowNum();
		int rowIndex = sheet.getPhysicalNumberOfRows();
//		System.out.println("createRow:"+rowIndex);
		
		int span =0;
		if(rowModel.hasSpan()){
			span = rowContext.parseIntValue(rowModel.getSpan());
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
	
	protected void doFieldValueExecutors(CellContextData cellContext){
		FieldModel field = cellContext.getFieldModel();
//		ExcelValueParser parser = cellContext.getParser();
		Map<String, Object> sheetContext = cellContext.getWorkbookData().getSheetContext();
		if(!field.getValueExecutors().isEmpty()){
			for(ExecutorModel model : field.getValueExecutors()){
				FieldValueExecutor executor = cellContext.getRowContext().getSheetData().getFieldValueExecutor(model);
				if(executor==null)
					throw new BaseException("not found executor: " + model.getExecutor());
				if(!executor.apply(cellContext, model))
					continue;
				Object preValue = sheetContext.get(model.getName());
				preValue = executor.execute(cellContext, model, preValue);
				sheetContext.put(model.getName(), preValue);
			}
		}
	}
	
//	protected Cell createCell(Sheet sheet, Row row, FieldModel field, int cellIndex, Object root){
	protected Cell createCell(CellContextData cellContext){
		int cellIndex = cellContext.getCellIndex();
		Row row = cellContext.getCurrentRow();
		FieldModel field = cellContext.getFieldModel();
		Sheet sheet = cellContext.getSheet();
		
		int rowNum = cellContext.getRowCount();
		
		/*if(cellIndex < 0)
			cellIndex = row.getLastCellNum();*/
		
//		Cell cell = row.createCell(cellIndex);
		Cell cell = createCell(row, cellIndex);
//		System.out.println("cell width: " +sheet.getColumnWidth(cellIndex)+" w:" + sheet.getDefaultColumnWidth());
		
		CellStyle cstyle = cellStyleBuilder.buildCellStyle(cellContext);
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
	

	/***
	 * 获取字段值
	 * @param cellData
	 * @return
	 */
//	protected Object getFieldValue(Object root, FieldModel field, Object defValue)
	protected Object getFieldValue(CellContextData cellData){
		FieldModel field = cellData.getFieldModel();
		
		Object fieldValue = null;
		if(StringUtils.isNotBlank(field.getVar())){
			fieldValue = cellData.parseValue(field.getVar());
		}else if(field.getValue()!=null){
			fieldValue = field.getValue();
		}else if(StringUtils.isNotBlank(field.getDefaultValue())){
			fieldValue = cellData.parseValue(field.getDefaultValue());
		}
		if(fieldValue==null){
			fieldValue = cellData.getDefFieldValue();
		}

		fieldValue = formatValue(fieldValue, field.getDataFormat());
		
		return fieldValue;
	}
	
	protected CellContextData createCellContext(Object objectValue, int objectValueIndex, RowContextData rowContext, FieldModel field, int cellIndex){
		CellContextData cellContext = new CellContextData(objectValue, objectValueIndex, rowContext, field, cellIndex, getDefaultFieldValue(field));
		cellContext.initData();
		rowContext.putChildCellContextData(field.getName(), cellContext);
		return cellContext;
	}
	
	/***
	 * 是否创建cell
	 * @param rowContext
	 * @param field
	 * @return
	 */
	private boolean canCreateCell(RowContextData rowContext, FieldModel field){
		if(StringUtils.isNotBlank(field.getCondition())){
			return (Boolean)rowContext.parseValue(field.getCondition());
		}
		return true;
	}
	
	protected void processField(Object root, RowContextData rowContext, FieldModel field){
		if(!canCreateCell(rowContext, field))
			return ;
		
		Row row = rowContext.getCurrentRow();
//		String pname = "processField";
//		UtilTimerStack.push(pname);
		int cellIndex = row.getLastCellNum();
		if(root==null){
			CellContextData cellContext = createCellContext(rowContext.getCurrentRowObject(), 0,  rowContext, field, cellIndex);
			this.processSingleField(cellContext);
		}else{
			List<Object> rootList = LangUtils.asList(root);

			int rowCount = row.getRowNum();
//			for(Object val : rootList){
			rowContext.getSelfContext().put("rootValue", rootList);
			try{
				for (int i = 0; i < rootList.size(); i++) {
					CellContextData cellContext = createCellContext(rootList.get(i), rowCount,  rowContext, field, cellIndex);
					this.processSingleField(cellContext);
					rowCount += cellContext.getRowSpanCount();
				}
			}finally{
				rowContext.getSelfContext().remove("rootValue");
			}
		}
//		UtilTimerStack.pop(pname);
	}
	
//	protected void processSingleField(Object root, Row row, FieldModel field, Object defValue, int cellIndex){
	protected void processSingleField(CellContextData cellContext){
//		Row row = cellContext.row;
		Cell cell = createCell(cellContext);
		FieldModel field = cellContext.getFieldModel();
		Object v = cellContext.getFieldValue();
		if(v==null)
			v = getFieldValue(cellContext);

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
//			actualValue = nf.format(value);
			actualValue = Double.parseDouble(nf.format(value));
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
		String df = field.getDataFormat();
		if(StringUtils.isNotBlank(df)){
			cell.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat(df));
		}
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
}
