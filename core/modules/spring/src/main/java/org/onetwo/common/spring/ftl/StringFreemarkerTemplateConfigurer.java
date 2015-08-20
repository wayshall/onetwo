package org.onetwo.common.spring.ftl;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.file.FileUtils;

import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;

/****
 * 动态模板
 * @author weishao
 *
 */
public class StringFreemarkerTemplateConfigurer extends DynamicFreemarkerTemplateConfigurer {

	private DynamicTemplateLoader dynamicTemplateLoader;
	
	public StringFreemarkerTemplateConfigurer(){
		this((Map<String, StringTemplate>)null);
	}

	public StringFreemarkerTemplateConfigurer(Map<String, StringTemplate> mapper) {
		super();
		setTemplateProvider(new DefaultStringTemplateProvider(mapper));
		this.dynamicTemplateLoader = new DynamicTemplateLoader(getTemplateProvider());
	}

	public StringFreemarkerTemplateConfigurer(DefaultStringTemplateProvider templateProvider) {
		super();
		setTemplateProvider(templateProvider);
		this.dynamicTemplateLoader = new DynamicTemplateLoader(getTemplateProvider());
	}

	public DefaultStringTemplateProvider getTemplateProvider() {
		return (DefaultStringTemplateProvider)super.getTemplateProvider();
	}

	@Override
	protected void buildConfigration(Configuration cfg) {
		cfg.setTagSyntax(Configuration.SQUARE_BRACKET_TAG_SYNTAX);
	}


	public StringFreemarkerTemplateConfigurer putTemplate(String name, String content){
		getTemplateProvider().getMapper().put(name, new StringWraperTemplate(content));
		return this;
	}

	public boolean isMapped(String name){
		return getTemplateProvider().getMapper().containsKey(name);
	}

	public StringFreemarkerTemplateConfigurer putTemplate(String name, File file){
		getTemplateProvider().getMapper().put(name, new FileWraperTemplate(file));
		return this;
	}

	public StringFreemarkerTemplateConfigurer putTemplate(String name, StringTemplate st){
		getTemplateProvider().getMapper().put(name, st);
		return this;
	}

	protected TemplateLoader getTempateLoader(){
		return dynamicTemplateLoader;
	}
	
	public class DefaultStringTemplateProvider implements StringTemplateProvider{
		
		private final Map<String, StringTemplate> mapper;
		private boolean ignoreLocal = true;
		
		private DefaultStringTemplateProvider(Map<String, StringTemplate> mapper) {
			super();
			this.mapper = LangUtils.isEmpty(mapper)?new HashMap<String, StringTemplate>():mapper;
		}

		@Override
		public String getTemplateContent(String name) {
			if(ignoreLocal){
				String path = FileUtils.getFileNameWithoutExt(name);
				if(path.endsWith(Locale.CHINA.toString()) || path.endsWith(Locale.CHINESE.toString())){
					return null;
				}
			}
			StringTemplate p = mapper.get(name);
			if(p==null)
				throw new BaseException("no template found: " + name);
			return p.getTemplate();
		}

		public Map<String, StringTemplate> getMapper() {
			return mapper;
		}


	}
	
	public static interface StringTemplate {
		String getTemplate();
	}
	
	public static class StringWraperTemplate implements StringTemplate {

		private final String content;
		
		private StringWraperTemplate(String content) {
			super();
			this.content = content;
		}

		@Override
		public String getTemplate() {
			return content;
		}
		
	}
	
	public static class FileWraperTemplate implements StringTemplate {

		private final File file;
		private final String charset;
		

		private FileWraperTemplate(File file) {
			this(file, LangUtils.UTF8);
		}
		private FileWraperTemplate(File file, String charset) {
			super();
			this.file = file;
			this.charset = charset;
		}

		@Override
		public String getTemplate() {
			return FileUtils.readAsString(file, charset);
		}
		
	}

}
