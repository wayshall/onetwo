package org.onetwo.common.web.s2.tag.webtag;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.spring.SpringApplication;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.propconf.PropConfig;
import org.onetwo.common.utils.propconf.PropUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

@SuppressWarnings("unchecked")
public class DefaultTemplateTagManager implements TemplateTagManager {
	
	protected Logger logger = Logger.getLogger(DefaultTemplateTagManager.class);
	
	private Map<String, TagExecutor> tagServiceMapper = new HashMap<String, TagExecutor>();
	
	private Map<String, LinkAdapter> linkAdapters = new HashMap<String, LinkAdapter>();
	
	private String configName;
	
	private PathMatcher nameMatcher = new AntPathMatcher();
	
	public DefaultTemplateTagManager(){
	}
	
	public DefaultTemplateTagManager(String configName){
		this.configName = configName;
	}
	
	public boolean load(){
		PropConfig config = PropUtils.loadPropConfig(configName, false);
		if(config==null)
			return false;
		Enumeration<String> enu = (Enumeration<String>)config.configNames();
		String key = null;
		while(enu.hasMoreElements()){
			key = enu.nextElement();
			this.register(key, config.getProperty(key));
		}
		return true;
	}
	
	public boolean reload(){
		clear();
		return load();
	}
	
	public void clear(){
		this.tagServiceMapper.clear();
		this.linkAdapters.clear();
	}
	
	public TemplateTagManager registerTagExecutor(String tag, TagExecutor executor){
		Assert.hasText(tag, "tag must has text!");
		this.tagServiceMapper.put(tag, executor);
		return this;
	}

	
	public TemplateTagManager registerLinkAdapter(String tag, LinkAdapter linkAdapter){
		Assert.hasText(tag, "tag must has text!");
		this.linkAdapters.put(tag, linkAdapter);
		return this;
	}
	
	public TemplateTagManager register(String tag, String beanName){
		Object executor = SpringApplication.getInstance().getBean(beanName);
		if(executor==null){
			String sn = StringUtils.getSimpleBeanName(beanName);
			executor = SpringApplication.getInstance().getBean(sn);
		}
		if(executor==null)
			executor = ReflectUtils.newInstance(beanName);
		if(executor==null)
			throw new ServiceException("no tag service name : " + beanName);
		
		if(executor instanceof TagExecutor)
			registerTagExecutor(tag, (TagExecutor)executor);
		else if(executor instanceof LinkAdapter)
			registerLinkAdapter(tag, (LinkAdapter)executor);
		else
			logger.error("the service["+executor+"] has no register!");
		
		return this;
	}
	
	public TagExecutor getTagExcutor(String name){
		TagExecutor tager = this.tagServiceMapper.get(name);
		for(Map.Entry<String, TagExecutor> entry : this.tagServiceMapper.entrySet()){
			if(this.nameMatcher.match(entry.getKey(), name))
				return entry.getValue();
		}
		
		if(tager != null) {
			return tager;
		} 
		
		return (TagExecutor)SpringApplication.getInstance().getBean(name);
	}
	
	public LinkAdapter getLinkAdapter(String name){
		return this.linkAdapters.get(name);
	}
}
