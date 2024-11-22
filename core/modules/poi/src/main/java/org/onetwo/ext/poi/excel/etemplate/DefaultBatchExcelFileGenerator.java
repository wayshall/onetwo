package org.onetwo.ext.poi.excel.etemplate;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.onetwo.common.file.FileUtils;
import org.onetwo.ext.poi.excel.reader.HashMapRowMapper;
import org.onetwo.ext.poi.excel.reader.WorkbookReader;
import org.onetwo.ext.poi.excel.reader.WorkbookReaderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wayshall
 * <br/>
 */
public class DefaultBatchExcelFileGenerator {
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private File templateFile;
	private ETemplateContext templateContext;
	private File dataFile;

	public void generate(File outDir) {
		
		WorkbookReader reader = WorkbookReaderFactory.createWorkbookByMapper(new HashMapRowMapper());
		List<Map<String, Object>> dataMapList = reader.readFirstSheet(dataFile);
		String fileName = FileUtils.getFileNameWithoutExt(templateFile.getName());
		String ext = FileUtils.getExtendName(templateFile.getName(), true);
		
		int index = 0;
		for (Map<String, Object> rowMap : dataMapList) {
			ETemplateContext tempalteContext = getTemplateContext().clone();
			String generatedName = FileUtils.convertDir(outDir.getPath()) + fileName + "-" + index + ext;
			ExcelTemplateEngineer templateEngineer = new DefaultExcelTemplateEngineer();
			tempalteContext.putAll(rowMap);
			templateEngineer.generate(templateFile, generatedName, tempalteContext);
			index++;
		}
	}

	public File getTemplateFile() {
		return templateFile;
	}

	public void setTemplateFile(File templateFile) {
		this.templateFile = templateFile;
	}

	public ETemplateContext getTemplateContext() {
		if (templateContext==null) {
			templateContext = ETemplateContext.newContext();
		}
		return templateContext;
	}

	public void setTemplateContext(ETemplateContext templateContext) {
		this.templateContext = templateContext;
	}

	public void setDataFile(File dataFile) {
		this.dataFile = dataFile;
	}

}
