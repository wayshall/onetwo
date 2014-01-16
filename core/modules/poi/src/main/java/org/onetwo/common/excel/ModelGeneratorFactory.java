package org.onetwo.common.excel;

import java.util.Map;

import org.onetwo.common.interfaces.TemplateGenerator;

public interface ModelGeneratorFactory {
	public TemplateGenerator create(WorkbookModel workbook, Map<String, Object> context);
}
