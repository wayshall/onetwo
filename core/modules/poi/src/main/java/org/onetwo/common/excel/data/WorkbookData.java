package org.onetwo.common.excel.data;

import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.onetwo.common.excel.DefaultExcelValueParser;
import org.onetwo.common.excel.EmptyWorkbookListener;
import org.onetwo.common.excel.VarModel;
import org.onetwo.common.excel.WorkbookListener;
import org.onetwo.common.excel.WorkbookModel;
import org.onetwo.common.excel.exception.ExcelException;
import org.onetwo.common.excel.utils.ExcelUtils;

import com.google.common.collect.Maps;

public class WorkbookData extends AbstractExcelContextData {

	public final static WorkbookListener EMPTY_WORKBOOK_LISTENER = new EmptyWorkbookListener();
	private final Workbook workbook;
	private final ExcelValueParser excelValueParser;
	private final WorkbookListener workbookListener;
	private final WorkbookModel workbookModel;
	

	private Map<String, Object> sheetContext = Maps.newHashMap();
	private Map<String, Object> rowContext = Maps.newHashMap();
//	private Map<String, Object> cellContext = Maps.newHashMap();
	
//	private Map<ExecutorModel, FieldValueExecutor> fieldValueExecutors;

	public WorkbookData(WorkbookModel workbookModel, Workbook workbook, WorkbookListener workbookListener) {
		this(workbookModel, workbook, new DefaultExcelValueParser(null), workbookListener);
	}

	public WorkbookData(WorkbookModel workbookModel, Workbook workbook, Map<String, Object> context) {
		this(workbookModel, workbook, new DefaultExcelValueParser(context), EMPTY_WORKBOOK_LISTENER);
	}
	
	public WorkbookData(WorkbookModel workbookModel, Workbook workbook, ExcelValueParser excelValueParser) {
		this(workbookModel, workbook, excelValueParser, EMPTY_WORKBOOK_LISTENER);
	}

	public WorkbookData(WorkbookModel workbookModel, Workbook workbook, ExcelValueParser excelValueParser, WorkbookListener workbookListener) {
		this.workbook = workbook;
		this.excelValueParser = excelValueParser;
		this.workbookListener = workbookListener;
		this.workbookModel = workbookModel;
	}

	public void initData(){
		Map<String, Object> context = this.excelValueParser.getContext();
		for(VarModel var : workbookModel.getVars()){
			if(context.containsKey(var.getName()))
				throw new ExcelException("var is exist: " + var.getName());
			String expr = var.getValue();
			Object value = var.getValue();
			if(ExcelUtils.isExpr(expr)){
				expr = ExcelUtils.getExpr(expr);
				value = parseValue(expr);
			}
			excelValueParser.putVar(var.getName(), value);
		}
		
	}
	
	protected AbstractExcelContextData getParentContextData(){
		return null;
	}

	@Override
	public Map<String, Object> getSelfContext() {
		return this.getWorkbookContext();
	}

	final public Map<String, Object> getWorkbookContext() {
		return excelValueParser.getContext();
	}

	final public Map<String, Object> getSheetContext() {
		return sheetContext;
	}

	final public Map<String, Object> getRowContext() {
		return rowContext;
	}

	/*final public Map<String, Object> getCellContext() {
		return cellContext;
	}*/
	
	public Workbook getWorkbook() {
		return workbook;
	}

	public ExcelValueParser getExcelValueParser() {
		return excelValueParser;
	}

	public WorkbookListener getWorkbookListener() {
		return workbookListener;
	}

	public WorkbookModel getWorkbookModel() {
		return workbookModel;
	}

	/*public FieldValueExecutor getFieldValueExecutor(ExecutorModel model){
		return this.fieldValueExecutors.get(model);
	}
	
	public void addFieldValueExecutor(ExecutorModel model, FieldValueExecutor executor){
		if(fieldValueExecutors==null)
			fieldValueExecutors = Maps.newHashMap();
		fieldValueExecutors.put(model, executor);
	}*/

}
