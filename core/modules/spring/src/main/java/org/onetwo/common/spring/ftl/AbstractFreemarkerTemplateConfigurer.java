package org.onetwo.common.spring.ftl;

import java.util.Map;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.CharsetUtils;
import org.onetwo.common.utils.LangUtils;
import org.slf4j.Logger;

import freemarker.cache.TemplateLoader;
import freemarker.core.ParseException;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.Template;

abstract public class AbstractFreemarkerTemplateConfigurer{
	public static final BeansWrapper INSTANCE = FtlUtils.BEAN_WRAPPER;

	static {
		INSTANCE.setSimpleMapWrapper(true);
	}

	protected final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	private Configuration configuration;
	
	private String encoding = CharsetUtils.UTF_8;
	
	private Map<String, Object> freemarkerVariables;
	
	private boolean initialized;

	
	public AbstractFreemarkerTemplateConfigurer(){
	}

	protected BeansWrapper getBeansWrapper(){
		return INSTANCE;
	}
	
	final public AbstractFreemarkerTemplateConfigurer addDirective(NamedDirective directive){
		if(this.freemarkerVariables==null)
			this.freemarkerVariables = LangUtils.newHashMap();
		this.freemarkerVariables.put(directive.getName(), directive);
		return this;
	}
	
	public boolean isInitialized() {
		return initialized;
	}

	/****
	 * must be invoke after contruction
	 */
	public void initialize() {
		if(isInitialized())
			return ;
		
		TemplateLoader loader = getTempateLoader();
		Assert.notNull(loader);
		try {
			this.configuration = new Configuration(Configuration.VERSION_2_3_0);
			this.configuration.setObjectWrapper(getBeansWrapper());
			this.configuration.setOutputEncoding(this.encoding);
			//设置默认不自动格式化数字……以防sb……
			this.configuration.setNumberFormat("#");
//			this.cfg.setDirectoryForTemplateLoading(new File(templateDir));
			
			/*if(templateProvider!=null){
				this.configuration.setTemplateLoader(new DynamicTemplateLoader(templateProvider));
			}*/
			if(LangUtils.isNotEmpty(getFreemarkerVariables()))
				configuration.setAllSharedVariables(new SimpleHash(freemarkerVariables, configuration.getObjectWrapper()));
			
			//template loader
			/*if(!LangUtils.isEmpty(templatePaths)){
				TemplateLoader loader = FtlUtils.getTemplateLoader(resourceLoader, templatePaths);
				this.configuration.setTemplateLoader(loader);
			}*/
			this.configuration.setTemplateLoader(loader);
			
			this.buildConfigration(this.configuration);
			
			initialized = true;
		} catch (Exception e) {
			throw new BaseException("create freemarker template error : " + e.getMessage(), e);
		}
	}
	
	abstract protected TemplateLoader getTempateLoader();
	protected void buildConfigration(Configuration cfg) {
	}

	public Template getTemplate(String name){
		Template template;
		try {
			template = getConfiguration().getTemplate(name);
		}catch (ParseException e) {
			throw new BaseException("sql tempalte["+name+"] syntax error : " + e.getMessage());
		} catch (Exception e) {
			throw new BaseException("get tempalte["+name+"] error : " + e.getMessage(), e);
		}
		return template;
	}
	
	/*public String parse(String name, Object rootMap){
		Template template = getTemplate(name);
		StringWriter sw = new StringWriter();
		try {
			template.process(rootMap, sw);
		} catch (TemplateException e) {
			Exception cause = e.getCauseException();
			if(cause!=null){
				throw LangUtils.asBaseException("parse tempalte error : " + cause.getMessage(), cause);
			}else{
				throw new BaseException("parse tempalte error : " + e.getMessage(), e);
			}
		} catch (Exception e) {
			throw new BaseException("parse tempalte error : " + e.getMessage(), e);
		}
		return sw.toString();
	}*/

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public String getEncoding() {
		return encoding;
	}

	public Map<String, Object> getFreemarkerVariables() {
		return freemarkerVariables;
	}

	public void setFreemarkerVariables(Map<String, Object> freemarkerVariables) {
		this.freemarkerVariables = freemarkerVariables;
	}


}
