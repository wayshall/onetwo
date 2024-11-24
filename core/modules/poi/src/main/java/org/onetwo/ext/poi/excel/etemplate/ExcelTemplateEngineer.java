package org.onetwo.ext.poi.excel.etemplate;

import java.io.File;
import java.io.OutputStream;

/**
 * @author wayshall
 * <br/>
 */
public interface ExcelTemplateEngineer {

	/****
	 * 根据模版文件，生成新的excel文件
	 * @author wayshall
	 * @param templateFile excel模版文件
	 * @param generatedPath 生成的新文件
	 * @param context
	 */
	void generate(File templateFile, String generatedPath, ETemplateContext context);
	void generate(String templateFile, String generatedPath, ETemplateContext context);

	void generate(File templateFile, OutputStream out, ETemplateContext context);

}