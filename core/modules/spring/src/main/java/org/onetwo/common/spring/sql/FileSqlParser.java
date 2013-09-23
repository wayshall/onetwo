package org.onetwo.common.spring.sql;

import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.ftl.AbstractFreemarkerTemplate;
import org.onetwo.common.spring.ftl.StringTemplateProvider;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FileSqlParser<T extends JFishNamedFileQueryInfo> extends AbstractFreemarkerTemplate {
	public static final String QUERY_POSTFIX = ".query";//for ftl
	
	private JFishNamedSqlFileManager<T> sqlManager;
	
	public FileSqlParser(final JFishNamedSqlFileManager<T> sqlm) {
		super();
		this.sqlManager = sqlm;
		
		this.setTemplateProvider(new StringTemplateProvider() {
			
			@Override
			public String getTemplateContent(String qname) {
				String name = qname.substring(0, qname.length()-QUERY_POSTFIX.length());
				if(name.endsWith(Locale.CHINA.toString()) || name.endsWith(Locale.CHINESE.toString())){
					return null;
				}
				
				boolean count = JFishNamedFileQueryInfo.isCountName(name);
				String realName = JFishNamedFileQueryInfo.trimCountPostfix(name);
				JFishNamedFileQueryInfo info = sqlManager.getNamedQueryInfo(realName);
				return count?info.getCountSql():info.getSql();
			}
		});
	}
	
	protected void buildConfigration(Configuration cfg) {
		cfg.setTagSyntax(Configuration.SQUARE_BRACKET_TAG_SYNTAX);
	}


	public String parseSql(String name, Map<Object, Object> context){
		Template template = getTemplate(name+QUERY_POSTFIX);
		StringWriter sw = new StringWriter();
		try {
			template.process(context, sw);
		}catch (Exception e) {
			throw new BaseException("parse template error: " + e.getMessage(), e);
		}
		return sw.toString();
	}
}
