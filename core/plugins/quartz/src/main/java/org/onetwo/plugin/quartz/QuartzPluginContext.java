package org.onetwo.plugin.quartz;

import java.util.List;

import javax.annotation.Resource;

import org.onetwo.common.spring.SpringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;


@Configuration
public class QuartzPluginContext implements InitializingBean {

	@Resource
	private ApplicationContext applicationContext;
	
	public QuartzPluginContext(){
		System.out.println("QuartzPluginContext");
	}
	

	@Bean
	public SchedulerFactoryBean jfishSchedulerFactoryBean(){
		SchedulerFactoryBean fb = SpringUtils.getBean(applicationContext, SchedulerFactoryBean.class);
		if(fb==null){
			fb = new JFishSchedulerFactoryBean();
		}
		return fb;
	}
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		List<QuartzJobTask> tasklist = SpringUtils.getBeans(applicationContext, QuartzJobTask.class);
		System.out.println("===========>>register task");
		int i = 0;
		for(QuartzJobTask task : tasklist){
			SpringUtils.registerBean(applicationContext, "jobDetailFactoryBean#"+i, JobDetailFactoryBean.class,
					"targetObject", task);
			i++;
		}
	}

	
}
