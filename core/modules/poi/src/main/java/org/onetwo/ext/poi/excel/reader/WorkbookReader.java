package org.onetwo.ext.poi.excel.reader;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;


public interface WorkbookReader {

	public <T> List<T> readFirstSheet(String path);
	public <T> List<T> readFirstSheet(File file);
	public <T> List<T> readFirstSheet(InputStream in);
//	public <T> List<T> readFirstSheet(InputStream in, boolean excel2007);
	
	public Map<String, List<Object>> readData(String path);
	public Map<String, List<Object>> readData(File file);
	public Map<String, List<Object>> readData(InputStream in);
	
	public Map<String, List<Object>> readData(String path, int startSheet, int readCount);
	public Map<String, List<Object>> readData(InputStream in, int startSheet, int readCount);
	public Map<String, List<Object>> readData(File file, int startSheet, int readCount);
//	public Map<String, List<?>> readData(InputStream in, boolean excel2007);
	
//	public SSFRowMapper<?> getRowMapper();

//	public void setRowMapper(SSFRowMapper<?> rowMapper);
	

}