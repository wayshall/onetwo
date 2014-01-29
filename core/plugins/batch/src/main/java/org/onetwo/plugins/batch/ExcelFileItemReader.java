package org.onetwo.plugins.batch;

import org.apache.poi.ss.usermodel.Workbook;
import org.onetwo.common.excel.ExcelBufferReader;
import org.onetwo.common.excel.ExcelUtils;
import org.onetwo.common.excel.RowMapperWorkbookBufferReader;
import org.onetwo.common.excel.SSFRowMapper;
import org.onetwo.common.log.MyLoggerFactory;
import org.slf4j.Logger;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;


public class ExcelFileItemReader<T> extends AbstractItemCountingItemStreamItemReader<T> implements ResourceAwareItemReaderItemStream<T>, InitializingBean {

	private static final Logger logger = MyLoggerFactory.getLogger(ExcelFileItemReader.class);
	
	private Workbook workbook;
//	private Sheet sheet;
	private ExcelBufferReader<T> reader;
	private SSFRowMapper<T> rowMapper;
	private boolean strict = true;
	private Resource resource;
	private boolean noInput = false;
//	private int linesToSkip = 0;
	private int lineCount = 0;
	
	public ExcelFileItemReader(){
		setName(ClassUtils.getShortName(FlatFileItemReader.class));
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(rowMapper, "rowMapper is required");
	}
	

	@Override
	protected T doRead() throws Exception {
		if (noInput) {
			return null;
		}
		T data = this.reader.read();
		lineCount++;
		return data;
	}

	@Override
	protected void doClose() throws Exception {
		lineCount = 0;
		if (reader != null) {
			reader = null;
		}
	}

	@Override
	protected void doOpen() throws Exception {
		Assert.notNull(resource, "Input resource must be set");

		noInput = true;
		if (!resource.exists()) {
			if (strict) {
				throw new IllegalStateException("Input resource must exist (reader is in 'strict' mode): " + resource);
			}
			logger.warn("Input resource does not exist " + resource.getDescription());
			return;
		}

		if (!resource.isReadable()) {
			if (strict) {
				throw new IllegalStateException("Input resource must be readable (reader is in 'strict' mode): "
						+ resource);
			}
			logger.warn("Input resource is not readable " + resource.getDescription());
			return;
		}

		this.workbook = ExcelUtils.readWorkbook(resource.getInputStream());
		
//		int sheetIndex = 0;
//		sheet = workbook.getSheetAt(sheetIndex);
//		reader = new RowMapperSheetBufferReader<T>(sheet, sheetIndex, this.rowMapper);
		reader = new RowMapperWorkbookBufferReader<T>(workbook, this.rowMapper);
		reader.initReader();
		/*for (int i = 0; i < linesToSkip; i++) {
			readRow();
		}*/
//		this.linesToSkip = reader.getDataRowStartIndex();
		noInput = false;
	}

	public void setRowMapper(SSFRowMapper<T> rowMapper) {
		this.rowMapper = rowMapper;
	}


	@Override
	public void setResource(Resource resource) {
		this.resource = resource;
	}

}
