package org.onetwo.common.excel;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.interfaces.excel.ExcelValueParser;
import org.onetwo.common.profiling.UtilTimerStack;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;

public class POIExcelGeneratorImpl implements PoiExcelGenerator {

	protected TemplateModel tempalte;
	// protected Object root;
//	protected Map context;
	protected HSSFWorkbook workbook;
	protected HSSFSheet sheet;
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
		Assert.notNull(this.tempalte);
		final List<RowModel> rows = this.tempalte.getRows();
		Assert.notEmpty(rows);
		sheet = workbook.createSheet(tempalte.getName());
		//for (RowModel row : this.tempalte.getRows()) {
		int columns = 0;
		boolean printTime = UtilTimerStack.isActive();
		String rowname = "";
		for (int i = 0; i < rows.size(); i++) {
			RowModel row = rows.get(i);

			if(printTime){
				rowname = row.getType()+"-"+row.getName()+i;
				UtilTimerStack.push(rowname);
			}
			this.rowProcessors.get(row.getType()).processRow(sheet, row);
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
		
		//tttttttttttttt
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
