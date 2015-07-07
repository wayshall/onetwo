package org.onetwo.common.db.generator;

import java.io.File;

public interface TemplateEngine {

	public void afterPropertiesSet();

	public String generateString(GenerateContext context, String templatePath);

	public File generateFile(GenerateContext context, String templatePath,
			String targetPath);

}