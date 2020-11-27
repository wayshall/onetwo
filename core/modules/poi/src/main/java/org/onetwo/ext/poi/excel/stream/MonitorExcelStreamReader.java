package org.onetwo.ext.poi.excel.stream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.file.FileUtils;
import org.onetwo.ext.poi.excel.stream.SheetStreamReaderBuilder.SheetData;
import org.onetwo.ext.poi.excel.stream.SheetStreamReaderBuilder.SheetStreamReader;

import com.monitorjbl.xlsx.StreamingReader;

/**
 * @author weishao zeng
 * <br/>
 */

public class MonitorExcelStreamReader extends BaseExcelStreamReader {

	public MonitorExcelStreamReader(List<SheetStreamReader<?>> sheetReaders) {
		super(sheetReaders);
	}
	
	@Override
	public void from(String path, SheeDataModelConsumer consumer) {
		from(new File(path), consumer);
	}

	@Override
	public void from(File file, SheeDataModelConsumer consumer) {
		String ext = FileUtils.getExtendName(file.getName());
		if (!ext.equalsIgnoreCase("xlsx")) {
			throw new ServiceException("不支持的文件格式, 请使用xlsx格式：" + file.getName());
		}
		InputStream inputStream;
		try {
			inputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new BaseException("read excel file error: " + e.getMessage());
		}
		from(inputStream, consumer);
	}

	@Override
	public void from(InputStream inputStream, SheeDataModelConsumer consumer) {
		Workbook workbook = StreamingReader.builder()
		        .rowCacheSize(100)    // number of rows to keep in memory (defaults to 10)
		        .bufferSize(4096)     // buffer size to use when reading InputStream to file (defaults to 1024)
		        .open(inputStream); 
		read(workbook, consumer);
	}


	protected SheetData createSheetData(Sheet sheet, int sheetIndex) {
		SheetData sheetData = super.createSheetData(sheet, sheetIndex);
		sheetData.setCanConverToStringValue(false);
		return sheetData;
	}
	
	protected boolean isIgnoreSheet(Sheet sheet) {
		return false;
	}
}
