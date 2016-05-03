package org.onetwo.common.excel.utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ognl.Ognl;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.onetwo.common.excel.DefaultPropertyStringParser;
import org.onetwo.common.excel.ExecutorModel;
import org.onetwo.common.excel.FieldModel;
import org.onetwo.common.excel.PoiModel;
import org.onetwo.common.excel.PropertyStringParser;
import org.onetwo.common.excel.RowModel;
import org.onetwo.common.excel.TemplateModel;
import org.onetwo.common.excel.VarModel;
import org.onetwo.common.excel.WorkbookModel;
import org.onetwo.common.excel.etemplate.ExcelTemplateValueProvider;
import org.onetwo.common.excel.exception.ExcelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

abstract public class ExcelUtils {
	private final static Logger logger = LoggerFactory.getLogger(ExcelUtils.class);
	
	public static final String JSON_FILTER_TEMPLATE = "templateModelFilter";
	public static final String JSON_FILTER_ROW = "rowModelFilter";
	public static final String JSON_FILTER_FIELD = "fieldModelFilter";
	
	public static final Pattern IS_DIGIT = Pattern.compile("^\\d+$");
	public static final Pattern AWORD = Pattern.compile("^\\w+$", Pattern.CASE_INSENSITIVE);
	
	public static final PropertyStringParser DEFAULT_PROPERTY_STRING_PARSER = new DefaultPropertyStringParser();
	
	private static final List<Class<?>> BASE_CLASS;
	private static final List<Class<?>> SIMPLE_CLASS;

	static {
		
		List<Class<?>> cls = new ArrayList<Class<?>>();
		cls.add(Boolean.class);
		cls.add(boolean.class);
		cls.add(Character.class);
		cls.add(char.class);
		cls.add(Byte.class);
		cls.add(byte.class);
		cls.add(Short.class);
		cls.add(short.class);
		cls.add(Integer.class);
		cls.add(int.class);
		cls.add(Long.class);
		cls.add(long.class);
		cls.add(Float.class);
		cls.add(float.class);
		cls.add(Double.class);
		cls.add(double.class);
		BASE_CLASS = Collections.unmodifiableList(cls);
		
		List<Class<?>> simples = new ArrayList<Class<?>>(cls);
		simples.add(String.class);
		simples.add(Date.class);
		simples.add(Calendar.class);
		simples.add(Number.class);
		
		SIMPLE_CLASS = Collections.unmodifiableList(simples);
	}

	public static List<Class<?>> getBaseTypeClass(){
		return BASE_CLASS;
	}
	
	public static boolean isExpr(String str){
		if(isBlank(str))
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
		for(Class<?> btype : getBaseTypeClass()){
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
			throw wrapAsUnCheckedException("读取模板["+config+"]配置出错：" + e.getMessage(), e);
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
			value = Ognl.getValue(exp, context==null?Collections.EMPTY_MAP:context, root);
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
			throw new ExcelException("read workbook error by inputStream : " + e.getMessage(), e);
		} 
	}
	
	public static Workbook readWorkbookFromClasspath(String path){
		return readWorkbook(new ClassPathResource(path));
	}
	
	public static Workbook readWorkbook(Resource res){
		try {
			return WorkbookFactory.create(res.getInputStream());
		} catch (Exception e) {
			throw new ExcelException("read workbook error by resource : " + e.getMessage(), e);
		} 
	}
	
	public static CellValue getFormulaCellValue(Cell cell){
		try {
			return cell==null?null:cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator().evaluate(cell);
		} catch (Exception e) {
//			throw new BaseException("get formual cell value["+cell.getRowIndex()+", "+ cell.getColumnIndex()+"] error : " + e.getMessage(), e);
			logger.warn("get formual cell value["+cell.getRowIndex()+", "+ cell.getColumnIndex()+"] error : " + e.getMessage());
			return null;
		}
	}
	
	public static Workbook createWorkbook(InputStream in){
		Workbook workbook = null;
		try {
//			br.mark(1024*10);
			workbook = WorkbookFactory.create(in);
		} catch (Exception e) {
			throw wrapAsUnCheckedException("read excel inputstream error : " + in, e);
		}finally{
			IOUtils.closeQuietly(in);
		}
		return workbook;
	}
	
	public static void addRow(Sheet sheet, int rowNumber, int count){
		if(count<0){
			//remove
			rowNumber += Math.abs(count);
		}
		if(rowNumber>sheet.getLastRowNum())
			rowNumber = sheet.getLastRowNum();
		sheet.shiftRows(rowNumber, sheet.getLastRowNum(), count, true, true);
	}
	
	public static void copyCellStyle(Cell source, Cell target){
		CellStyle style = source.getCellStyle();
		if(style!=null){
			//TODO:会影响性能, 可缓存。。。
			CellStyle newCellStyle = source.getRow().getSheet().getWorkbook().createCellStyle();
			newCellStyle.cloneStyleFrom(style);
			target.setCellStyle(style);
		}
		if(source.getCellComment()!=null){
			target.setCellComment(source.getCellComment());
		}
		if(source.getHyperlink()!=null){
			target.setHyperlink(source.getHyperlink());
		}
		target.setCellType(source.getCellType());
	}
	
	public static void clearRowValue(Row row){
		if(row==null)
			return ;
		for(Cell cell : row){
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue("");
		}
	}
	
	public static void removeCellRange(Row row){
		Sheet sheet = row.getSheet();
		for(Cell cell : row){
			for(int i=0; i< sheet.getNumMergedRegions(); i++){
				CellRangeAddress cr = sheet.getMergedRegion(i);
				if(cr.getFirstRow()==row.getRowNum() && cr.getFirstColumn()==cell.getColumnIndex()){
					sheet.removeMergedRegion(i);
				}
			}
		}
	}
	

	public static RuntimeException wrapAsUnCheckedException(Exception e){
		return wrapAsUnCheckedException("error: " + e.getMessage(), e);
	}
	public static RuntimeException wrapAsUnCheckedException(String msg, Exception e){
		if(!RuntimeException.class.isInstance(e)){
			return new RuntimeException(msg, e);
		}
		return (RuntimeException) e;
	}
	
	public static void parseCommonRow(Row row, ExcelTemplateValueProvider provider){
		int cellNumbs = row.getPhysicalNumberOfCells();
		for (int cellIndex = 0; cellIndex < cellNumbs; cellIndex++) {
			Cell cell = row.getCell(cellIndex);
			Object cellValue = getCellValue(cell);
			if(cellValue==null)
				continue;
			
			Object newCellValue = provider.parseCellValue(cell, provider);
			ExcelUtils.setCellValue(cell, newCellValue);
		}
	}
	
	public static void copyRow(Sheet worksheet, Row newRow, Row sourceRow) {
		Workbook workbook = worksheet.getWorkbook();
        for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
            Cell oldCell = sourceRow.getCell(i);
            Cell newCell = newRow.createCell(i);

            if (oldCell == null) {
                newCell = null;
                continue;
            }

            CellStyle newCellStyle = workbook.createCellStyle();
            newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
            newCell.setCellStyle(newCellStyle);

            if (oldCell.getCellComment() != null) {
                newCell.setCellComment(oldCell.getCellComment());
            }

            if (oldCell.getHyperlink() != null) {
                newCell.setHyperlink(oldCell.getHyperlink());
            }

            newCell.setCellType(oldCell.getCellType());

            switch (oldCell.getCellType()) {
                case Cell.CELL_TYPE_BLANK:
                    newCell.setCellValue(oldCell.getStringCellValue());
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    newCell.setCellValue(oldCell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_ERROR:
                    newCell.setCellErrorValue(oldCell.getErrorCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    newCell.setCellFormula(oldCell.getCellFormula());
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    newCell.setCellValue(oldCell.getNumericCellValue());
                    break;
                case Cell.CELL_TYPE_STRING:
                    newCell.setCellValue(oldCell.getRichStringCellValue());
                    break;
            }
        }

        for (int i = 0; i < worksheet.getNumMergedRegions(); i++) {
            CellRangeAddress cellRangeAddress = worksheet.getMergedRegion(i);
            if (cellRangeAddress.getFirstRow() == sourceRow.getRowNum()) {
                CellRangeAddress newCellRangeAddress = new CellRangeAddress(newRow.getRowNum(),
                        (newRow.getRowNum() +
                                (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow()
                                        )),
                        cellRangeAddress.getFirstColumn(),
                        cellRangeAddress.getLastColumn());
                worksheet.addMergedRegion(newCellRangeAddress);
            }
        }
    }
	
	public static boolean isBlank(String str) {
		return str == null || str.trim().equals("");
	}
	public static boolean isMultiple(Object obj){
		if(obj==null)
			return false;
		if(obj instanceof Iterable){
			return true;
		}else if(obj.getClass().isArray()){
			return true;
		}else {
			return false;
		}
	}
	

	public static <T> List<T> tolist(Object object) {
		return tolist(object, Collections.EMPTY_LIST);
	}
	
	public static <T> List<T> tolist(Object object, List<T> def) {
		if (object == null)
			return def;
		List<T> list = null;
		if (List.class.isInstance(object)) {
			list = (List<T>) object;
		} else if(Iterable.class.isInstance(object)){
			list = new ArrayList<>();
			for(T obj : (Iterable<T>)object){
				list.add(obj);
			}
		}else if (object.getClass().isArray()) {
			int length = Array.getLength(object);
			list = new ArrayList<>(length);
//			appendToList(object, list);
			for (int i = 0; i < length; i++) {
				list.add((T)Array.get(object, i));
			}
		} else {
			list = new ArrayList<>(5);
			list.add((T)object);
		}
		return list == null ? def : list;
	}
	
	public static void makeDirs(String path){
		makeDirs(path, !new File(path).isDirectory());
	}
    
	public static void makeDirs(String path, boolean file){
		File outDir = new File(path);
		if(file)
			outDir = outDir.getParentFile();
		
		if(!outDir.exists())
			if(!outDir.mkdirs())
				throw new RuntimeException("can't create output dir:"+path);
	}
	
	public static boolean isEmpty(Collection<?> col){
		return (col==null || col.isEmpty());
	}

	public static boolean isEmpty(Map map){
		return map==null || map.isEmpty();
	}
	
	public static void closeIO(Closeable io){
		try {
			if(io!=null)
				io.close();
		} catch (IOException e) {
			throw new RuntimeException("close io error: " + e.getMessage(), e);
		}
	}
	

	public static boolean isEmpty(Object[] arrays){
		return (arrays==null || arrays.length==0);
	}
	

	public static <K, V> Map<K, V> asMap(Object... arrays){
		 return Stream.iterate(0, i->i+2)
				 .limit(arrays.length/2)
				 .map(i->new Object[]{arrays[i], arrays[i+1]})
				 .collect(Collectors.toMap(array->(K)array[0], array->(V)array[1]));
	}
	

	public static String trimToEmpty(Object str) {
		return str == null ? "" : str.toString().trim();
	}
	
	public static <T> T newInstance(Class<T> clazz) {
		T instance = null;
		try {
			instance = clazz.newInstance();
		} catch (Exception e) {
			throw wrapAsUnCheckedException("instantce class error : " + clazz, e);
		}
		return instance;
	}
	
	public static String strings(String... strings) {
		if (strings == null || strings.length == 0)
			return "";
		int size = 0;
		for(String str : strings){
			if(str==null)
				continue;
			size += str.length();
		}
		StringBuilder sb = new StringBuilder(size);
		for (String str : strings){
			if(str==null)
				continue;
			sb.append(str);
		}
		return sb.toString();
	}
	
	public static BeanWrapper newBeanWrapper(Object obj){
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(obj);
		bw.setAutoGrowNestedPaths(true);
		return bw;
	}
	
	public static String defaultValues(String val, String... defs){
		if(isBlank(val)){
			for(String def : defs){
				if(isNotBlank(def))
					return def;
			}
		}
		return val;
	}
	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}
	
	@SuppressWarnings("rawtypes")
	public static Iterator convertIterator(Object value) {
        Iterator iterator;

        if (value instanceof Iterator) {
            return (Iterator) value;
        }

        if (value instanceof Map) {
            value = ((Map) value).entrySet();
        }

        if (value == null) {
            return null;
        }

        if (value instanceof Iterable) {
            iterator = ((Iterable) value).iterator();
        } else if (value.getClass().isArray()) {
            //need ability to support primitives; therefore, cannot
            //use Object[] casting.
            ArrayList list = new ArrayList(Array.getLength(value));

            for (int j = 0; j < Array.getLength(value); j++) {
                list.add(Array.get(value, j));
            }

            iterator = list.iterator();
        } else if (value instanceof Enumeration) {
            Enumeration enumeration = (Enumeration) value;
            ArrayList list = new ArrayList();

            while (enumeration.hasMoreElements()) {
                list.add(enumeration.nextElement());
            }

            iterator = list.iterator();
        } else {
            List list = new ArrayList(1);
            list.add(value);
            iterator = list.iterator();
        }

        return iterator;
    }
	
	public static int size(Object obj){
		if(obj==null)
			return 0;
		if(obj instanceof Collection){
			return ((Collection)obj).size();
		}else if(obj instanceof CharSequence){
			return ((CharSequence)obj).length();
		}else if(obj instanceof Map){
			return ((Map)obj).size();
		}else if(obj.getClass().isArray()){
			return Array.getLength(obj);
		}
		return 0;
	}
	
	public static boolean isMultipleAndNotEmpty(Object obj){
		if(obj==null)
			return false;
		if(obj instanceof Iterable){
//			return !isEmpty((Collection)obj);
			Iterator<?> it = ((Iterable)obj).iterator();
			return it.hasNext();
		}else if(obj.getClass().isArray()){
			return !isEmpty((Object[])obj);
		}else {
			return false;
		}
	}
	public static boolean isWord(String str){
		return AWORD.matcher(str).matches();
	}

	public static <T> List<T> emptyIfNull(List<T> list){
		return list==null?Collections.EMPTY_LIST:list;
	}
	
	public static String getResourcePath(String path){
		String fullpath = ExcelUtils.class.getClassLoader().getResource("").getPath() + path;
		return fullpath;
	}
}
