package org.onetwo.common.spring.ftl;

import org.onetwo.common.utils.Assert;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import freemarker.cache.TemplateLoader;


public class DirsFreemarkerTemplateConfigurer extends AbstractFreemarkerTemplateConfigurer {


	private ResourceLoader resourceLoader = new DefaultResourceLoader();
	private String[] templatePaths;


	protected TemplateLoader getTempateLoader(){
		Assert.notEmpty(templatePaths);
		TemplateLoader loader = FtlUtils.getTemplateLoader(resourceLoader, templatePaths);
		return loader;
	}

	public String[] getTemplatePaths() {
		return templatePaths;
	}

	public void setTemplatePaths(String... templatePaths) {
		this.templatePaths = templatePaths;
	}
}
