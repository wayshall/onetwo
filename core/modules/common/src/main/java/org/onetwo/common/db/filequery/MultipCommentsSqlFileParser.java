package org.onetwo.common.db.filequery;

import java.util.Enumeration;
import java.util.List;

import org.onetwo.common.db.filequery.LineLexer.LineToken;
import org.onetwo.common.db.filequery.NamespacePropertiesFileManagerImpl.JFishPropertyConf;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.propconf.JFishProperties;
import org.onetwo.common.utils.propconf.ResourceAdapter;
import org.slf4j.Logger;

public class MultipCommentsSqlFileParser<T extends NamespaceProperty> implements SqlFileParser<T> {
	
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
	public void parseToNamespaceProperty(JFishPropertyConf<T> conf, PropertiesNamespaceInfo<T> np, ResourceAdapter<?> f) {
		if(!f.getName().endsWith(conf.getPostfix())){
			logger.info("file["+f.getName()+" is not a ["+conf.getPostfix()+"] file, ignore it.");
			return ;
		}
		LineLexer lineLexer = new LineLexer(createLineReader(f));
		lineLexer.nextLineToken();
		while(lineLexer.getLineToken()!=LineToken.EOF){
			LineToken token = lineLexer.getLineToken();
			if(debug)
				logger.info("current token {}  ", token);
			
			switch (token) {
				case MULTIP_COMMENT:
					T bean = ReflectUtils.newInstance(conf.getPropertyBeanClass());
					bean.setNamespaceInfo(np);
					bean.setSrcfile(f);
					
					List<String> comments = lineLexer.getLineBuf();
					JFishProperties config = parseComments(comments);
					bean.setName(config.getAndThrowIfEmpty("name"));
					config.remove("name");
					bean.setConfig(config);
					
					if(debug)
						logger.info("config: {}", config);
					
					Enumeration<?> keys = config.propertyNames();
					while(keys.hasMoreElements()){
						String prop = keys.nextElement().toString();
						this.setNamedInfoProperty(bean, prop, config.getProperty(prop));
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
							throw new BaseException("error syntax: " + lineLexer.getLineToken());
						}
					}
					bean.setValue(buf.toString());
					
					if(debug)
						logger.info("value: {}", bean.getValue());
					
					np.put(bean.getName(), bean, true);
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
					config.setProperty(strs[0], strs[1]);
				}else{
					throw new BaseException("error syntax for config: " + comment);
				}
			}
		}
		return config;
	}
	
	protected void setNamedInfoProperty(T bean, String prop, Object val){
		if(prop.indexOf(NamespaceProperty.DOT_KEY)!=-1){
			prop = StringUtils.toJavaName(prop, NamespaceProperty.DOT_KEY, false);
		}
		try {
			ReflectUtils.setExpr(bean, prop, val);
		} catch (Exception e) {
			logger.error("set value error : "+prop);
			LangUtils.throwBaseException(e);
		}
	}
	

	protected List<String> readResourceAsList(ResourceAdapter<?> f){
		if(f.isSupportedToFile())
			return FileUtils.readAsList(f.getFile());
		else
			throw new UnsupportedOperationException();
	}

	protected LineReader createLineReader(ResourceAdapter<?> f){
		return new LineReader(readResourceAsList(f));
	}
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

}
