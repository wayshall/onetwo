package org.onetwo.common.excel;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import ognl.Ognl;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

abstract public class ExcelUtils {
	private final static Logger logger = MyLoggerFactory.getLogger(ExcelUtils.class);
	
	public static final String JSON_FILTER_TEMPLATE = "templateModelFilter";
	public static final String JSON_FILTER_ROW = "rowModelFilter";
	public static final String JSON_FILTER_FIELD = "fieldModelFilter";
	
	public static final Pattern IS_DIGIT = Pattern.compile("^\\d+$");
	
	public static final PropertyStringParser DEFAULT_PROPERTY_STRING_PARSER = new DefaultPropertyStringParser();
	
	public static boolean isExpr(String str){
		if(StringUtils.isBlank(str))
			return false;
		str = str.trim();
		return str.startsWith("%{") && str.endsWith("}");
	}
	
	public static String getExpr(String str){
		return str.substring(2, str.length()-1);
	}

	public static <T> T readTemplate(String path){
		Resource config = new ClassPathResource(path);
		return readTemplate(config);
	}
	

	public static WorkbookModel readAsWorkbookModel(String path){
		return readAsWorkbookModel(new ClassPathResource(path));
	}
	
	public static WorkbookModel readAsWorkbookModel(Resource config){
		WorkbookModel model = null;
		PoiModel m = ExcelUtils.readTemplate(config);
		if(TemplateModel.class.isInstance(m)){
			model = new WorkbookModel();
			model.addSheet((TemplateModel)m);
		}else{
			model = (WorkbookModel) m;
		}
		model.initModel();
		return model;
	}
	

	public static XStream registerExcelModel(){
		XStream xstream = new XStream(new DomDriver());
		xstream.alias("workbook", WorkbookModel.class);
		xstream.alias("vars", List.class);
		xstream.alias("var", VarModel.class);
		xstream.alias("sheets", List.class);
		xstream.alias("template", TemplateModel.class);
		xstream.alias("sheet", TemplateModel.class);
		xstream.alias("rows", List.class);
		xstream.alias("row", RowModel.class);
		xstream.alias("field", FieldModel.class);
		xstream.alias("valueExecutors", List.class);
		xstream.alias("valueExecutor", ExecutorModel.class);
//		xstream.useAttributeFor(Number.class);
//		xstream.useAttributeFor(boolean.class);
//		xstream.useAttributeFor(String.class); 
//		xstream.useAttributeFor(int.class); 
		xstream.useAttributeFor(String.class);
		for(Class<?> btype : LangUtils.getBaseTypeClass()){
			xstream.useAttributeFor(btype);
		}
		return xstream;
	}
	
	public static <T> T readTemplate(Resource config){
		XStream xstream = registerExcelModel();
		
		Object template = null;
		try {
			
			if(config.exists()){
				template = xstream.fromXML(new FileInputStream(config.getFile()));
			}else{
				template = xstream.fromXML(config.getInputStream());
			}
		} catch (Exception e) {
			throw new ServiceException("读取模板["+config+"]配置出错：" + e.getMessage(), e);
		} 
		
		return (T)template;
	}
	
	public static void setCellValue(Cell cell, Object value){
		/*if(value==null){
			cell.setCellValue("");
			return ;
		}
		 
		if(value instanceof Date){
//			cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
			HSSFRichTextString cellValue = new HSSFRichTextString(DateUtil.formatDateTime((Date)value));
			cell.setCellValue(cellValue);
		}else{
			HSSFRichTextString cellValue = new HSSFRichTextString(value.toString());
			cell.setCellValue(cellValue);
		}*/
		
		if(value==null){
			cell.setCellType(Cell.CELL_TYPE_BLANK);
			return ;
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
	
	public static Object getCellValue(Cell cell){
		if(cell==null)
			return null;
		int type = cell.getCellType();
		Object value = null;
		if(Cell.CELL_TYPE_STRING==type){
			value = cell.getStringCellValue().trim();
		}else if(Cell.CELL_TYPE_NUMERIC==type){
			value = cell.getNumericCellValue();
		}else if(Cell.CELL_TYPE_FORMULA==type){
			value = cell.getCellFormula();
		}else if(Cell.CELL_TYPE_BOOLEAN==type){
			value = cell.getBooleanCellValue();
		}else if(Cell.CELL_TYPE_BLANK==type){
			value = "";
		}
		return value;
	}
	

	public static List<String> getRowValues(Row row){
		int cellCount = row.getPhysicalNumberOfCells();
		List<String> rowValues = new ArrayList<String>();
		
		Cell cell = null;
		Object cellValue = null;
		for(int i=0; i<cellCount; i++){
			cell = row.getCell(i);
			cellValue = ExcelUtils.getCellValue(cell);
			rowValues.add(cellValue.toString().trim());
		}
		return rowValues;
	}

	public static Map mapRow(Row row, List<String> names){
		int cellCount = row.getPhysicalNumberOfCells();
		
		Cell cell = null;
		Object cellValue = null;
		Map<String, Object> rowValue = new HashMap<String, Object>();
		for(int i=0; i<names.size(); i++){
			String colName = names.get(i);
			if(i<=cellCount){
				cell = row.getCell(i);
				cellValue = ExcelUtils.getCellValue(cell);
			}
			rowValue.put(colName, cellValue);
		}
		return rowValue;
	}

	public static Object getValue(String exp, Map context, Object root){
		Object value = null;
		try {
			value = Ognl.getValue(exp, context, root);
		} catch (Exception e) {
//			logger.info("["+exp+"] getValue error : " + e.getMessage(), e);
			logger.info("["+exp+"] getValue error : " + e.getMessage());
		}
		return value;
	}
	
	public static Workbook readWorkbook(InputStream inp){
		try {
			return WorkbookFactory.create(inp);
		} catch (Exception e) {
			throw new BaseException("read workbook error by inputStream : " + e.getMessage(), e);
		} 
	}
	
	public static Workbook readWorkbookFromClasspath(String path){
		return readWorkbook(new ClassPathResource(path));
	}
	
	public static Workbook readWorkbook(Resource res){
		try {
			return WorkbookFactory.create(res.getInputStream());
		} catch (Exception e) {
			throw new BaseException("read workbook error by resource : " + e.getMessage(), e);
		} 
	}
	
	public static CellValue getFormulaCellValue(Cell cell){
		return cell==null?null:cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator().evaluate(cell);
	}
	
}
