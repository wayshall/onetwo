package org.onetwo.ext.poi.excel.generator;

import java.util.Map;

import org.onetwo.ext.poi.excel.interfaces.TemplateGenerator;

public interface ModelGeneratorFactory {
	public TemplateGenerator create(WorkbookModel workbook, Map<String, Object> context);
}
