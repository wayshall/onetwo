package org.onetwo.ext.poi.excel.reader;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

public interface ExcelReader {
	
	public <T> Map<String, T> readData(InputStream in, ExcelDataExtractor<T> extractor);
	
	public <T> Map<String, T> readData(InputStream in, ExcelDataExtractor<T> extractor, int startSheet, int endSheet);
	
	public <T> Map<String, T> readData(File file, ExcelDataExtractor<T> extractor, int startSheet, int endSheet);


}