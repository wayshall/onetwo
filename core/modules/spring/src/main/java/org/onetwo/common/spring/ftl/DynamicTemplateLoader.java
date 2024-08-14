package org.onetwo.common.spring.ftl;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Locale;

import org.onetwo.common.file.FileUtils;

import freemarker.cache.TemplateLoader;
// 动态模版加载器
public class DynamicTemplateLoader implements TemplateLoader {

	private StringTemplateProvider templateProvidor;
	
	public DynamicTemplateLoader(StringTemplateProvider templateProvidor) {
		this.templateProvidor = templateProvidor;
	}

	public void closeTemplateSource(Object templateSource) throws IOException {
	}
	// 根据名称查找模版
	public Object findTemplateSource(String name) throws IOException {
		String noPostfix = FileUtils.getFileNameWithoutExt(name);
		if(noPostfix.endsWith(Locale.CHINA.toString()) || noPostfix.endsWith(Locale.CHINESE.toString())){
			return null;
		}
		return templateProvidor.getTemplateContent(name);
	}

	public long getLastModified(Object templateSource) {
		return 0;
	}

	public Reader getReader(Object templateSource, String encoding) throws IOException {
		return new StringReader((String) templateSource);
	}
}
