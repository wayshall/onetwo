package org.onetwo.common.excel;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.interfaces.excel.ExcelValueParser;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.profiling.UtilTimerStack;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;

public class POIExcelGeneratorImpl implements PoiExcelGenerator {
	
	private Logger logger = MyLoggerFactory.getLogger(this.getClass());

	protected TemplateModel tempalte;
	// protected Object root;
//	protected Map context;
	protected HSSFWorkbook workbook;
//	protected HSSFSheet sheet;
//	protected int rowIndex;
//	protected int cellIndex;

	private ExcelValueParser excelValueParser;
	
	private Map<String, RowProcessor> rowProcessors;
	
	public POIExcelGeneratorImpl() {
		this.init();
	}
	
	protected void init(){
		this.workbook = new HSSFWorkbook();
		
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

	public HSSFWorkbook getWorkbook() {
		return workbook;
	}

	public POIExcelGeneratorImpl(TemplateModel template){
		this(template, null);
	}
	@SuppressWarnings("rawtypes")
	public POIExcelGeneratorImpl(TemplateModel template, Map context) {
		this.init();
		this.tempalte = template;
		if(context!=null){
			this.excelValueParser = new DefaultExcelValueParser(context);
		}
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
		HSSFSheet sheet = workbook.createSheet(sheetname);
		SheetData sdata = new SheetData(sheet, datalist);
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
	
	/* (non-Javadoc)
	 * @see com.project.base.excel.ExcelGenerator#write2File(java.io.OutputStream)
	 */
	public void write(OutputStream out) {
		try {
			this.workbook.write(out);
		} catch (Exception e) {
			throw new ServiceException(e);
		} finally {
		}
	}

	/* (non-Javadoc)
	 * @see com.project.base.excel.ExcelGenerator#write2File(java.lang.String)
	 */
	public void write(String path) {
		FileOutputStream file = null;
		try {
			file = new FileOutputStream(path);
			this.workbook.write(file);
		} catch (Exception e) {
			throw new ServiceException(e);
		} finally {
			LangUtils.closeIO(file);
		}
	}

	public static void main(String[] args) {
	}

}
