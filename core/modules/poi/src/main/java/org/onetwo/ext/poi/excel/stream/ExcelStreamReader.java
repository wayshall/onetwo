package org.onetwo.ext.poi.excel.stream;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.onetwo.common.convert.Types;
import org.onetwo.ext.poi.utils.ExcelUtils;
import org.springframework.util.Assert;

/**
 * @author weishao zeng
 * <br/>
 */
public class ExcelStreamReader {
	private List<SheetStreamReader> sheetReaders;
	
	private ExcelStreamReader(List<SheetStreamReader> sheetReaders) {
		super();
		this.sheetReaders = sheetReaders;
	}
	
	public void from(String path) {
		read(ExcelUtils.createWorkbook(new File(path)));
	}
	
	public void from(File file) {
		read(ExcelUtils.createWorkbook(file));
	}
	
	public void from(InputStream inputStream) {
		read(ExcelUtils.createWorkbook(inputStream));
	}
	
	public void read(Workbook workbook){
		Assert.notNull(workbook, "workbook can not be null");
		try {
			int sheetCount = workbook.getNumberOfSheets();
			Sheet sheet = null;
			for(int sheetIndex=0; sheetIndex<sheetCount; sheetIndex++){
				sheet = workbook.getSheetAt(sheetIndex);
				if(sheet.getPhysicalNumberOfRows()<1)
					continue;
				
				for(SheetStreamReader reader : sheetReaders) {
					if (reader.match(sheetIndex)) {
						reader.onRead(sheet, sheetIndex);
					}
				}
			}
		}catch (Exception e) {
			throw ExcelUtils.wrapAsUnCheckedException("read excel file error.", e);
		}
	}
	

	public static class ExcelStreamReaderBuilder {
		private List<SheetStreamReader> sheetReaders = new ArrayList<>();
		public ExcelStreamReaderBuilder addSheetReader(SheetStreamReader sheetReader) {
			this.sheetReaders.add(sheetReader);
			return this;
		}
		/***
		 * from 0
		 * 
		 * @author weishao zeng
		 * @param fromIndex
		 * @return
		 */
		public SheetStreamReaderBuilder readSheet(int fromIndex) {
			SheetStreamReaderBuilder sheetBuilder = new SheetStreamReaderBuilder(this);
			return sheetBuilder.from(fromIndex).to(fromIndex);
		}
		public void from(String path) {
			build().from(path);
		}
		public void from(InputStream inputStream) {
			build().from(inputStream);
		}
		public void from(File file) {
			build().from(file);
		}
		public ExcelStreamReader build() {
			ExcelStreamReader reader = new ExcelStreamReader(sheetReaders);
			return reader;
		}
	}
	
	public static class SheetStreamReaderBuilder {
		int fromIndex;
		int toIndex;
		private List<RowStreamReader> rowReaders = new ArrayList<>();
		private ExcelStreamReaderBuilder parentBuilder;
		
		public SheetStreamReaderBuilder(ExcelStreamReaderBuilder parentBuilder) {
			this.parentBuilder = parentBuilder;
		}

		public SheetStreamReaderBuilder from(int startIndex) {
			this.fromIndex = startIndex;
			return this;
		}

		public SheetStreamReaderBuilder to(int toIndex) {
			this.toIndex = toIndex;
			return this;
		}

		public SheetStreamReaderBuilder toEnd() {
			this.toIndex = Integer.MAX_VALUE;
			return this;
		}

		protected SheetStreamReaderBuilder addRowReader(RowStreamReader rowReader) {
			this.rowReaders.add(rowReader);
			return this;
		}
		public ExcelStreamReaderBuilder endSheet() {
			this.check();
			SheetStreamReader sheetReader = new SheetStreamReader(fromIndex, toIndex, rowReaders);
			parentBuilder.addSheetReader(sheetReader);
			this.reset();
			return parentBuilder;
		}
		/***
		 * from 0 
		 * @author weishao zeng
		 * @param startIndex
		 * @return
		 */
		public RowStreamReaderBuilder row(int startIndex) {
			RowStreamReaderBuilder rowBuilder = new RowStreamReaderBuilder(this);
			return rowBuilder.from(startIndex).to(startIndex);
		}
		
		private void check() {
			Assert.state(fromIndex!=-1, "fromIndex not set");
			Assert.state(toIndex!=-1, "toIndex not set");
		}
		
		private void reset() {
			this.fromIndex = -1;
			this.toIndex = -1;
		}
	}

	public static class RowStreamReaderBuilder {
		private int fromIndex;
		private int toIndex;
		private SheetStreamReaderBuilder parentSheetBuilder;
		
		public RowStreamReaderBuilder(SheetStreamReaderBuilder parentSheetBuilder) {
			super();
			this.parentSheetBuilder = parentSheetBuilder;
		}

		public RowStreamReaderBuilder from(int fromIndex) {
			this.fromIndex = fromIndex;
			return this;
		}

		public RowStreamReaderBuilder to(int toIndex) {
			this.toIndex = toIndex;
			return this;
		}

		public RowStreamReaderBuilder toEnd() {
			this.toIndex = Integer.MAX_VALUE;
			return this;
		}
		
		private void check() {
			Assert.state(fromIndex!=-1, "fromIndex not set");
			Assert.state(toIndex!=-1, "toIndex not set");
		}
		
		private void reset() {
			this.fromIndex = -1;
			this.toIndex = -1;
		}
		
		public SheetStreamReaderBuilder onData(RowConsumer rowConsumer) {
			this.check();
			RowStreamReader RowStreamReader = new RowStreamReader(fromIndex, toIndex, rowConsumer);
			this.parentSheetBuilder.addRowReader(RowStreamReader);
			this.reset();
			return parentSheetBuilder;
		}
		public SheetStreamReaderBuilder end() {
			return parentSheetBuilder;
		}
	}
	
	public static class SheetStreamReader {
		private int fromIndex;
		private int toIndex;
		private List<RowStreamReader> rowReaders;
		
		public SheetStreamReader(int fromIndex, int toIndex, List<RowStreamReader> rowReaders) {
			super();
			this.fromIndex = fromIndex;
			this.toIndex = toIndex;
			this.rowReaders = rowReaders;
		}
		public boolean match(int sheetIndex) {
			return fromIndex <= sheetIndex && sheetIndex <= toIndex;
		}
		public void onRead(Sheet sheet, int sheetIndex) {
			int rowCount = sheet.getPhysicalNumberOfRows();
			for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
				for(RowStreamReader reader : rowReaders) {
					Row row = sheet.getRow(rowIndex);
					if (reader.match(rowIndex)) {
						reader.onRead(sheet, sheetIndex, row, rowIndex);
					}
				}
			}
		}
	}
	
	static class RowStreamReader {
		private int fromIndex;
		private int toIndex;
		private RowConsumer rowConsumer;
		public RowStreamReader(int fromIndex, int toIndex, RowConsumer rowConsumer) {
			super();
			this.fromIndex = fromIndex;
			this.toIndex = toIndex;
			this.rowConsumer = rowConsumer;
		}
		public boolean match(int rowIndex) {
			return fromIndex <= rowIndex && rowIndex <= toIndex;
		}
		public void onRead(Sheet sheet, int sheetIndex, Row row, int rowIndex) {
			RowData rowData = new RowData(row, sheetIndex);
			rowConsumer.onRow(rowData, rowIndex);
		}
	}
	public static interface RowConsumer {
		void onRow(RowData row, int rowIndex);
	}
	
	public static class RowData {
		final private Row row;
		final private int sheetIndex;

		public RowData(Row row, int sheetIndex) {
			super();
			this.row = row;
			this.sheetIndex = sheetIndex;
		}
		/***
		 * from 0
		 * 
		 * @author weishao zeng
		 * @param cellnum
		 * @return
		 */
		public Cell getCell(int cellnum) {
			return row.getCell(cellnum);
		}
		public Object getCellValue(int cellnum) {
			Cell cell = getCell(cellnum);
			return ExcelUtils.getCellValue(cell);
		}
		public String getString(int cellnum) {
			return getCellValue(cellnum, String.class);
		}
		public Integer getInt(int cellnum) {
			return getCellValue(cellnum, Integer.class);
		}
		public <T> T getCellValue(int cellnum, Class<T> clazz) {
			return getCellValue(cellnum, clazz, null);
		}
		public <T> T getCellValue(int cellnum, Class<T> clazz, T def) {
			Object value = getCellValue(cellnum);
			return Types.asValue(value, clazz, def);
		}
		public Row getRow() {
			return row;
		}
		public int getSheetIndex() {
			return sheetIndex;
		}
	}
}

