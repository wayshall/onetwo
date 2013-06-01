package org.onetwo.plugins.fmtagext.spring;

import java.util.List;

import org.onetwo.common.spring.SpringUtils;
import org.onetwo.plugins.fmtagext.BaseRestController;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class PageBuilder implements ApplicationContextAware {

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		List<BaseRestController> controllers = SpringUtils.getBeans(applicationContext, BaseRestController.class);
		
		for(BaseRestController<?> rc : controllers){
			rc.initBuild();
			System.out.println(rc.getClass()+" initBuild...");
		}
		
	}
}
