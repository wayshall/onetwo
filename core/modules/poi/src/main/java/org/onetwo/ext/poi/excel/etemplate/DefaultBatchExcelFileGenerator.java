package org.onetwo.ext.poi.excel.etemplate;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.utils.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author wayshall
 * <br/>
 */
public class DefaultBatchExcelFileGenerator implements InitializingBean {
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private File templateFile;
	private ETemplateContext templateContext;
	/***
	 * 相当于主键列，用于判断是否空行，且生成excel文件命名的一部分
	 */
	private String keyName;


	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(templateFile, "templateFile can not be null!");
	}
	
	public void generate(String outDirPath, TemplateExcelDataProvider provider) {
		List<Map<String, Object>> dataMapList = provider.readDataList();
		generate(outDirPath, dataMapList);
	}
	
	public void generate(String outDirPath, List<Map<String, Object>> dataMapList) {
		File outDir = new File(FileUtils.convertDir(outDirPath));
		FileUtils.makeDirs(outDir, false);
		
		String fileName = FileUtils.getFileNameWithoutExt(templateFile.getName());
		String ext = FileUtils.getExtendName(templateFile.getName(), true);
		

		ExcelTemplateEngineer templateEngineer = new DefaultExcelTemplateEngineer();
		int index = 0;
		String filePartName = null;
		for (Map<String, Object> rowMap : dataMapList) {
			if (StringUtils.isNotBlank(keyName)) {
				// 如果配置了keyName
				String key = org.onetwo.common.utils.StringUtils.trimToEmpty(rowMap.get(keyName));
				if (StringUtils.isBlank(key)) {
					// key为空，忽略
					continue;
				}
				filePartName = key;
			} else {
				filePartName = String.valueOf(index);
			}
			ETemplateContext tempalteContext = getTemplateContext().clone();
			String generatedName = FileUtils.convertDir(outDir.getPath()) + fileName + "-" + filePartName + ext;
			tempalteContext.putAll(rowMap);
			templateEngineer.generate(templateFile, generatedName, tempalteContext);
			if (logger.isInfoEnabled()) {
				logger.info("generated file index: {}", index);
			}
			index++;
		}
		if (logger.isInfoEnabled()) {
			logger.info("generated file amount: {}", index);
		}
	}

	public File getTemplateFile() {
		return templateFile;
	}

	public void setTemplateFile(File templateFile) {
		this.templateFile = templateFile;
	}

	public void setTemplateFilePath(String templateFilePath) {
		Assert.hasText(templateFilePath, "templateFilePath must has text");
		this.templateFile = new File(templateFilePath);
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

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

}
