package org.onetwo.common.spring.sql;

import java.util.Map.Entry;

import org.onetwo.common.spring.ftl.AbstractFreemarkerTemplateConfigurer;
import org.onetwo.common.spring.ftl.ForeachDirective;
import org.onetwo.common.utils.propconf.PropertiesNamespaceInfo;

import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;

public class StringTemplateLoaderFileSqlParser<T extends JFishNamedFileQueryInfo> extends AbstractFreemarkerTemplateConfigurer implements FileSqlParser<T> {

//	public static final String QUERY_POSTFIX = ".query";//for ftl
	
	private JFishNamedSqlFileManager<T> sqlManager;
	private StringTemplateLoader templateLoader;
	
	
	public StringTemplateLoaderFileSqlParser(final JFishNamedSqlFileManager<T> sqlm) {
		super();
		this.sqlManager = sqlm;
		
		addDirective(new ForeachDirective());
		this.templateLoader = new StringTemplateLoader();
	}
	
	

	@Override
	public void initParser() {
		this.initialize();
		for(PropertiesNamespaceInfo<T> namespace : this.sqlManager.getAllNamespaceProperties()){
			for(T info : namespace.getNamedProperties()){
				this.templateLoader.putTemplate(info.getFullName(), info.getSql());
				this.templateLoader.putTemplate(info.getCountName(), info.getCountSql());
				for(Entry<String, String> entry : info.getAttrs().entrySet()){
					this.templateLoader.putTemplate(info.getTemplateName(entry.getKey()), entry.getValue());
				}
			}
		}
	}



	@Override
	protected void buildConfigration(Configuration cfg) {
		cfg.setTagSyntax(Configuration.SQUARE_BRACKET_TAG_SYNTAX);
	}

	@Override
	protected TemplateLoader getTempateLoader() {
		return templateLoader;
	}


}
