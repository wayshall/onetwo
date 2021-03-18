package org.onetwo.ext.poi.excel.stream;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.onetwo.ext.poi.excel.stream.SheetStreamReaderBuilder.SheetStreamReader;
import org.onetwo.ext.poi.utils.ExcelUtils;

/**
 * @author weishao zeng
 * <br/>
 */
public class PoiExcelStreamReader extends BaseExcelStreamReader {
//	private static final DelegateSheetImageReader imageDelegateReader = new DelegateSheetImageReader();
	
	PoiExcelStreamReader(List<SheetStreamReader<?>> sheetReaders) {
		super(sheetReaders);
	}
	
	@Override
	public void from(String path, SheeDataModelConsumer consumer) {
		read(ExcelUtils.createWorkbook(new File(path)), consumer);
	}
	
	@Override
	public void from(File file, SheeDataModelConsumer consumer) {
		read(ExcelUtils.createWorkbook(file), consumer);
	}
	
	@Override
	public void from(InputStream inputStream, SheeDataModelConsumer consumer) {
		read(ExcelUtils.createWorkbook(inputStream), consumer);
	}
	
}

