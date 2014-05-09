package org.onetwo.plugin.quartz;

import java.util.List;

import org.onetwo.common.spring.SpringUtils;
import org.quartz.Trigger;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

public class JFishSchedulerFactoryBean extends SchedulerFactoryBean {
	
	private ApplicationContext context;

	public void afterPropertiesSet() throws Exception {
		List<Trigger> triggerlist = SpringUtils.getBeans(context, Trigger.class);
		System.out.println("triggers: " + triggerlist);
		setTriggers(triggerlist.toArray(new Trigger[triggerlist.size()]));
		setStartupDelay(5);
	}
	
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.context = applicationContext;
		super.setApplicationContext(applicationContext);
	}
}
