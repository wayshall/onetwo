package org.onetwo.ext.poi.excel.reader;

public interface ExcelBufferReader<T> {

	void initReader();
	T read();
	public int getCurrentRowNumber();
	boolean isEnd();

}