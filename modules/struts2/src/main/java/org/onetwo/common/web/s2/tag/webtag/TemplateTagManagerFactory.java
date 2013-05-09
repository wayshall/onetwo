package org.onetwo.common.web.s2.tag.webtag;

import org.apache.log4j.Logger;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.params.TagParamsMap;

public class TemplateTagManagerFactory {
	
	private static final Logger logger = Logger.getLogger(TemplateTagManagerFactory.class);
	
	private static final String CONFIG_NAME = "tag-service-mapper.properties";

	private static TemplateTagManager DEFAULT ;
	private static boolean isSetup;

	public static void setup(){
		DEFAULT = new DefaultTemplateTagManager(CONFIG_NAME);
		DEFAULT.load();
		TagParamsMap.loadVarKey();
		isSetup = true;
		if(logger.isInfoEnabled()){
			logger.info("template tag has setup...");
		}
	}

	public static void reload(){
		DEFAULT.reload();
		TagParamsMap.reloadVarKey();
		if(logger.isInfoEnabled()){
			logger.info("template tag has reload...");
		}
	}
	
	protected static void checkSetup(){
		if(!isSetup)
			throw new ServiceException("the tempalte tag has not setup!");
	}
	
	public static TemplateTagManager getDefault(){
		checkSetup();
		return DEFAULT;
	}
	
	public static void main(String[] args){
		DefaultTemplateTagManager d = new DefaultTemplateTagManager();
		d.registerTagExecutor("新闻>*", new AbstractTagExecutor(){

			@Override
			public Object execute(TagParamsMap params) {
				return "新闻>*";
			}
			
		});
		d.registerTagExecutor("test2", new AbstractTagExecutor(){

			@Override
			public Object execute(TagParamsMap params) {
				// TODO Auto-generated method stub
				return "test2";
			}
			
		});
		TemplateTagManagerFactory.DEFAULT = d;
		TemplateTagManagerFactory.isSetup = true;
		TagExecutor t = TemplateTagManagerFactory.getDefault().getTagExcutor("新闻>行业资讯>最佳旅游");
		System.out.println(t.execute(null));
		
		
	}
	
}