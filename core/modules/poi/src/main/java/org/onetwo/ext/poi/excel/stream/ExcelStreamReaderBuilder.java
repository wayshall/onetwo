package org.onetwo.ext.poi.excel.stream;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.ext.poi.excel.stream.BaseExcelStreamReader.SheeDataModelConsumer;
import org.onetwo.ext.poi.excel.stream.SheetStreamReaderBuilder.SheetStreamReader;
import org.slf4j.Logger;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author weishao zeng
 * <br/>
 */

public class ExcelStreamReaderBuilder {
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
		return build(this.sheetReaders);
	}
	
	protected ExcelStreamReader build(List<SheetStreamReader<?>> sheetReaders) {
		ExcelStreamReader reader = new PoiExcelStreamReader(sheetReaders);
		return reader;
	}
}
