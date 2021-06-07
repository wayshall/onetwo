package org.onetwo.ext.poi.excel.stream;

import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.onetwo.ext.poi.excel.stream.SheetStreamReaderBuilder.SheetData;
import org.onetwo.ext.poi.excel.stream.SheetStreamReaderBuilder.SheetStreamReader;
import org.onetwo.ext.poi.utils.ExcelUtils;
import org.springframework.util.Assert;

/**
 * @author weishao zeng
 * <br/>
 */
abstract public class BaseExcelStreamReader implements ExcelStreamReader {
	protected List<SheetStreamReader<?>> sheetReaders;
	
	protected BaseExcelStreamReader(List<SheetStreamReader<?>> sheetReaders) {
		super();
		this.sheetReaders = sheetReaders;
	}
	
	
	public void read(Workbook workbook, SheeDataModelConsumer consumer){
		Assert.notNull(workbook, "workbook can not be null");
		try {
			int sheetCount = workbook.getNumberOfSheets();
			Sheet sheet = null;
//			List<? extends PictureData> picDatas = workbook.getAllPictures();
			for(int sheetIndex=0; sheetIndex<sheetCount; sheetIndex++){
				sheet = workbook.getSheetAt(sheetIndex);
				if(isIgnoreSheet(sheet)) {
					continue;
				}
				
				for(SheetStreamReader<?> reader : sheetReaders) {
					if (reader.match(sheetIndex)) {
						SheetData sheetData = createSheetData(sheet, sheetIndex);
						Object dataInst = reader.onRead(sheetData, sheetIndex);
						if (consumer!=null && dataInst!=null) {
							consumer.onSheetReadCompleted(dataInst);
						}
					}
				}
			}
		} catch (Exception e) {
			throw ExcelUtils.wrapAsUnCheckedException("read excel file error.", e);
		}
	}
	
	protected SheetData createSheetData(Sheet sheet, int sheetIndex) {
		SheetData sheetData = new SheetData(sheet, sheetIndex);
		return sheetData;
	}
	
	protected boolean isIgnoreSheet(Sheet sheet) {
		return sheet.getPhysicalNumberOfRows()<1;
	}
	
	public static interface SheeDataModelConsumer {
		/****
		 * 当整个sheet读取完毕时，触发此回调
		 * @author weishao zeng
		 * @param dataInst
		 */
		void onSheetReadCompleted(Object dataInst);
	}
	
}

