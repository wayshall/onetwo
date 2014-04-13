package org.onetwo.common.spring.sql;

import java.util.Map.Entry;

import org.onetwo.common.spring.ftl.ForeachDirective;
import org.onetwo.common.spring.ftl.StringFreemarkerTemplateConfigurer;
import org.onetwo.common.utils.propconf.PropertiesNamespaceInfo;

import freemarker.template.Configuration;

public class StringTemplateProviderFileSqlParser<T extends JFishNamedFileQueryInfo> extends StringFreemarkerTemplateConfigurer implements FileSqlParser<T> {
	
//	public class TemplateProvider  implements StringTemplateProvider {
//		final private JFishNamedSqlFileManager<T> sqlm;
//
//		public TemplateProvider(JFishNamedSqlFileManager<T> sqlm) {
//			super();
//			this.sqlm = sqlm;
//		}
//		
//
//		public String getTemplateContent(String name){
//		}
//	}

//	public static final String QUERY_POSTFIX = ".query";//for ftl
	
	private JFishNamedSqlFileManager<T> sqlManager;
	
	
	public StringTemplateProviderFileSqlParser(final JFishNamedSqlFileManager<T> sqlm) {
		super();
		this.sqlManager = sqlm;
		
		addDirective(new ForeachDirective());
//		this.setTemplateProvider(templateProvider);
	}
	
	

	@Override
	public void initParser() {
		this.initialize();
		for(PropertiesNamespaceInfo<T> namespace : this.sqlManager.getAllNamespaceProperties()){
			for(T info : namespace.getNamedProperties()){
//				this.templateLoader.putTemplate(info.getFullName(), info.getSql());
				this.putTemplate(info.getFullName(), new NamespaceInfoTemplate(info.getFullName(), namespace));
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

	private class NamespaceInfoTemplate implements StringTemplate {
		
		private String queryName;
		private String attrName;
		private PropertiesNamespaceInfo<T> namespaces;
		

		public NamespaceInfoTemplate(String queryName,
				PropertiesNamespaceInfo<T> namespaces) {
			super();
			this.queryName = queryName;
			this.namespaces = namespaces;
		}


		public NamespaceInfoTemplate(String queryName, String attrName,
				PropertiesNamespaceInfo<T> namespaces) {
			super();
			this.queryName = queryName;
			this.attrName = attrName;
			this.namespaces = namespaces;
		}



		@Override
		public String getTemplate() {
			String content = "";
			
			return content;
		}
		
	}

}
