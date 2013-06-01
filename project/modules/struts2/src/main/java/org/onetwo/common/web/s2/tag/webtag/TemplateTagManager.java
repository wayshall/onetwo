package org.onetwo.common.web.s2.tag.webtag;

public interface TemplateTagManager {

	public boolean reload();
	
	public boolean load();
	
	public TemplateTagManager register(String tag, String beanName);

	public TagExecutor getTagExcutor(String name);
	
	public LinkAdapter getLinkAdapter(String name);

}