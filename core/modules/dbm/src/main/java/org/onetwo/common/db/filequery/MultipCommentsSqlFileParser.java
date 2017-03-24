package org.onetwo.common.db.filequery;

import java.util.Enumeration;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.db.filequery.SimpleSqlFileLineLexer.LineToken;
import org.onetwo.common.db.filequery.spi.SqlDirectiveExtractor;
import org.onetwo.common.db.filequery.spi.SqlFileParser;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.propconf.JFishProperties;
import org.onetwo.common.propconf.ResourceAdapter;
import org.onetwo.dbm.exception.FileNamedQueryException;
import org.slf4j.Logger;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

public class MultipCommentsSqlFileParser implements SqlFileParser {
	
	public static class SimpleDirectiveExtractor implements SqlDirectiveExtractor {
		public static final String DIRECTIVE_PREFIX = SimpleSqlFileLineLexer.COMMENT + ">";//-->
		public static final String DIRECTIVE_START = DIRECTIVE_PREFIX + "[";
		public static final String DIRECTIVE_END = "]";

		@Override
		public boolean isDirective(String value) {
			return value.startsWith(DIRECTIVE_START) && value.endsWith(DIRECTIVE_END);
		}

		@Override
		public String extractDirective(String value) {
//			String directive = StringUtils.substringBetween(value, DIRECTIVE_START, DIRECTIVE_END);// value.substring(SimpleSqlFileLineLexer.COMMENT.length());
			String directive = StringUtils.substringAfter(value, DIRECTIVE_PREFIX);// value.substring(SimpleSqlFileLineLexer.COMMENT.length());
			return directive;
		}
		
	}
	
	public static final String GLOBAL_NS_KEY = "global";
	public static final String AT = "@";
//	public static final String EQUALS_MARK = "=";
	public static final String COLON = ":";
	
	
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	protected boolean debug = false;
	protected SqlDirectiveExtractor sqlDirectiveExtractor = new SimpleDirectiveExtractor();
	
	@Override
	public void parseToNamespaceProperty(DbmNamedQueryFile namespaceInfo, ResourceAdapter<?> f) {
		if(!f.getName().endsWith(POSTFIX)){
			logger.info("file["+f.getName()+" is not a ["+POSTFIX+"] file, ignore it.");
			return ;
		}
		SimpleSqlFileLineLexer lineLexer = new SimpleSqlFileLineLexer(createLineReader(f));
		lineLexer.nextLineToken();
		while(lineLexer.getLineToken()!=LineToken.EOF){
			LineToken token = lineLexer.getLineToken();
			if(debug)
				logger.info("current token {}  ", token);
			
			switch (token) {
				case MULTIP_COMMENT:
					this.parseQueryStatement(lineLexer, namespaceInfo, f);
					break;
					
				default:
					if(debug)
						logger.info("ignore token {} : {} ", token, lineLexer.getLineBuf());
					
					lineLexer.nextLineToken();
					break;
			}
		}
		
	}
	
	protected void parseQueryStatement(SimpleSqlFileLineLexer lineLexer,
										DbmNamedQueryFile namespaceInfo, ResourceAdapter<?> f){
		List<String> comments = lineLexer.getLineBuf();
		JFishProperties config = parseComments(comments);
		//name
		//没有发现 @name 属性的注释，抛错……
//		String name = config.getAndThrowIfEmpty(DbmNamedFileQueryInfo.NAME_KEY);
		String name = config.getProperty(DbmNamedQueryInfo.NAME_KEY);
		//没有发现 @name 属性的注释，忽略
		if(StringUtils.isBlank(name)){
			scanSqlContent(lineLexer);
			return ;
		}
		config.remove(DbmNamedQueryInfo.NAME_KEY);
		
		DbmNamedQueryInfo bean = namespaceInfo.getNamedProperty(name);
		String sqlPropertyName = "value";
		if(bean==null){
			bean = new DbmNamedQueryInfo();
			bean.setDbmNamedQueryFile(namespaceInfo);
			bean.setSrcfile(f);

			bean.setName(name);
			bean.setConfig(config);

			namespaceInfo.put(bean.getName(), bean, true);

			//别名
			if(config.containsKey(DbmNamedQueryInfo.ALIAS_KEY)){
				bean.setAliasList(config.getStringList(DbmNamedQueryInfo.ALIAS_KEY, ","));
				config.remove(DbmNamedQueryInfo.ALIAS_KEY);
			}
			
			/*if(config.containsKey(JFishNamedFileQueryInfo.MATCHER_KEY)){
				//matcher
				String matchers = config.getProperty(JFishNamedFileQueryInfo.MATCHER_KEY);
				bean.setMatchers(Arrays.asList(StringUtils.split(matchers, JFishNamedFileQueryInfo.MATCHER_SPIT_KEY)));
				config.remove(JFishNamedFileQueryInfo.MATCHER_KEY);
				
			}*/
			if(config.containsKey(DbmNamedQueryInfo.PROPERTY_KEY)
					|| config.containsKey(DbmNamedQueryInfo.FRAGMENT_KEY)){
				throw new FileNamedQueryException("no parent query["+bean.getName()+"] found, the @property or @fragment tag must defined in a subquery, near at "
						+ "line : " + lineLexer.getLineReader().getLineNumber());
			}
		}else{
			if(config.containsKey(DbmNamedQueryInfo.PROPERTY_KEY)){
				//property:propertyName
				sqlPropertyName = config.getProperty(DbmNamedQueryInfo.PROPERTY_KEY);
				config.remove(DbmNamedQueryInfo.PROPERTY_KEY);
				
			}else if(config.containsKey(DbmNamedQueryInfo.FRAGMENT_KEY)){
				//fragment[fragmentValue]=sql
				sqlPropertyName = DbmNamedQueryInfo.FRAGMENT_KEY + "[" + 
															config.getProperty(DbmNamedQueryInfo.FRAGMENT_KEY)
																	+ "]";
				config.remove(DbmNamedQueryInfo.FRAGMENT_KEY);
				
			}else{
				throw new FileNamedQueryException("named query["+name+"]'s  must be specify a @property or @fragment."
						+ "near at line : " + lineLexer.getLineReader().getLineNumber());
			}
//			sqlPropertyName = config.getProperty(PROPERTY_KEY);
		}
		
		
		if(debug)
			logger.info("config: {}", config);
		
		Enumeration<?> keys = config.propertyNames();
		BeanWrapper beanBw = PropertyAccessorFactory.forBeanPropertyAccess(bean);
		while(keys.hasMoreElements()){
			String prop = keys.nextElement().toString();
			this.setNamedInfoProperty(beanBw, prop, config.getProperty(prop));
		}
		
		String sqlContent = scanSqlContent(lineLexer);
//		bean.setValue(buf.toString());
		beanBw.setPropertyValue(sqlPropertyName, sqlContent);
		
		if(debug)
			logger.info("value: {}", bean.getValue());
	}
	
	private String scanSqlContent(SimpleSqlFileLineLexer lineLexer){
		StringBuilder buf = new StringBuilder();
		while(lineLexer.nextLineToken()){
			if(lineLexer.getLineToken()==LineToken.EOF){
				break;
			}else if(lineLexer.getLineToken()==LineToken.MULTIP_COMMENT){
				break;
			}else if(lineLexer.getLineToken()==LineToken.ONE_LINE_COMMENT){
				String value = StringUtils.join(lineLexer.getLineBuf(), " ");
				if(sqlDirectiveExtractor.isDirective(value)){//-->[#if ... ]
					buf.append(sqlDirectiveExtractor.extractDirective(value)).append(" ");
				}
				continue;
			}else if(lineLexer.getLineToken()==LineToken.CONTENT){
				String value = StringUtils.join(lineLexer.getLineBuf(), " ");
				buf.append(value).append(" ");
			}else{
				throw new FileNamedQueryException("error syntax: " + lineLexer.getLineToken());
			}
		}
		return buf.toString();
	}
	
	
	protected JFishProperties parseComments(List<String> comments){
		JFishProperties config = new JFishProperties();
		for(final String comment : comments){
//			logger.info("comment: {}", comment);
			if(comment.startsWith(AT)){
				String line = comment.substring(AT.length());
				String[] strs = StringUtils.split(line, COLON);
				if(strs.length==2){
					config.setProperty(strs[0].trim(), strs[1].trim());
				}else{
					throw new FileNamedQueryException("error syntax for config: " + comment);
				}
			}
		}
		return config;
	}
	
	protected void setNamedInfoProperty(BeanWrapper beanBw, String prop, Object val){
		if(prop.indexOf(DbmNamedQueryInfo.DOT_KEY)!=-1){
			prop = org.onetwo.common.utils.StringUtils.toCamel(prop, DbmNamedQueryInfo.DOT_KEY, false);
		}
		beanBw.setPropertyValue(prop, val);
		/*try {
			ReflectUtils.setExpr(bean, prop, val);
		} catch (Exception e) {
			logger.error("set value error : "+prop);
			LangUtils.throwBaseException(e);
		}*/
	}
	
/*
	protected List<String> readResourceAsList(ResourceAdapter<?> f){
		if(f.isSupportedToFile())
			return FileUtils.readAsList(f.getFile());
		else
			throw new UnsupportedOperationException();
	}*/

	protected SimpleSqlFileLineReader createLineReader(ResourceAdapter<?> f){
//		return new SimpleSqlFileLineReader(readResourceAsList(f));
		return new SimpleSqlFileLineReader(f.readAsList());
	}
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

}
