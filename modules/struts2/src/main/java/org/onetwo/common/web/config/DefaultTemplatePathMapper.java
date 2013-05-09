package org.onetwo.common.web.config;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.propconf.PropConfig;
import org.onetwo.common.utils.propconf.PropUtils;
import org.onetwo.common.web.subdomain.SubdomainRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * 处理portal的模板目录
 * 把/portal解释成/portal/${template?template:"template1"}的目录路径
 * @author weishao
 *
 */
@SuppressWarnings("unchecked")
public class DefaultTemplatePathMapper implements TemplatePathMapper {
	
	public static final String TEMPLATE_HOLDER = "#template#";
	public static final String CONFIG_PREFIX = "config:";

	private static TemplatePathMapper instance = new DefaultTemplatePathMapper();

	public static TemplatePathMapper getInstance() {
		return instance;
	}
	
//	private SimpleExpression se = new SimpleExpression("#{", "}", new TemplateValueProvider());

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	protected List<SubdomainRule> rules = new ArrayList<SubdomainRule>();
	
	private boolean enable;
	
	private String configName;
	private PropConfig pathMapper;
	private Map<String, TemplateInfo> templateConfig = new HashMap<String, TemplateInfo>();
	private TemplateProvider templateProvider;

	public DefaultTemplatePathMapper() {
		this.configName = "template-path-mapper.properties";
		load();
	}
	
	public void clear(){
		this.rules.clear();
	}
	
	public void reload(){
		clear();
		load();
	}
	
	protected void load(){
		pathMapper = PropUtils.loadPropConfig(configName, false);
		if(pathMapper==null){
			enable = false;
			return ;
		}
		String enableStr = pathMapper.getProperty("enable", "false");
		this.enable = "true".equals(enableStr);
		if(!enable)
			return ;
		Enumeration<String> enu = (Enumeration<String>)pathMapper.configNames();
		SubdomainRule r = null;
		String key = null;
		while(enu.hasMoreElements()){
			key = enu.nextElement();
			if(key.equals("enable"))
				continue;
			else if(key.startsWith(CONFIG_PREFIX)){
				this.processConfig(key, pathMapper.getProperty(key));
			}
			else{
				r = new SubdomainRule();
				r.setFrom(key);
				r.setTo(pathMapper.getProperty(key));
				
				rules.add(r);
			}
		}
		if(this.templateProvider==null)
			this.templateProvider = new SimpleTemplateProvider();
	}
	
	protected void processConfig(String key, String value){
		if(StringUtils.isBlank(value))
			return ;
		String val = key.substring(CONFIG_PREFIX.length());
		if("template.provider".equals(val)){
			this.templateProvider = ReflectUtils.newInstance(value);
		}
	}
	
	protected SubdomainRule getMatchRule(String path){
		for (SubdomainRule r : rules) {
			if (!r.isMatche(path))
				continue;
			return r;
		}
		return null;
	}

	public boolean isInited() {
		return !this.rules.isEmpty();
	}

	public String parse(String path, boolean isResource){ 
		if(!isEnable() || !isInited() || !templateProvider.isEnable())
			return path;
		SubdomainRule r = getMatchRule(path);
		if(r==null)
			return path;
		String newPath = path;
		newPath = r.parse(newPath);
		String tempDir = this.templateProvider.getTemplateDir();
		
		if(!isResource){
			if(tempDir!=null)
				newPath = newPath.replace(TEMPLATE_HOLDER, tempDir);
			return newPath;
		}
		
		String resource = null;
		while(StringUtils.isNotBlank(tempDir)){
			newPath = newPath.replace(TEMPLATE_HOLDER, tempDir);
         	resource = SiteConfig.getInstance().findResource(newPath);
         	if(StringUtils.isBlank(resource)){
         		tempDir = getParent(tempDir);
         	}else{
         		break;
         	}
		}
		
		if(StringUtils.isNotBlank(resource)){
			newPath = newPath.replaceAll("/+", "/");
			return newPath;
		}
		return path;
	}

	
	public String getParent(String templatePath){
		if(!templatePath.endsWith("/"))
			templatePath = templatePath + "/";
		
		TemplateInfo info = null;
		
		synchronized (this) {
			if(this.templateConfig.containsKey(templatePath)){
				info = this.templateConfig.get(templatePath);
				if(info!=null)
					return info.parent;
				else
					return null;
			}
			
			String templateRes = SiteConfig.getInstance().findResource(templatePath + "template.properties");
			if(StringUtils.isNotBlank(templateRes)){
				Properties prop = null;
				try {
					prop = PropUtils.loadProperties(templateRes);
					info = createTemplateInfo(templatePath, prop);
				} catch (Exception e) {
					logger.error("load template path config error : " + e.getMessage(), e);
				}
			}
			if(info!=null)
				this.templateConfig.put(templatePath, info);
		}
		
		if(info==null)
			return null;
		else
			return info.parent;
	}
	
	protected TemplateInfo createTemplateInfo(String path, Properties config){
		return new TemplateInfo(path, config==null?null:config.getProperty("parent"));
	}
	
	public boolean isEnable() {
		return enable;
	}
	
	public static class TemplateInfo {
		public String path;
		public String parent;
		
		public TemplateInfo(String path, String parent) {
			super();
			this.path = path;
			this.parent = parent;
		}
		
		
	}
	
	public static void main(String[] args){
		String path = "/zh/aa.jsp";
		String res = DefaultTemplatePathMapper.getInstance().parse(path, false);
		System.out.println(res);
	}

}
