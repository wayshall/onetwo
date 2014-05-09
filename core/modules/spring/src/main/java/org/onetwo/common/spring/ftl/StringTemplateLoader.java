package org.onetwo.common.spring.ftl;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Map;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;

import freemarker.cache.TemplateLoader;

public class StringTemplateLoader implements TemplateLoader {

	private static final String DEFAULT_TEMPLATE_KEY = "_default_template_key";

	private Map<String, Object> templates = LangUtils.newHashMap();

	public StringTemplateLoader() {
	}

	public void addTemplate(String name, String template) {
		if (StringUtils.isBlank(name) || StringUtils.isBlank(template)) {
			return;
		}

		if (!templates.containsKey(name)) {
			templates.put(name, template);
		}
	}

	public void closeTemplateSource(Object templateSource) throws IOException {

	}

	public Object findTemplateSource(String name) throws IOException {
		if (StringUtils.isBlank(name)) {
			name = DEFAULT_TEMPLATE_KEY;
		}
		return templates.get(name);
	}

	public long getLastModified(Object templateSource) {
		return 0;
	}

	public Reader getReader(Object templateSource, String encoding) throws IOException {
		return new StringReader((String) templateSource);
	}
}
