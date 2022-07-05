package org.onetwo.ext.poi.excel.generator;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.onetwo.common.convert.Types;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.ext.poi.excel.data.CellContextData;
import org.onetwo.ext.poi.excel.data.RowContextData;
import org.onetwo.ext.poi.excel.exception.ExcelException;
import org.onetwo.ext.poi.utils.ExcelUtils;
import org.onetwo.ext.poi.utils.TheFunction;

/**
 * @author weishao zeng
 * <br/>
 */
public class FieldProccessor {
	private CellListener cellListener;
	private DefaultCellStyleBuilder cellStyleBuilder;
	protected PoiExcelGenerator generator;
	
	public FieldProccessor(PoiExcelGenerator generator) {
		super();
		this.generator = generator;
		this.cellStyleBuilder = new DefaultCellStyleBuilder(generator);
	}


	public FieldProccessor(PoiExcelGenerator generator, DefaultCellStyleBuilder cellStyleBuilder) {
		super();
		this.cellStyleBuilder = cellStyleBuilder;
		this.generator = generator;
	}

	public void processField(Object root, RowContextData rowContext, FieldModel field) {
		if(!canCreateCell(rowContext, field))
			return ;
		
		Row row = rowContext.getCurrentRow();
//		String pname = "processField";
//		UtilTimerStack.push(pname);
		int cellIndex = row.getLastCellNum();
		if(root==null){
//			String name = "no root processField";
//			UtilTimerStack.push(name);
			CellContextData cellContext = createCellContext(rowContext.getCurrentRowObject(), 0,  rowContext, field, cellIndex);
			this.processSingleField(cellContext);
//			UtilTimerStack.pop(name);
		}else{
//			String name = "root processField";
//			UtilTimerStack.push(name);
			List<Object> rootList = ExcelUtils.tolist(root);

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
//			UtilTimerStack.pop(name);
		}
//		UtilTimerStack.pop(pname);
		
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
	
	protected void processSingleField(CellContextData cellContext){
//		Row row = cellContext.row;
		
//		UtilTimerStackObject ts = UtilTimerStackObject.createObject();
		

//		ts.push("createCell");
		Cell cell = createCell(cellContext);
		FieldModel field = cellContext.getFieldModel();
		Object v = cellContext.getFieldValue();
		if(v==null) {
			v = getFieldValue(cellContext);
		}
//		ts.pop("createCell");

		/*for(FieldListener fl : field.getListeners()){
			v = fl.getCellValue(cell, v);
		}*/

		cellContext.setFieldValue(v);
		
//		ts.push("doFieldValueExecutors");
		this.doFieldValueExecutors(cellContext);
//		ts.pop("doFieldValueExecutors");
		

//		ts.push("setCellValue");
//		v = formatValue(v, field.getDataFormat());
		setCellValue(field, cell, v);
		
		cellContext.addRowSpanCount(cellContext.getRowSpan());
//		ts.pop("setCellValue");
	}
	
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
		//性能关键点。。。。。。。
//		if(field.isRange()){
		int rowSpan = cellContext.getRowSpan();
		if(rowSpan>1 || colspan>1){
//			CellRangeAddress range = createCellRange(row, cell, field, root);
			CellRangeAddress range = new CellRangeAddress(rowNum, rowNum+rowSpan-1, cell.getColumnIndex(), cell.getColumnIndex()+colspan-1);
			sheet.addMergedRegion(range);
		}
		/*int cellType = getCellType(field.getDataType());
		cell.setCellType(cellType);*/
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
	
	protected Object formatValue(Object value, String dataFormat){
		Object actualValue;
		if(value instanceof Date){
			if(StringUtils.isBlank(dataFormat)){
				dataFormat = TheFunction.DATE_TIME;
				actualValue = TheFunction.getInstance().formatDateByPattern(dataFormat, (Date)value);
//				actualValue = value;
			}else{
				actualValue = TheFunction.getInstance().formatDateByPattern(dataFormat, (Date)value);
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
//			cell.setCellType(Cell.CELL_TYPE_BLANK);
			cell.setBlank();
			return ;
		}
		
		/*String df = field.getDataFormat();
		if(StringUtils.isNotBlank(df)){
			cell.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat(df));
		}*/
//		formatValue(value, df);
		/*if(Number.class.isInstance(value)){
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
		}*/
		if (StringUtils.isNotBlank(field.getDataType())) {
			Object convertedValue = Types.convertValue(value, ReflectUtils.loadClass(field.getDataType()));
			ExcelUtils.setCellValue(cell, convertedValue);
		} else  {
			ExcelUtils.setCellValue(cell, value);
		}
	}
	
	protected void setCellValue(FieldModel field, Cell cell, Object value) {
		if(this.cellListener!=null)
			this.cellListener.beforeSetValue(cell, value);
//		ExcelUtils.setCellValue(cell, value);;
		this.processCellTypeByValue(field, cell, value);
	}

	
	protected void doFieldValueExecutors(CellContextData cellContext){
		FieldModel field = cellContext.getFieldModel();
//		ExcelValueParser parser = cellContext.getParser();
		Map<String, Object> sheetContext = cellContext.getWorkbookData().getSheetContext();
		if(!field.getValueExecutors().isEmpty()){
			for(ExecutorModel model : field.getValueExecutors()){
				FieldValueExecutor executor = cellContext.getRowContext().getSheetData().getFieldValueExecutor(model);
				if(executor==null)
					throw new ExcelException("not found executor: " + model.getExecutor());
				if(!executor.apply(cellContext, model))
					continue;
				Object preValue = sheetContext.get(model.getName());
				preValue = executor.execute(cellContext, model, preValue);
				sheetContext.put(model.getName(), preValue);
			}
		}
	}

	
	public Object getDefaultFieldValue(FieldModel field){
		return "";
	}

	public void setCellListener(CellListener cellListener) {
		this.cellListener = cellListener;
	}


	public CellListener getCellListener() {
		return cellListener;
	}
	
}
