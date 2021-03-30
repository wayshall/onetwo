package org.onetwo.ext.poi.excel.stream;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.onetwo.common.convert.Types;
import org.onetwo.common.date.DateUtils;
import org.onetwo.common.date.Dates;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.ext.poi.utils.ExcelUtils;
import org.springframework.util.Assert;

/**
 * @author weishao zeng
 * <br/>
 */

public class SheetStreamReaderBuilder<T> {
//	private static final DelegateSheetImageReader imageDelegateReader = new DelegateSheetImageReader();
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
		public T onRead(SheetData sheetData, int sheetIndex) {
			T dataModelInst = createDataModel();
//			int rowCount = sheet.getPhysicalNumberOfRows();
			int rowIndex = 0;
			for (Row row : sheetData.getSheet()) {
//				for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
				for(RowStreamReader<T> reader : rowReaders) {
//					Row row = sheet.getRow(rowIndex);
					if (row==null) {
						// 忽略空行
						continue;
					}
					if (reader.match(rowIndex)) {
						reader.onRead(dataModelInst, sheetData, row, rowIndex);
					}
				}
				rowIndex++;
			}
			return dataModelInst;
		}
		
		/***
		 * 此方法有问题，尚未能读取图片位置
		 * @author weishao zeng
		 * @param sheetData
		 
		protected void initPictureDatas(SheetData sheetData) {
			Map<Integer, Map<Integer, PictureData>> pictureDatas = imageDelegateReader.readPictureDatas(sheetData.getSheet());
			sheetData.setPictureDatas(pictureDatas);
		}*/
		
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
	
	public static class SheetData {
		final private Sheet sheet;
		final private int sheetIndex;
		private Map<Integer, Map<Integer, PictureData>> pictureDatas;
		private boolean canConverToStringValue = true;
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
		public boolean isCanConverToStringValue() {
			return canConverToStringValue;
		}
		public void setCanConverToStringValue(boolean canConverToStringValue) {
			this.canConverToStringValue = canConverToStringValue;
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
		
		/***
		 * @author weishao zeng
		 * @param cellnum base 0
		 * @return
		 */
		public String getString(int cellnum) {
			Cell cell = getCell(cellnum);
			return (String)ExcelUtils.getCellValue(cell, sheet.isCanConverToStringValue());
		}
		
		/***
		 * 获取并解释第cellnum列的值为Integer类型
		 * 若该列无法解释为数字，则返回null
		 * @author weishao zeng
		 * @param cellnum
		 * @return
		 */
		public Integer getInt(int cellnum) {
			return getCellValue(cellnum, Integer.class);
		}
		
		/****
		 * 获取并解释第cellnum列的值为Integer类型.
		 * 若该列为空则返回默认值，若队列的解释器解释时出错，则抛错
		 * @author weishao zeng
		 * @param cellnum
		 * @param def
		 * @return
		 */
		public int getInt(int cellnum, int def) {
			Object value = getCellValue(cellnum);
			if (value==null || StringUtils.isBlank(value.toString())) {
				return def;
			}
			Integer intValue = Types.asInteger(value);
			if (intValue==null) {
				return def;
			}
			return intValue;
		}
		
		public Long getLong(int cellnum) {
			return getCellValue(cellnum, Long.class);
		}

		public long getLong(int cellnum, long def) {
			Object value = getCellValue(cellnum);
			if (value==null || StringUtils.isBlank(value.toString())) {
				return def;
			}
			Long longValue = Types.asLong(value);
			if (longValue==null) {
				return def;
			}
			return longValue;
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
		
		/****
		 * 
		 * @author weishao zeng
		 * @param cellnum
		 * @param pattern 当cell为日期格式时，此参数无效
		 * @return
		 */
		public Date getDate(int cellnum, String pattern) {
			Cell cell = getCell(cellnum);
            boolean isDateCell = DateUtil.isCellDateFormatted(cell);
            Date date;
            if (isDateCell) {
            	date = cell.getDateCellValue();
            } else {
				String value = getString(cellnum);
				date = DateUtils.parseByPatterns(value, pattern);
            }
			return date;
		}
		
		public LocalDate getLocalDate(int cellnum, String pattern) {
			String value = getString(cellnum);
			LocalDate date = Dates.parseLocalDate(value, pattern);
			return date;
		}
		
		public LocalDateTime getLocalDateTime(int cellnum, String pattern) {
			String value = getString(cellnum);
			LocalDateTime date = Dates.parseLocalDateTime(value, pattern);
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
		
		/***
		 * 解释第cellnum列的值为clazz类型的值，若该列为null或者对应的解释器错误，则返回默认值def
		 * @author weishao zeng
		 * @param cellnum
		 * @param clazz
		 * @param def
		 * @return
		 */
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
