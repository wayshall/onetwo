package org.onetwo.ext.poi.excel.etemplate;

import java.util.List;
import java.util.Map;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.ext.poi.excel.reader.HashMapRowMapper;
import org.onetwo.ext.poi.excel.reader.WorkbookReader;
import org.onetwo.ext.poi.excel.reader.WorkbookReaderFactory;

/**
 * @author wayshall
 * <br/>
 */
public class SimpleBatchExceGenerator {
	public static SimpleBatchExceGenerator create() {
		return new SimpleBatchExceGenerator();
	}
	private String templateFilePath;

	private String dataFilePath;
	
	/****
	 * 标题行，默认为第一行
	 */
	private int dataFileTitleRowIndex = 0;
	/****
	 * 数据开始行，默认为第二行
	 */
	private int dataFileDataStartRowIndex = 1;
	
	private int dataFileSheetIndex = 0;
	
	private String outDirPath;
	
	private ETemplateContext context;
	
	private String keyName;
	
	private void checkArgs() {
		Assert.hasText(templateFilePath, "templateFilePath must has text!");
		Assert.hasText(dataFilePath, "dataFilePath must has text!");
		Assert.hasText(outDirPath, "outDirPath must has text!");
	}
	
	public void generate(int count) {
		this.checkArgs();
		DefaultBatchExcelFileGenerator bg = new DefaultBatchExcelFileGenerator();
		bg.setTemplateContext(context);
		
		bg.setTemplateFilePath(templateFilePath);
		bg.setKeyName(keyName);
		
		bg.generate(outDirPath, readByCount(count));
	}
	
	private List<Map<String, Object>> readByCount(int count) {
		if (count==0) {
			throw new BaseException("count can not be 0");
		}
		WorkbookReader reader = WorkbookReaderFactory.createWorkbookByMapper(new HashMapRowMapper(dataFileTitleRowIndex, dataFileDataStartRowIndex));
		Map<String, List<Object>> sheetMap = reader.readData(dataFilePath, dataFileSheetIndex, 1);
		List<Map<String, Object>> dataList = LangUtils.getFirst(sheetMap);
		if (count<0 || count==dataList.size()) {
			return dataList;
		}
		int end = 0 + count;
		if (end > dataList.size()) {
			end = dataList.size();
		}
		return dataList.subList(0, end);
	}

	public SimpleBatchExceGenerator templateFilePath(String templateFilePath) {
		this.templateFilePath = templateFilePath;
		return this;
	}

	public SimpleBatchExceGenerator dataFilePath(String dataFilePath) {
		this.dataFilePath = dataFilePath;
		return this;
	}

	public SimpleBatchExceGenerator dataFileTitleRowIndex(int dataFileTitleRowIndex) {
		this.dataFileTitleRowIndex = dataFileTitleRowIndex;
		return this;
	}

	public SimpleBatchExceGenerator dataFileDataStartRowIndex(int dataFileDataStartRowIndex) {
		this.dataFileDataStartRowIndex = dataFileDataStartRowIndex;
		return this;
	}

	public SimpleBatchExceGenerator dataFileSheetIndex(int dataFileSheetIndex) {
		this.dataFileSheetIndex = dataFileSheetIndex;
		return this;
	}

	public SimpleBatchExceGenerator outDirPath(String outDirPath) {
		this.outDirPath = outDirPath;
		return this;
	}

	public SimpleBatchExceGenerator context(ETemplateContext context) {
		this.context = context;
		return this;
	}
	public SimpleBatchExceGenerator keyName(String keyName) {
		this.keyName = keyName;
		return this;
	}
	
}
