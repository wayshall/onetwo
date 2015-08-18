package org.onetwo.common.spring.sql;

import java.util.Enumeration;
import java.util.List;

import org.onetwo.common.db.filequery.FileNamedQueryException;
import org.onetwo.common.db.filequery.NamespacePropertiesFileManagerImpl.JFishPropertyConf;
import org.onetwo.common.db.filequery.NamespaceProperty;
import org.onetwo.common.db.filequery.PropertiesNamespaceInfo;
import org.onetwo.common.db.filequery.SimpleSqlFileLineLexer;
import org.onetwo.common.db.filequery.SimpleSqlFileLineLexer.LineToken;
import org.onetwo.common.db.filequery.SimpleSqlFileLineReader;
import org.onetwo.common.db.filequery.SqlFileParser;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.propconf.JFishProperties;
import org.onetwo.common.utils.propconf.ResourceAdapter;
import org.slf4j.Logger;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

public class MultipCommentsSqlFileParser implements SqlFileParser<JFishNamedFileQueryInfo> {
	
	public static final String GLOBAL_NS_KEY = "global";
	public static final String AT = "@";
//	public static final String EQUALS_MARK = "=";
	public static final String COLON = ":";
	
	
	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	protected boolean debug = false;
//	protected LineLexer lineLexer;
	

	/*public DefaultSqlFileParser2(ResourceAdapter<?> f){
		lineLexer = new LineLexer(createLineReader(f));
	}
	
	public DefaultSqlFileParser2(List<String> lines){
		this.lineLexer = new LineLexer(new LineReader(lines));
	}
	
	public DefaultSqlFileParser2(LineReader lineReader){
		this.lineLexer = new LineLexer(lineReader);
	}*/
	
	@Override
	public void parseToNamespaceProperty(JFishPropertyConf<JFishNamedFileQueryInfo> conf, PropertiesNamespaceInfo<JFishNamedFileQueryInfo> np, ResourceAdapter<?> f) {
		if(!f.getName().endsWith(conf.getPostfix())){
			logger.info("file["+f.getName()+" is not a ["+conf.getPostfix()+"] file, ignore it.");
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
					List<String> comments = lineLexer.getLineBuf();
					JFishProperties config = parseComments(comments);
					//name
					String name = config.getAndThrowIfEmpty(JFishNamedFileQueryInfo.NAME_KEY);
					config.remove(JFishNamedFileQueryInfo.NAME_KEY);
					
					JFishNamedFileQueryInfo bean = np.getNamedProperty(name);
					String sqlPropertyName = "value";
					if(bean==null){
						bean = ReflectUtils.newInstance(conf.getPropertyBeanClass());
						bean.setNamespaceInfo(np);
						bean.setSrcfile(f);

						bean.setName(name);
						bean.setConfig(config);

						np.put(bean.getName(), bean, true);
					}else{
						if(config.containsKey(JFishNamedFileQueryInfo.PROPERTY_KEY)){
							//property
							sqlPropertyName = config.getProperty(JFishNamedFileQueryInfo.PROPERTY_KEY);
							config.remove(JFishNamedFileQueryInfo.PROPERTY_KEY);
							
						}else if(config.containsKey(JFishNamedFileQueryInfo.FRAGMENT_KEY)){
							//fragment
							sqlPropertyName = JFishNamedFileQueryInfo.FRAGMENT_KEY + "[" + 
																		config.getProperty(JFishNamedFileQueryInfo.FRAGMENT_KEY)
																				+ "]";
							config.remove(JFishNamedFileQueryInfo.FRAGMENT_KEY);
							
						}else{
							throw new FileNamedQueryException("named query["+name+"]'s  must be specify a @property or @fragment."
									+ "line : " + lineLexer.getLineReader().getLineNumber());
						}
//						sqlPropertyName = config.getProperty(PROPERTY_KEY);
					}
					
					
					if(debug)
						logger.info("config: {}", config);
					
					Enumeration<?> keys = config.propertyNames();
					BeanWrapper beanBw = PropertyAccessorFactory.forBeanPropertyAccess(bean);
					while(keys.hasMoreElements()){
						String prop = keys.nextElement().toString();
						this.setNamedInfoProperty(beanBw, prop, config.getProperty(prop));
					}
					
					StringBuilder buf = new StringBuilder();
					while(lineLexer.nextLineToken()){
						if(lineLexer.getLineToken()==LineToken.EOF){
							break;
						}else if(lineLexer.getLineToken()==LineToken.MULTIP_COMMENT){
							break;
						}else if(lineLexer.getLineToken()==LineToken.CONTENT){
							String value = StringUtils.join(lineLexer.getLineBuf(), " ");
							buf.append(value).append(" ");
						}else{
							throw new FileNamedQueryException("error syntax: " + lineLexer.getLineToken());
						}
					}
//					bean.setValue(buf.toString());
					beanBw.setPropertyValue(sqlPropertyName, buf.toString());
					
					if(debug)
						logger.info("value: {}", bean.getValue());
					
					break;
					
				default:
					if(debug)
						logger.info("ignore token {} : {} ", token, lineLexer.getLineBuf());
					
					lineLexer.nextLineToken();
					break;
			}
		}
		
	}
	
	
	protected JFishProperties parseComments(List<String> comments){
		JFishProperties config = new JFishProperties();
		for(final String comment : comments){
			logger.info("comment: {}", comment);
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
		if(prop.indexOf(NamespaceProperty.DOT_KEY)!=-1){
			prop = StringUtils.toJavaName(prop, NamespaceProperty.DOT_KEY, false);
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
