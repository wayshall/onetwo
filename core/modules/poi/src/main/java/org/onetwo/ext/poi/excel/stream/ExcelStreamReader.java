package org.onetwo.ext.poi.excel.stream;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.onetwo.common.convert.Types;
import org.onetwo.common.date.DateUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.ext.poi.excel.reader.image.DelegateSheetImageReader;
import org.onetwo.ext.poi.utils.ExcelUtils;
import org.slf4j.Logger;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author weishao zeng
 * <br/>
 */
public class ExcelStreamReader {
	private static final DelegateSheetImageReader imageDelegateReader = new DelegateSheetImageReader();
	private List<SheetStreamReader<?>> sheetReaders;
	
	private ExcelStreamReader(List<SheetStreamReader<?>> sheetReaders) {
		super();
		this.sheetReaders = sheetReaders;
	}
	
	public void from(String path, SheeDataModelConsumer consumer) {
		read(ExcelUtils.createWorkbook(new File(path)), consumer);
	}
	
	public void from(File file, SheeDataModelConsumer consumer) {
		read(ExcelUtils.createWorkbook(file), consumer);
	}
	
	public void from(InputStream inputStream, SheeDataModelConsumer consumer) {
		read(ExcelUtils.createWorkbook(inputStream), consumer);
	}
	
	public void read(Workbook workbook, SheeDataModelConsumer consumer){
		Assert.notNull(workbook, "workbook can not be null");
		try {
			int sheetCount = workbook.getNumberOfSheets();
			Sheet sheet = null;
			List<? extends PictureData> picDatas = workbook.getAllPictures();
			for(int sheetIndex=0; sheetIndex<sheetCount; sheetIndex++){
				sheet = workbook.getSheetAt(sheetIndex);
				if(sheet.getPhysicalNumberOfRows()<1)
					continue;
				
				for(SheetStreamReader<?> reader : sheetReaders) {
					if (reader.match(sheetIndex)) {
						Object dataInst = reader.onRead(sheet, sheetIndex);
						if (consumer!=null && dataInst!=null) {
							consumer.onSheetReadCompleted(dataInst);
						}
					}
				}
			}
		}catch (Exception e) {
			throw ExcelUtils.wrapAsUnCheckedException("read excel file error.", e);
		}
	}
	
	public static interface SheeDataModelConsumer {
		/****
		 * 当整个sheet读取完毕时，触发此回调
		 * @author weishao zeng
		 * @param dataInst
		 */
		void onSheetReadCompleted(Object dataInst);
	}
	

	public static class ExcelStreamReaderBuilder {
		private List<SheetStreamReader<?>> sheetReaders = new ArrayList<>();
		
		public ExcelStreamReaderBuilder addSheetReader(SheetStreamReader<?> sheetReader) {
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
		public <T> SheetStreamReaderBuilder<T> readSheet(int fromIndex) {
			return readSheet(fromIndex, null);
		}
		public <T> SheetStreamReaderBuilder<T> readSheet(int fromIndex, Class<T> dataModel) {
			SheetStreamReaderBuilder<T> sheetBuilder = new SheetStreamReaderBuilder<T>(this);
			return sheetBuilder.from(fromIndex).to(fromIndex).dataModel(dataModel);
		}
		public void from(String path, SheeDataModelConsumer consumer) {
			build().from(path, consumer);
		}
		public void from(InputStream inputStream, SheeDataModelConsumer consumer) {
			build().from(inputStream, consumer);
		}
		public void from(File file, SheeDataModelConsumer consumer) {
			build().from(file, consumer);
		}
		public void from(MultipartFile dataFile) {
			try {
				build().from(dataFile.getInputStream(), null);
			} catch (IOException e) {
				throw new BaseException("import data file error: " + e.getMessage(), e);
			}
		}
		
		public void from(MultipartFile dataFile, boolean transtoFile) {
			if (!transtoFile) {
				from(dataFile);
			}
			String tempPath = FileUtils.getJavaIoTmpdir(true) + LangUtils.randomUUID() + "." + FileUtils.getExtendName(dataFile.getOriginalFilename());
			Logger logger = JFishLoggerFactory.getCommonLogger();
			if (logger.isInfoEnabled()) {
				logger.info("MultipartFile transfer to dest file: {}", tempPath);
			}
			File dest = new File(tempPath);
			try {
				dataFile.transferTo(dest);
				build().from(dest, null);
			} catch (IOException e) {
				throw new BaseException("import data file error: " + e.getMessage(), e);
			}
		}
		
		public ExcelStreamReader build() {
			ExcelStreamReader reader = new ExcelStreamReader(sheetReaders);
			return reader;
		}
	}
	
	public static class SheetStreamReaderBuilder<T> {
		int fromIndex;
		int toIndex;
		private List<RowStreamReader<T>> rowReaders = new ArrayList<>();
		private ExcelStreamReaderBuilder parentBuilder;
		private Class<T> dataModel;
		
		public SheetStreamReaderBuilder(ExcelStreamReaderBuilder parentBuilder) {
			this.parentBuilder = parentBuilder;
		}
		
		public SheetStreamReaderBuilder<T> dataModel(Class<T> dataModel) {
			this.dataModel = dataModel;
			return this;
		}
		
		public SheetStreamReaderBuilder<T> from(int startIndex) {
			this.fromIndex = startIndex;
			return this;
		}


		public SheetStreamReaderBuilder<T> to(int toIndex) {
			this.toIndex = toIndex;
			return this;
		}

		public SheetStreamReaderBuilder<T> toEnd() {
			this.toIndex = Integer.MAX_VALUE;
			return this;
		}

		protected SheetStreamReaderBuilder<T> addRowReader(RowStreamReader<T> rowReader) {
			this.rowReaders.add(rowReader);
			return this;
		}
		public ExcelStreamReaderBuilder endSheet() {
			this.check();
			SheetStreamReader<T> sheetReader = new SheetStreamReader<T>(fromIndex, toIndex, rowReaders, dataModel);
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
		

		public class RowStreamReaderBuilder {
			private int fromIndex;
			private int toIndex;
			private SheetStreamReaderBuilder<T> parentSheetBuilder;
			
			public RowStreamReaderBuilder(SheetStreamReaderBuilder<T> parentSheetBuilder) {
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
			
			/***
			 * 添加行消费者RowConsumer
			 * @author weishao zeng
			 * @param rowConsumer
			 * @return
			 */
			public SheetStreamReaderBuilder<T> onData(RowConsumer<T> rowConsumer) {
				this.check();
				RowStreamReader<T> RowStreamReader = new RowStreamReader<>(fromIndex, toIndex, rowConsumer);
				this.parentSheetBuilder.addRowReader(RowStreamReader);
				this.reset();
				return parentSheetBuilder;
			}
			public SheetStreamReaderBuilder<T> end() {
				return parentSheetBuilder;
			}
		}

	}

	static public interface RowConsumer<T> {
		/***
		 * 读取行数据时触发此回调
		 * 若有空行，需要自行判断
		 * @author weishao zeng
		 * @param dataModel
		 * @param row
		 * @param rowIndex
		 */
		void onRow(T dataModel, RowData row, int rowIndex);
	}
	
	public static class SheetStreamReader<T> {
		private int fromIndex;
		private int toIndex;
		private List<RowStreamReader<T>> rowReaders;
		private Class<T> dataModel;
		
		public SheetStreamReader(int fromIndex, int toIndex, List<RowStreamReader<T>> rowReaders, Class<T> dataModel) {
			super();
			this.fromIndex = fromIndex;
			this.toIndex = toIndex;
			this.rowReaders = rowReaders;
			this.dataModel = dataModel;
		}
		public boolean match(int sheetIndex) {
			return fromIndex <= sheetIndex && sheetIndex <= toIndex;
		}
		private T createDataModel() {
			T dataModelInst = null;
			if (dataModel!=null) {
				dataModelInst = ReflectUtils.newInstance(dataModel);
			}
			return dataModelInst;
		}
		public T onRead(Sheet sheet, int sheetIndex) {
			SheetData sheetData = new SheetData(sheet, sheetIndex);
			T dataModelInst = createDataModel();
			int rowCount = sheet.getPhysicalNumberOfRows();
			for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
				for(RowStreamReader<T> reader : rowReaders) {
					Row row = sheet.getRow(rowIndex);
					if (row==null) {
						// 忽略空行
						continue;
					}
					if (reader.match(rowIndex)) {
						reader.onRead(dataModelInst, sheetData, row, rowIndex);
					}
				}
			}
			return dataModelInst;
		}
		
		/***
		 * 此方法有问题，尚未能读取图片位置
		 * @author weishao zeng
		 * @param sheetData
		 */
		protected void initPictureDatas(SheetData sheetData) {
			Map<Integer, Map<Integer, PictureData>> pictureDatas = imageDelegateReader.readPictureDatas(sheetData.getSheet());
			sheetData.setPictureDatas(pictureDatas);
		}
		
	}
	
	public static class SheetData {
		final private Sheet sheet;
		final private int sheetIndex;
		private Map<Integer, Map<Integer, PictureData>> pictureDatas;
		public SheetData(Sheet sheet, int sheetIndex) {
			super();
			this.sheet = sheet;
			this.sheetIndex = sheetIndex;
		}
		public Sheet getSheet() {
			return sheet;
		}
		public int getSheetIndex() {
			return sheetIndex;
		}
		public PictureData getPictureDatas(int rowIndex, int colIndex) {
			Map<Integer, PictureData> row = pictureDatas.get(rowIndex);
			if (row==null) {
				return null;
			}
			return row.get(colIndex);
		}
		void setPictureDatas(Map<Integer, Map<Integer, PictureData>> pictureDatas) {
			this.pictureDatas = pictureDatas;
		}
	}
	
	static class RowStreamReader<T> {
		private int fromIndex;
		private int toIndex;
		private RowConsumer<T> rowConsumer;
		public RowStreamReader(int fromIndex, int toIndex, RowConsumer<T> rowConsumer) {
			super();
			this.fromIndex = fromIndex;
			this.toIndex = toIndex;
			this.rowConsumer = rowConsumer;
		}
		public boolean match(int rowIndex) {
			return fromIndex <= rowIndex && rowIndex <= toIndex;
		}
		public void onRead(T dataModel, SheetData sheet, Row row, int rowIndex) {
			RowData rowData = new RowData(sheet, sheet.getSheetIndex(), row, rowIndex);
			rowConsumer.onRow(dataModel, rowData, rowIndex);
		}
	}
	
	public static class RowData {
		final private SheetData sheet;
		final private Row row;
		final private int sheetIndex;
		final private int rowIndex;

		public RowData(SheetData sheet, int sheetIndex, Row row, int rowIndex) {
			super();
			this.sheet = sheet;
			this.row = row;
			this.sheetIndex = sheetIndex;
			this.rowIndex = rowIndex;
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
		public PictureData getPicture(int cellnum) {
			return this.sheet.getPictureDatas(rowIndex, cellnum);
		}
		public Object getCellValue(int cellnum) {
			Cell cell = getCell(cellnum);
			return ExcelUtils.getCellValue(cell);
		}
		public String getString(int cellnum) {
			Cell cell = getCell(cellnum);
			return (String)ExcelUtils.getCellValue(cell, true);
		}
		public Integer getInt(int cellnum) {
			return getCellValue(cellnum, Integer.class);
		}
		public Long getLong(int cellnum) {
			return getCellValue(cellnum, Long.class);
		}
		public Double getDouble(int cellnum) {
			return getCellValue(cellnum, Double.class);
		}
		public Float getFloat(int cellnum) {
			return getCellValue(cellnum, Float.class);
		}
		public Boolean getBoolean(int cellnum) {
			return getCellValue(cellnum, Boolean.class);
		}
		public <T> T getCellValue(int cellnum, Class<T> clazz) {
			return getCellValue(cellnum, clazz, null);
		}
		public Date getDate(int cellnum, String pattern) {
			String value = getString(cellnum);
			Date date = DateUtils.parseByPatterns(value, pattern);
			return date;
		}
		
		public <T extends Enum<?>> T getEnum(int cellnum, Class<T> enumClass) {
			return getEnum(cellnum, enumClass, null);
		}
		
		public <T extends Enum<?>> T getEnum(int cellnum, Class<T> enumClass, T def) {
			String value = getString(cellnum);
			if (value==null) {
				return def;
			}
			T enumValue = Types.convertValue(value, enumClass, def);
			return enumValue;
		}
		public <T> T getCellValue(int cellnum, Class<T> clazz, T def) {
			Object value = getCellValue(cellnum);
			if (value==null) {
				return def;
			}
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

