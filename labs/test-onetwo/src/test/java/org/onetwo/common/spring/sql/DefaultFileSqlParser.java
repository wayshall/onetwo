package org.onetwo.common.spring.sql;

import java.io.StringWriter;
import java.util.Locale;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.ftl.DynamicFreemarkerTemplateConfigurer;
import org.onetwo.common.spring.ftl.ForeachDirective;
import org.onetwo.common.spring.ftl.StringTemplateProvider;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class DefaultFileSqlParser<T extends JFishNamedFileQueryInfo> extends DynamicFreemarkerTemplateConfigurer implements FileSqlParser<T> {
	public static final String FTL_PREFIX = "[ftl]";//for ftl
	public static final String QUERY_POSTFIX = ".query";//for ftl
	
	private JFishNamedSqlFileManager<T> sqlManager;
	
	public String asFtlContent(String content){
		return FTL_PREFIX + content;
	}
	
	public DefaultFileSqlParser(final JFishNamedSqlFileManager<T> sqlm) {
		super();
		this.sqlManager = sqlm;
		
		addDirective(new ForeachDirective());
		
		this.setTemplateProvider(new StringTemplateProvider() {
			
			@Override
			public String getTemplateContent(String qname) {
				String name = qname.substring(0, qname.length()-QUERY_POSTFIX.length());
				if(name.endsWith(Locale.CHINA.toString()) || name.endsWith(Locale.CHINESE.toString())){
					return null;
				}

				if(name.startsWith(FTL_PREFIX)){
					return name.substring(FTL_PREFIX.length());
				}
				boolean count = JFishNamedFileQueryInfo.isCountName(name);
				String realName = JFishNamedFileQueryInfo.trimCountPostfix(name);
				JFishNamedFileQueryInfo info = sqlManager.getNamedQueryInfo(realName);
				return count?info.getCountSql():info.getSql();
			}
		});
	}

	@Override
	protected void buildConfigration(Configuration cfg) {
		cfg.setTagSyntax(Configuration.SQUARE_BRACKET_TAG_SYNTAX);
	}


	@Override
	public String parse(String name, Object context){
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
