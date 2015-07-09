package org.onetwo.common.db.generator;

import java.io.File;

public interface TemplateEngine {

	public void afterPropertiesSet();

	public String generateString(Object context, String templatePath);

	public File generateFile(Object context, String templatePath,
			String targetPath);

}