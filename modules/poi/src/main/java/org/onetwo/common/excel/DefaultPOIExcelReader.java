package org.onetwo.common.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.StringUtils;

public class DefaultPOIExcelReader implements ExcelReader {
	
	public DefaultPOIExcelReader(){
	}
	
	@Override
	public <T> Map<String, T> readData(InputStream in, ExcelDataExtractor<T> extractor) {
		return readData(createWorkbook(in), extractor);
	}


	@Override
	public <T> Map<String, T> readData(InputStream in, ExcelDataExtractor<T> extractor, int startSheet, int endSheet) {
		return readData(createWorkbook(in), extractor, startSheet, endSheet);
	}
	

	@Override
	public <T> Map<String, T> readData(File file, ExcelDataExtractor<T> extractor, int startSheet, int endSheet) {
		return readData(createWorkbook(file), extractor, startSheet, endSheet);
	}

	public <T> Map<String, T> readData(Workbook workbook, ExcelDataExtractor<T> extractor){
		return readData(workbook, extractor, -1, -1);
	}
	
	/****
	 * 
	 * @param workbook
	 * @param extractor
	 * @param startSheet include
	 * @param endSheet not include
	 * @return
	 */
	public <T> Map<String, T> readData(Workbook workbook, ExcelDataExtractor<T> extractor, int startSheet, int endSheet){
		Assert.notNull(workbook);
		try {
			int sheetCount = workbook.getNumberOfSheets();
			Sheet sheet = null;
			Map<String, T> datas = new LinkedHashMap<String, T>();
			for(int i=0; i<sheetCount; i++){
				if((startSheet==-1 && endSheet==-1) || (i>=startSheet && i<endSheet)){
					sheet = workbook.getSheetAt(i);
					String name = sheet.getSheetName();
					if(sheet.getPhysicalNumberOfRows()<1)
						continue;
					if(StringUtils.isBlank(name))
						name = "" + i;
					T extractData = extractor.extractData(sheet, i);
					datas.put(name, extractData);
				}
			}
			return datas;
		} catch (Exception e) {
			throw new ServiceException("read excel file error.", e);
		}
	}
	
	protected Workbook createWorkbook(String path){
		File file = new File(path);
		if(!file.exists()){
			path = FileUtils.getResourcePath(path);
			file = new File(path);
		}
		Workbook workbook = createWorkbook(file);
		return workbook;
	}
	
	protected Workbook createWorkbook(File file){
		Workbook workbook = null;
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			workbook = createWorkbook(in, file.getName().endsWith(".xlsx"));
		} catch (Exception e) {
			IOUtils.closeQuietly(in);
			try {
				in = new FileInputStream(file);
				workbook = createWorkbook(in, !file.getName().endsWith(".xlsx"));
			} catch (Exception ee) {
				throw new ServiceException("createWorkbook error : " + file.getPath(), ee);
			}
		}finally{
			IOUtils.closeQuietly(in);
		}
		return workbook;
	}
	
	protected Workbook createWorkbook(InputStream in, boolean isXssf) {
		Workbook workbook = null;
		try {
			if(isXssf){
				workbook = new XSSFWorkbook(in);
			}else{
				workbook = new HSSFWorkbook(in);
			}
		} catch (Exception e) {
			throw new ServiceException("createWorkbook error : " + in, e);
		}
		return workbook;
	}
	
	protected Workbook createWorkbook(InputStream in){
		Workbook workbook = null;
		try {
//			br.mark(1024*10);
			workbook = createWorkbook(in, true);
		} catch (Exception e) {
			try {
				if(in.markSupported()){
					in.mark(1024*10);//10kb
				}
				in.reset();
			} catch (Exception ee) {
				throw new ServiceException("Workbook reset error : " + in, ee);
			}
			workbook = createWorkbook(in, false);
		}finally{
			IOUtils.closeQuietly(in);
		}
		return workbook;
	}
	
	protected List<?> mapRow(Sheet sheet, int sheetIndex, SSFRowMapper<?> mapper){
		int rowCount = sheet.getPhysicalNumberOfRows();
		
		List<String> names = mapper.mapTitleRow(sheetIndex, sheet);
		
		Row row = null;
		List<Object> datas = new ArrayList<Object>();
		for(int rowIndex=mapper.getDataRowStartIndex(); rowIndex<rowCount; rowIndex++){
			row = sheet.getRow(rowIndex);
			Object value = mapper.mapDataRow(sheet, names, row, rowIndex);
			if(value==null)
				continue;
			datas.add(value);
		}
		return datas;
	}
	
}
