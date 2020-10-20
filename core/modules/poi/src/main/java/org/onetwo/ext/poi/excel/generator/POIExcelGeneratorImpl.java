package org.onetwo.ext.poi.excel.generator;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.onetwo.ext.poi.excel.data.ExcelValueParser;
import org.onetwo.ext.poi.excel.data.RowContextData;
import org.onetwo.ext.poi.excel.data.SheetData;
import org.onetwo.ext.poi.excel.data.WorkbookData;
import org.onetwo.ext.poi.utils.ExcelPage;
import org.onetwo.ext.poi.utils.ExcelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/****
 * 单sheet生成器
 * @author weishao
 *
 */
public class POIExcelGeneratorImpl extends AbstractWorkbookExcelGenerator implements PoiExcelGenerator{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	protected TemplateModel tempalte;
	// protected Object root;
//	protected Map context;
//	protected Workbook workbook;
	protected WorkbookData workbookData;
//	protected HSSFSheet sheet;
//	protected int rowIndex;
//	protected int cellIndex;

//	private ExcelValueParser excelValueParser;
	
	private Map<String, RowProcessor> rowProcessors;
	
	private PropertyStringParser propertyStringParser = ExcelUtils.DEFAULT_PROPERTY_STRING_PARSER;
	
	public POIExcelGeneratorImpl() {
		this.workbookData = new WorkbookData(null, new HSSFWorkbook(), new DefaultExcelValueParser(null));
		this.init();
	}

	public POIExcelGeneratorImpl(TemplateModel template){
		this(template, null);
	}
	public POIExcelGeneratorImpl(TemplateModel template, Map<String, Object> context) {
		this(new WorkbookData(null, new HSSFWorkbook(), new DefaultExcelValueParser(context)), template);
	}
	public POIExcelGeneratorImpl(WorkbookData workbook, TemplateModel template) {
		this.workbookData = workbook;
		this.tempalte = template;
//		this.excelValueParser = excelValueParser;
		this.init();
	}
	
	final protected void init(){
		RowProcessor titleProcessor;
		RowProcessor iteratorProcessor;
		RowProcessor rowProcessor;
		
		rowProcessor = new DefaultRowProcessor(this);
		titleProcessor = new DefaultRowProcessor(this, new DefaultCellStyleBuilder(this){

			@Override
			public String getFont(FieldModel field) {
				return field.getHeaderFont();
			}

			@Override
			public String getStyle(FieldModel field) {
				return field.getHeaderStyle();
			}
			
			
		}){
			@Override
			public Object getDefaultFieldValue(FieldModel field) {
				return field.getLabel();
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
		return this.workbookData.getExcelValueParser();
	}

	/*public void setExcelValueParser(ExcelValueParser excelValueParser) {
		this.excelValueParser = excelValueParser;
	}*/

	public Workbook getWorkbook() {
		return this.workbookData.getWorkbook();
	}
	

	public static boolean isMultipleAndNotEmpty(Object obj){
		if(obj==null)
			return false;
		Object mobj = obj;
		if(ExcelPage.class.isInstance(obj)){
			mobj = ((ExcelPage)obj).getResult();
		}
		return ExcelUtils.isMultipleAndNotEmpty(mobj);
	}

	@Override
	public Workbook generateIt() {
//		this.initWorkbookData();
		
		boolean createSheet = true;
		if(StringUtils.isNotBlank(tempalte.getCondition())){
			createSheet = (Boolean)workbookData.parseValue(tempalte.getCondition());
		}
		if(!createSheet){
			logger.info("condition[ {} = {} ], ignore create sheet.", tempalte.getCondition(), createSheet);
			return null;
		}
		SheetDatasource<?> ds = null;
		if(StringUtils.isBlank(tempalte.getDatasource())){
			ds = new ListExportDatasource(this.workbookData, tempalte, Collections.EMPTY_LIST);
		}else{
			Object dsObj = workbookData.parseValue(tempalte.getDatasource());
			if(dsObj instanceof SheetDatasource){
				ds = (SheetDatasource<?>) dsObj;
			}else{
				List<?> datalist = ExcelUtils.tolist(dsObj);
				ds = new ListExportDatasource(this.workbookData, tempalte, datalist);
			}
		}
		

		int dataSourceSize = 0;
		SheetData sdata = createSheetData(getWorkbookData());
		sdata.setTotalSheet(1);
		if(tempalte.isMultiSheet()){
			Object dataObject = null;
			int i = 0;
//			boolean createAtleastOneSheet = false;
			while((dataObject = ds.getSheetDataList(i))!=null && isMultipleAndNotEmpty(dataObject) ){
				sdata.setSheetIndexOfSheets(i);
				List<?> datalist = null;
				if(ExcelPage.class.isInstance(dataObject)){
					ExcelPage page = (ExcelPage) dataObject;
					datalist = page.getResult();
					if(i==0)
						sdata.setTotalSheet(page.getTotalPages());
				}else{
					datalist = ExcelUtils.tolist(dataObject);
				}
				logger.info("{} sheet, data size: {}", i, datalist.size());
				sdata.setDatasource(datalist);
				Sheet sheet = this.generateSheet(ds.getSheetLabel(i), sdata);
				
				i++;
				dataSourceSize += sheet.getLastRowNum();
			}
			//如果一个sheet也没有，创建一个空的
			if(i==0){
				sdata.setDatasource(Collections.EMPTY_LIST);
				Sheet sheet = this.generateSheet(ds.getSheetLabel(i), sdata);
				dataSourceSize = sheet.getLastRowNum();
			}
		}else{
			sdata.setDatasource(ds.getSheetDataList(0));
			Sheet sheet = this.generateSheet(ds.getSheetLabel(0), sdata);
			
			dataSourceSize = sheet.getLastRowNum();
		}
		if(logger.isInfoEnabled()){
			logger.info("workbook generate data size: {}", dataSourceSize);
		}
		
		return workbookData.getWorkbook();
	}
	
	private Sheet generateSheet(String sheetname, SheetData sdata){
		//the maximum sheet name is 30 character in xssfworkbook 
		if(sheetname.length()>30)
			sheetname = sheetname.substring(0, 30);
		Sheet sheet = getWorkbook().createSheet(sheetname);
		int sheetIndex = getWorkbook().getSheetIndex(sheet);
		this.getWorkbookData().getWorkbookListener().afterCreateSheet(sheet, sheetIndex);
		
		/*Map<Integer, Short> columnMap = this.propertyStringParser.parseColumnwidth(this.tempalte.getColumnWidth());
		if(LangUtils.isNotEmpty(columnMap)){
			for(Entry<Integer, Short> entry : columnMap.entrySet()){
				sheet.setColumnWidth(entry.getKey(), entry.getValue()*256);
			}
		}*/
		
		/*if(sdata==null){
			sdata = createSheetData(getWorkbookData(), datalist);
		}*/
		sdata.setSheet(sheet);
		this.buildColumnWidth(sdata);
		this.generateSheet(sdata);
		this.buildAutoColumnSize(sdata);
		
		return sheet;
	}
	
	private void buildColumnWidth(SheetData sdata){
		String columnWidth = sdata.getSheetModel().getColumnWidth();
		if(StringUtils.isNotBlank(columnWidth) && columnWidth.startsWith("#")){
			columnWidth = (String)sdata.parseValue(columnWidth);
		}
		Map<Integer, Short> columnMap = this.propertyStringParser.parseColumnwidth(columnWidth);
		if(!ExcelUtils.isEmpty(columnMap)){
			for(Entry<Integer, Short> entry : columnMap.entrySet()){
				sdata.getSheet().setColumnWidth(entry.getKey(), entry.getValue()*256);
			}
		}
	}

	
	private void buildAutoColumnSize(SheetData sdata){
		Sheet sheet = sdata.getSheet();
		if(sheet.getPhysicalNumberOfRows()>0 && sdata.getSheetModel().isAutoSizeColumn()){
			int cellCount = sheet.getRow(0).getPhysicalNumberOfCells();
			boolean useMerged = sdata.getSheetModel().isUseMergedCells();
			for (int i = 0; i < cellCount; i++) {
				sheet.autoSizeColumn(i, useMerged);
			}
		}else{
			Map<Short, Boolean> columSize = sdata.getSheetModel().getAutoSizeColumnMap();
			if(ExcelUtils.isEmpty(columSize))
				return ;
			for(Entry<Short, Boolean> entry : columSize.entrySet()){
				sheet.autoSizeColumn(entry.getKey(), entry.getValue());
			}
		}
	}
	
	private SheetData createSheetData(WorkbookData workbookData){
		SheetData sdata = new SheetData(getWorkbookData(), tempalte);
		this.initSheetData(sdata);
		return sdata;
	}

	private void initSheetData(SheetData sdata){
		for(RowModel row : this.tempalte.getRows()){
			for(FieldModel field : row.getFields()){
				for(ExecutorModel model : field.getValueExecutors()){
					if(model.getInstance()!=null){
						sdata.addFieldValueExecutor(model, model.getInstance());
					}else{
						FieldValueExecutor executor = (FieldValueExecutor)this.workbookData.getExcelValueParser().parseValue(model.getExecutor(), null, null);
						if(executor!=null)
							sdata.addFieldValueExecutor(model, executor);
					}
				}
				
			}
		}
		sdata.initData();
	}
	
	private void generateSheet(SheetData sheetData) {
		Assert.notNull(this.tempalte, "template can not be null");
		final List<RowModel> rows = this.tempalte.getRows();
		Assert.notEmpty(rows, "rows can not be null");
//		sheet = workbook.createSheet(tempalte.getName());
		//for (RowModel row : this.tempalte.getRows()) {
//		int columns = 0;
//		boolean printTime = UtilTimerStack.isActive();
//		String rowname = "";
		
		sheetData.getSelfContext().put(tempalte.getVarName(), sheetData);
		try{
			for (int i = 0; i < rows.size(); i++) {
				RowModel row = rows.get(i);
				/*if(printTime){
					rowname = row.getType()+"-"+row.getName()+i;
					UtilTimerStack.push(rowname);
				}*/
//				Object dataSourceValue = getExcelValueParser().parseValue(row.getDatasource(), null, null);A
//				if(StringUtils.isNotBlank(row.getCondition())){
//					Boolean createRow = (Boolean)sheetData.parseValue(row.getCondition());
//					if(createRow==null || !createRow){
//						continue;
//					}
//				}
				RowContextData datacontext = createRowDataContext(sheetData, row);
				this.rowProcessors.get(row.getType()).processRow(datacontext);
				/*if(printTime){
					UtilTimerStack.pop(rowname);
				}*/
			}
		}finally{
			sheetData.getSelfContext().remove(tempalte.getVarName());
		}
		
		
	}
	
	private RowContextData createRowDataContext(SheetData sheetData, RowModel rowModel){
		RowContextData data = new RowContextData(sheetData, rowModel);
		data.initData();
		return data;
	}

	public PropertyStringParser getPropertyStringParser() {
		return propertyStringParser;
	}

	@Override
	public WorkbookData getWorkbookData() {
		return workbookData;
	}

	public static void main(String[] args) {
	}

}
