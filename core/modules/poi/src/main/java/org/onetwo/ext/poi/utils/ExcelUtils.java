package org.onetwo.ext.poi.utils;

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

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.onetwo.ext.poi.excel.etemplate.ExcelTemplateValueProvider;
import org.onetwo.ext.poi.excel.exception.ExcelException;
import org.onetwo.ext.poi.excel.generator.DefaultPropertyStringParser;
import org.onetwo.ext.poi.excel.generator.ExcelGenerators;
import org.onetwo.ext.poi.excel.generator.ExecutorModel;
import org.onetwo.ext.poi.excel.generator.FieldModel;
import org.onetwo.ext.poi.excel.generator.PoiModel;
import org.onetwo.ext.poi.excel.generator.PropertyStringParser;
import org.onetwo.ext.poi.excel.generator.RowModel;
import org.onetwo.ext.poi.excel.generator.TemplateModel;
import org.onetwo.ext.poi.excel.generator.VarModel;
import org.onetwo.ext.poi.excel.generator.WorkbookModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import ognl.Ognl;

abstract public class ExcelUtils {
	private final static Logger logger = LoggerFactory.getLogger(ExcelUtils.class);
	
	public static final String JSON_FILTER_TEMPLATE = "templateModelFilter";
	public static final String JSON_FILTER_ROW = "rowModelFilter";
	public static final String JSON_FILTER_FIELD = "fieldModelFilter";
	
	public static final Pattern IS_DIGIT = Pattern.compile("^\\d+$");
	public static final Pattern AWORD = Pattern.compile("^\\w+$", Pattern.CASE_INSENSITIVE);
	
	public static final PropertyStringParser DEFAULT_PROPERTY_STRING_PARSER = new DefaultPropertyStringParser();
	
	private static final List<Class<?>> BASE_CLASS;
//	private static final List<Class<?>> SIMPLE_CLASS;

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
		
//		SIMPLE_CLASS = Collections.unmodifiableList(simples);
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
	
	@SuppressWarnings("unchecked")
	public static <T> T readTemplate(Resource config){
		XStream xstream = registerExcelModel();
		
		T template = null;
		try {
			
			if(config.exists()){
				template = (T)xstream.fromXML(new FileInputStream(config.getFile()));
			}else{
				template = (T)xstream.fromXML(config.getInputStream());
			}
		} catch (Exception e) {
			throw wrapAsUnCheckedException("读取模板["+config+"]配置出错：" + e.getMessage(), e);
		} 
		
		return template;
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
//			cell.setCellType(Cell.CELL_TYPE_BLANK);
			cell.setBlank();
			return ;
		}
		if(Number.class.isInstance(value)){
//			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellValue(((Number)value).doubleValue());
		}else if(String.class.isInstance(value)){
//			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(value.toString());
		}else if(Boolean.class.isInstance(value)){
//			cell.setCellType(Cell.CELL_TYPE_BOOLEAN);
			cell.setCellValue((Boolean)value);
		}else if(Date.class.isInstance(value)){
//			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cell.setCellValue((Date)value);
		}else if(Calendar.class.isInstance(value)){
//			cell.setCellType(Cell.CELL_TYPE_FORMULA);
			cell.setCellValue((Calendar)value);
		}else{
			HSSFRichTextString cellValue = new HSSFRichTextString(value.toString());
			cell.setCellValue(cellValue);
		}
		
	}
	

	public static Object getCellValue(Cell cell, boolean convertCellTypeAsString){
		if(!convertCellTypeAsString){
			return getCellValue(cell);
		}
		if(cell==null)
			return null;
		cell.setCellType(CellType.STRING);
		String value = StringUtils.trimToEmpty(cell.getStringCellValue());
		return value;
	}
	
	public static Object getCellValue(Cell cell){
		if(cell==null)
			return null;
		CellType type = cell.getCellType();
		Object value = null;
		if(CellType.STRING==type){
//			value = StringUtils.cleanInvisibleUnicode(cell.getStringCellValue().trim());
			value = cell.getStringCellValue().trim();
		}else if(CellType.NUMERIC==type){
			value = cell.getNumericCellValue();
		}else if(CellType.FORMULA==type){
			value = cell.getCellFormula();
		}else if(CellType.BOOLEAN==type){
			value = cell.getBooleanCellValue();
		}else if(CellType.BLANK==type){
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

	public static Map<String, Object> mapRow(Row row, List<String> names){
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

	public static Object getValue(String exp, Map<?, ?> context, Object root){
		Object value = null;
		try {
			value = Ognl.getValue(exp, context==null?Collections.EMPTY_MAP:context, root);
		} catch (Exception e) {
			if(ExcelGenerators.isDevModel()){
				logger.warn("["+exp+"] getValue error : " + e.getMessage());
			}
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
			if(ExcelGenerators.isDevModel()){
				logger.warn("get formual cell value["+cell.getRowIndex()+", "+ cell.getColumnIndex()+"] error : " + e.getMessage());
			}
			return null;
		}
	}
	

	public static Workbook createWorkbook(File file){
		Workbook workbook = null;
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			workbook = createWorkbook(in);
		} catch (Exception e) {
			throw new ExcelException("read excel error : " + file.getPath(), e);
		}finally{
			IOUtils.closeQuietly(in);
		}
		return workbook;
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
			cell.setCellType(CellType.STRING);
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

			String cellText = cellValue.toString();
			if(provider.isExpresstion(cellText)){
				Object newCellValue = provider.parseCellValue(cellText);
				provider.setCellValue(cell, newCellValue);
			}
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
                case BLANK:
                    newCell.setCellValue(oldCell.getStringCellValue());
                    break;
                case BOOLEAN:
                    newCell.setCellValue(oldCell.getBooleanCellValue());
                    break;
                case ERROR:
                    newCell.setCellErrorValue(oldCell.getErrorCellValue());
                    break;
                case FORMULA:
                    newCell.setCellFormula(oldCell.getCellFormula());
                    break;
                case NUMERIC:
                    newCell.setCellValue(oldCell.getNumericCellValue());
                    break;
                case STRING:
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
		return tolist(object, Collections.emptyList());
	}
	
	@SuppressWarnings("unchecked")
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

	public static boolean isEmpty(Map<?, ?> map){
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
	

	@SuppressWarnings("unchecked")
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
	

	public static BeanWrapper newBeanMapWrapper(Object obj, Object...listElementTypes){
		BeanWrapper bw = null;
		if(Map.class.isInstance(obj)){
			bw = new BeanMapWrapper(obj, listElementTypes);
		}else{
			bw = PropertyAccessorFactory.forBeanPropertyAccess(obj);
		}
		bw.setAutoGrowNestedPaths(true);
		return bw;
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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
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
			return ((Collection<?>)obj).size();
		}else if(obj instanceof CharSequence){
			return ((CharSequence)obj).length();
		}else if(obj instanceof Map){
			return ((Map<?, ?>)obj).size();
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
			Iterator<?> it = ((Iterable<?>)obj).iterator();
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
		return list==null?Collections.emptyList():list;
	}
	
	public static String getResourcePath(String path){
		String fullpath = ExcelUtils.class.getClassLoader().getResource("").getPath() + path;
		return fullpath;
	}

	public static OPCPackage readPackage(String path){
		try {
			OPCPackage pack = POIXMLDocument.openPackage(path);
			return pack;
		} catch (IOException e) {
			throw new RuntimeException("read word file["+path+"] error:"+e.getMessage());
		}
	}
	
	public static String toCamel(String str, boolean isFirstUpper) {
		return toCamel(str, '_', isFirstUpper);
	}

	public static String toCamel(String str, char op, boolean isFirstUpper) {
		if (str.indexOf(op) == -1) {
			str = str.toLowerCase();
			if (isFirstUpper && Character.isLowerCase(str.charAt(0))) {
				return str.substring(0, 1).toUpperCase() + str.substring(1);
			} else {
				return str;
			}
		}
		char[] chars = str.toCharArray();
		StringBuilder newStr = new StringBuilder();
		boolean needUpper = isFirstUpper;
		for (int i = 0; i < chars.length; i++) {
			char c = Character.toLowerCase(chars[i]);
			if (needUpper) {
				c = Character.toUpperCase(c);
				needUpper = false;
			}
			if (c == op) {
				needUpper = true;
				continue;
			}
			newStr.append(c);
		}
		return newStr.toString();
	}
	
	public static boolean isCellTypeString(CellType type) {
		return type==CellType.STRING;
	}
	public static void setCellTypeAsString(Cell cell) {
		cell.setCellType(CellType.STRING);
	}
}
