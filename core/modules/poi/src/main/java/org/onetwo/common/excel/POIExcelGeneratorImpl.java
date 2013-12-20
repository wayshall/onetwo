package org.onetwo.common.excel;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.onetwo.common.interfaces.excel.ExcelValueParser;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.profiling.UtilTimerStack;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;

public class POIExcelGeneratorImpl extends AbstractWorkbookExcelGenerator implements PoiExcelGenerator{
	
	private Logger logger = MyLoggerFactory.getLogger(this.getClass());

	protected TemplateModel tempalte;
	// protected Object root;
//	protected Map context;
	protected Workbook workbook;
//	protected HSSFSheet sheet;
//	protected int rowIndex;
//	protected int cellIndex;

	private ExcelValueParser excelValueParser;
	
	private Map<String, RowProcessor> rowProcessors;
	
	private PropertyStringParser propertyStringParser = ExcelUtils.DEFAULT_PROPERTY_STRING_PARSER;
	
	public POIExcelGeneratorImpl() {
		this.workbook = new HSSFWorkbook();
		this.init();
	}

	public POIExcelGeneratorImpl(TemplateModel template){
		this(template, null);
	}
	public POIExcelGeneratorImpl(TemplateModel template, Map<String, Object> context) {
		this(new HSSFWorkbook(), template, context);
	}
	public POIExcelGeneratorImpl(Workbook workbook, TemplateModel template, Map<String, Object> context) {
		this.workbook = workbook;
		this.tempalte = template;
		if(context!=null){
			this.excelValueParser = new DefaultExcelValueParser(context);
		}
		this.init();
	}
	
	final protected void init(){
		RowProcessor titleProcessor;
		RowProcessor iteratorProcessor;
		RowProcessor rowProcessor;
		
		rowProcessor = new DefaultRowProcessor(this);
		titleProcessor = new DefaultRowProcessor(this){
			@Override
			public Object getDefaultFieldValue(FieldModel field) {
				return field.getLabel();
			}

			@Override
			public String getFont(FieldModel field) {
				return field.getHeaderFont();
			}

			@Override
			public String getStyle(FieldModel field) {
				return field.getHeaderStyle();
			}
			
			
		};
		iteratorProcessor = new SmartIteratorRowProcessor(this, titleProcessor);
//		iteratorProcessor = new ExtIteratorRowProcessor(this, titleProcessor);
		
		rowProcessors = new HashMap<String, RowProcessor>();
		rowProcessors.put(RowModel.Type.ROW_KEY, rowProcessor);
		rowProcessors.put(RowModel.Type.HEADER_KEY, titleProcessor);
		rowProcessors.put(RowModel.Type.TITLE_KEY, titleProcessor);
		rowProcessors.put(RowModel.Type.ITERATOR_KEY, iteratorProcessor);
	}

	public ExcelValueParser getExcelValueParser() {
		return excelValueParser;
	}

	public void setExcelValueParser(ExcelValueParser excelValueParser) {
		this.excelValueParser = excelValueParser;
	}

	public Workbook getWorkbook() {
		return workbook;
	}


	@Override
	public void generateIt() {
		ExportDatasource ds = null;
		if(StringUtils.isBlank(tempalte.getDatasource())){
			ds = new ListExportDatasource(tempalte, Collections.EMPTY_LIST);
		}else{
			Object dsObj = getExcelValueParser().parseValue(tempalte.getDatasource(), null, null);
			if(dsObj instanceof ExportDatasource){
				ds = (ExportDatasource) dsObj;
			}else{
				List<?> datalist = LangUtils.asList(dsObj);
				ds = new ListExportDatasource(tempalte, datalist);
			}
		}
		
		
		if(tempalte.isMultiSheet()){
			List<?> datalist = null;
			int i = 0;
			while(LangUtils.isNotEmpty( (datalist = ds.getSheetDataList(i)) )){
				logger.info("{} sheet, data size: {}", i, datalist.size());
				this.generateSheet(ds.getSheetLabel(i), datalist);
				i++;
			}
		}else{
			this.generateSheet(ds.getSheetLabel(0), ds.getSheetDataList(0));
		}
		
	}
	
	private void generateSheet(String sheetname, List<?> datalist){
		Sheet sheet = workbook.createSheet(sheetname);
		int sheetIndex = workbook.getSheetIndex(sheet);
		
		Map<Integer, Short> columnMap = this.propertyStringParser.parseColumnwidth(this.tempalte.getColumnWidth());
		if(LangUtils.isNotEmpty(columnMap)){
			for(Entry<Integer, Short> entry : columnMap.entrySet()){
				sheet.setColumnWidth(entry.getKey(), entry.getValue()*256);
			}
		}
		
		SheetData sdata = new SheetData(workbook, sheet, datalist);
		this.excelValueParser.getContext().put(tempalte.getVarName(), sdata);
		try{
			this.generateSheet(sdata);
		}finally{
			this.excelValueParser.getContext().remove(tempalte.getVarName());
		}
		
	}
	
	private void generateSheet(SheetData sheetData) {
		Assert.notNull(this.tempalte);
		final List<RowModel> rows = this.tempalte.getRows();
		Assert.notEmpty(rows);
//		sheet = workbook.createSheet(tempalte.getName());
		//for (RowModel row : this.tempalte.getRows()) {
//		int columns = 0;
		boolean printTime = UtilTimerStack.isActive();
		String rowname = "";
		for (int i = 0; i < rows.size(); i++) {
			RowModel row = rows.get(i);

			if(printTime){
				rowname = row.getType()+"-"+row.getName()+i;
				UtilTimerStack.push(rowname);
			}
//			Object dataSourceValue = getExcelValueParser().parseValue(row.getDatasource(), null, null);A
			RowDataContext datacontext = new RowDataContext(sheetData, row);
			this.rowProcessors.get(row.getType()).processRow(datacontext);
			if(printTime){
				UtilTimerStack.pop(rowname);
			}
			
			/*if (i == rows.size() - 1) {
				List<FieldModel> models = row.getFields();
				for (FieldModel fieldModel : models) {
					int cols = fieldModel.getColspanValue(null);
					columns += cols;
				}
				
			}*/
		}
		
		//cost a big time
		/*for (int j = 0; j < columns; j++) {
			sheet.autoSizeColumn(j);
		}*/
		
		//System.out.println("generate excel success!");
	}
	

	public PropertyStringParser getPropertyStringParser() {
		return propertyStringParser;
	}

	public static void main(String[] args) {
	}

}
